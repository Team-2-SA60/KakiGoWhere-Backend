package team2.kakigowherebackend.controller;

import java.net.MalformedURLException;
import java.util.List;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team2.kakigowherebackend.dto.PlaceDTO;
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
    public ResponseEntity<List<PlaceDTO>> getPlaces() {
        List<PlaceDTO> places = placeService.getAllPlaces();
        return ResponseEntity.ok(places);
    }

    @GetMapping("/places/image/{placeId}")
    public ResponseEntity<Resource> getPlaceImage(@PathVariable long placeId) {

        try {
            Resource image = placeService.getImageByPlaceId(placeId);

            if (image == null || !image.exists()) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);

        } catch (MalformedURLException e) {
            return ResponseEntity.noContent().build();
        }
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
