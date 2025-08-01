package team2.kakigowherebackend.service;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team2.kakigowherebackend.dto.ExportPlaceDTO;
import team2.kakigowherebackend.dto.PlaceDTO;
import team2.kakigowherebackend.dto.PlaceDetailDTO;
import team2.kakigowherebackend.model.Place;
import team2.kakigowherebackend.repository.PlaceRepository;

@Service
@Transactional
public class PlaceServiceImpl implements PlaceService {

    // Location of downloaded/uploaded images
    @Value("${upload.dir}")
    private String uploadDir;

    private final PlaceRepository placeRepo;

    public PlaceServiceImpl(PlaceRepository placeRepo) {
        this.placeRepo = placeRepo;
    }

    @Override
    public List<PlaceDTO> getPlaces() {
        List<PlaceDTO> placesDTO = new ArrayList<>();
        placeRepo.findAll().forEach(place -> placesDTO.add(new PlaceDTO(place)));
        return placesDTO;
    }

    @Override
    public List<ExportPlaceDTO> getPlacesForMl() {
        List<ExportPlaceDTO> places = new ArrayList<>();
        placeRepo
                .findAll()
                .forEach(
                        p -> {
                            places.add(new ExportPlaceDTO(p));
                        });
        return places;
    }

    @Override
    public PlaceDetailDTO getPlaceDetail(long placeId) {
        Optional<Place> place = placeRepo.findById(placeId);
        return place.map(PlaceDetailDTO::new).orElse(null);
    }

    @Override
    public Resource getImageByPlaceId(long placeId) throws MalformedURLException {
        Place place = placeRepo.findById(placeId).orElse(null);

        if (place == null || place.getImagePath() == null) return null;

        Path imageDir = Paths.get(uploadDir);
        Path imagePath = imageDir.resolve(place.getGoogleId() + ".jpg").normalize();

        Resource resource = new UrlResource(imagePath.toUri());
        if (resource.exists() && resource.isReadable()) {
            return resource;
        } else {
            return null;
        }
    }
}
