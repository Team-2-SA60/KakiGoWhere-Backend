package team2.kakigowherebackend.service;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import team2.kakigowherebackend.dto.ManagePlaceDetailDTO;
import team2.kakigowherebackend.model.Place;
import team2.kakigowherebackend.repository.PlaceRepository;

@Service
public class ManagePlaceServiceImpl implements ManagePlaceService {

    private final PlaceRepository placeRepo;
    private final ImageService imageService;

    public ManagePlaceServiceImpl(PlaceRepository placeRepo, ImageService imageService) {
        this.placeRepo = placeRepo;
        this.imageService = imageService;
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
        Place newPlace = new Place();

        newPlace.setName(newPlaceDTO.getName());
        newPlace.setDescription(newPlaceDTO.getDescription());
        newPlace.setAddress(newPlaceDTO.getAddress());
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
            String imagePath = imageService.upload(image, imageName);

            existingPlace.setImagePath(imagePath);
            placeRepo.save(existingPlace);

            return imagePath;

        } catch (Exception e) {
            return null;
        }
    }
}
