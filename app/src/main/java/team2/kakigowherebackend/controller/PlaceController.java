package team2.kakigowherebackend.controller;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team2.kakigowherebackend.model.Place;
import team2.kakigowherebackend.service.PlaceService;

@RestController
@RequestMapping("/api")
public class PlaceController {

    private final PlaceService placeService;

    public PlaceController(PlaceService placeService) {
        this.placeService = placeService;
    }

    @GetMapping("/places")
    public ResponseEntity<List<Place>> getPlaces() {
        List<Place> places = placeService.getAllPlaces();
        return ResponseEntity.ok(places);
    }

    @GetMapping("/places/image/{filename}")
    public ResponseEntity<Resource> getPlaceImage(@PathVariable String filename)
            throws IOException {
        if (filename == null || filename.isEmpty()) {
            filename = "default_image.jpg";
        }

        Path imagePath = Paths.get("app/src/main/resources/static/" + filename);
        Resource image = new UrlResource(imagePath.toUri());

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).body(image);
    }
}
