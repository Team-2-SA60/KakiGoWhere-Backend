package team2.kakigowherebackend.service;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team2.kakigowherebackend.dto.PlaceDTO;
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
    public void savePlace(Place place) {
        placeRepo.save(place);
    }

    @Override
    public List<PlaceDTO> getAllPlaces() {
        List<PlaceDTO> placesDTO = new ArrayList<>();
        placeRepo.findAll().forEach(place -> placesDTO.add(new PlaceDTO(place)));
        return placesDTO;
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
