package team2.kakigowherebackend.service;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import team2.kakigowherebackend.dto.GoogleSearchDTO;
import team2.kakigowherebackend.dto.ManagePlaceDetailDTO;
import team2.kakigowherebackend.model.Place;
import team2.kakigowherebackend.repository.PlaceRepository;

@Service
public class ManagePlaceServiceImpl implements ManagePlaceService {

    private final PlaceRepository placeRepo;
    private final ImageService imageService;
    private final GooglePlaceService googlePlaceService;
    private final RetrievePlaceService retrievePlaceService;

    public ManagePlaceServiceImpl(
            PlaceRepository placeRepo,
            ImageService imageService,
            GooglePlaceService googlePlaceService,
            RetrievePlaceService retrievePlaceService) {
        this.placeRepo = placeRepo;
        this.imageService = imageService;
        this.googlePlaceService = googlePlaceService;
        this.retrievePlaceService = retrievePlaceService;
    }

    @Override
    public Page<Place> getPlaces(int page, int pageSize, String keyword) {
        Pageable pageable;
        pageable = PageRequest.of(page, pageSize);
        return placeRepo.getPlacesBySearch(keyword, pageable);
    }

    @Override
    @Transactional
    public Place createPlace(ManagePlaceDetailDTO newPlaceDTO) {
        Place existingPlaceName = placeRepo.findByName(newPlaceDTO.getName()).orElse(null);
        if (existingPlaceName != null) return null;

        Place newPlace = new Place();

        newPlace.setName(newPlaceDTO.getName());
        newPlace.setDescription(newPlaceDTO.getDescription());
        newPlace.setAddress(newPlaceDTO.getAddress());
        newPlace.setURL(newPlaceDTO.getURL());
        newPlace.setLatitude(newPlaceDTO.getLatitude());
        newPlace.setLongitude(newPlaceDTO.getLongitude());
        newPlace.setActive(newPlaceDTO.isActive());
        newPlace.setAutoFetch(newPlaceDTO.isAutoFetch());
        newPlace.setInterestCategories(newPlaceDTO.getInterestCategories());
        newPlace.updateOpeningHours(newPlaceDTO.getOpeningHours());

        return placeRepo.save(newPlace);
    }

    @Override
    @Transactional
    public Place updatePlace(ManagePlaceDetailDTO updatedPlace) {
        Place existingPlace = placeRepo.findById(updatedPlace.getId()).orElse(null);

        if (existingPlace == null) return null;

        existingPlace.setName(updatedPlace.getName());
        existingPlace.setAddress(updatedPlace.getAddress());
        existingPlace.setDescription(updatedPlace.getDescription());
        existingPlace.setLatitude(updatedPlace.getLatitude());
        existingPlace.setLongitude(updatedPlace.getLongitude());
        existingPlace.setURL(updatedPlace.getURL());
        existingPlace.setActive(updatedPlace.isActive());
        existingPlace.setAutoFetch(updatedPlace.isAutoFetch());
        existingPlace.setInterestCategories(updatedPlace.getInterestCategories());
        existingPlace.updateOpeningHours(updatedPlace.getOpeningHours());

        return placeRepo.save(existingPlace);
    }

    @Override
    @Transactional
    public String uploadPlaceImage(long placeId, MultipartFile image) {
        Place existingPlace = placeRepo.findById(placeId).orElse(null);
        if (existingPlace == null) return null;

        try {
            String imageName = "";
            if (existingPlace.getGoogleId() != null) {
                imageName = existingPlace.getGoogleId();
            } else {
                imageName += existingPlace.getId();
            }
            String imagePath = imageService.upload(image, imageName, 800, 600);

            existingPlace.setImagePath(imagePath);
            placeRepo.save(existingPlace);

            return imagePath;

        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<GoogleSearchDTO> searchPlacesByText(String text) {
        JsonNode result = googlePlaceService.searchPlacesByText(text).block();
        List<GoogleSearchDTO> googleSearchDTOList = new ArrayList<>();
        if (result != null) {
            JsonNode placesNode = result.path("places");
            if (!placesNode.isArray()) return Collections.emptyList();

            for (JsonNode placeNode : placesNode) {
                JsonNode nameNode = placeNode.path("displayName").path("text");
                JsonNode googleIdNode = placeNode.path("id");
                JsonNode formattedAddressNode = placeNode.path("formattedAddress");
                JsonNode googleMapsUriNode = placeNode.path("googleMapsUri");
                if (!nameNode.isNull()
                        && !googleIdNode.isNull()
                        && !formattedAddressNode.isNull()) {

                    // Check if place already in database so we don't show
                    Optional<Place> checkExistingPlace =
                            placeRepo.findByGoogleId(googleIdNode.asText());
                    if (checkExistingPlace.isPresent()) continue;

                    GoogleSearchDTO place =
                            new GoogleSearchDTO(
                                    googleIdNode.asText(),
                                    nameNode.asText(),
                                    formattedAddressNode.asText(),
                                    googleMapsUriNode.asText());
                    googleSearchDTOList.add(place);
                }
            }
        }
        return googleSearchDTOList;
    }

    @Override
    @Transactional
    public Place savePlaceByGoogleId(String googleId) {
        // if this place already exist (by GoogleId) don't add
        Optional<Place> checkExistingPlace = placeRepo.findByGoogleId(googleId);
        if (checkExistingPlace.isPresent()) return null;

        JsonNode placeNode = googlePlaceService.getPlace(googleId).block();
        if (placeNode == null) return null;

        Place place = new Place();
        place.setGoogleId(placeNode.path("id").asText());
        retrievePlaceService.mapGooglePlace(place, placeNode);
        retrievePlaceService.addOpeningHours(place, placeNode);
        retrievePlaceService.checkAndAddInterestCategories(place, placeNode);
        retrievePlaceService.downloadImages(place, placeNode);

        return placeRepo.save(place);
    }
}
