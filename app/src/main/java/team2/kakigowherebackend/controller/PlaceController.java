package team2.kakigowherebackend.controller;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team2.kakigowherebackend.model.Place;
import team2.kakigowherebackend.service.PlaceService;
import team2.kakigowherebackend.service.RetrievePlaceService;
import team2.kakigowherebackend.service.RetrievePlaceServiceImpl;

@RestController
@RequestMapping("/api")
public class PlaceController {

    private final PlaceService placeService;
    private final RetrievePlaceService retrievePlaceService;

    public PlaceController(
            PlaceService placeService, RetrievePlaceServiceImpl retrievePlaceService) {
        this.placeService = placeService;
        this.retrievePlaceService = retrievePlaceService;
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
            filename = "app/src/main/resources/static/default_image.jpg";
        }

        Path imagePath = Paths.get(filename);
        Resource image = new UrlResource(imagePath.toUri());

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).body(image);
    }

    @GetMapping("/retrieveplaces")
    public ResponseEntity<String> retrievePlaces(@RequestParam("run") String run) {
        // PLEASE DON'T RUN THIS RANDOMLY
        // It will call google places API to update every places we have

        if (run == null || !run.equals("team2")) {
            return ResponseEntity.ok("Not ran");
        }

        retrievePlaceService.retrievePlaces();
        return ResponseEntity.ok("Ran retrieved places");
    }
}
