package team2.kakigowherebackend.service;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team2.kakigowherebackend.dto.PlaceNameDTO;
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
    public List<Place> getPlaces() {
        return placeRepo.findAllByActive(true);
    }

    @Override
    public Place getPlaceDetail(long placeId) {
        return placeRepo.findById(placeId).orElse(null);
    }

    @Override
    public Resource getImageByPlaceId(long placeId) throws MalformedURLException {
        Place place = placeRepo.findById(placeId).orElse(null);

        if (place == null || place.getImagePath() == null) return null;

        String imageName = "";
        if (place.getGoogleId() != null) {
            imageName = place.getGoogleId();
        } else {
            imageName += place.getId();
        }
        Path imageDir = Paths.get(uploadDir);
        Path imagePath = imageDir.resolve(imageName + ".jpg").normalize();
        Resource resource = new UrlResource(imagePath.toUri());

        if (!resource.exists() || !resource.isReadable()) {
            return new ClassPathResource("static/default_image.jpg");
        }

        return resource;
    }

    @Override
    public List<PlaceNameDTO> getPlaceNames() {
        return placeRepo.findAllNames();
    }
}
