package team2.kakigowherebackend.controller;

import java.net.MalformedURLException;
import java.util.List;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team2.kakigowherebackend.dto.ExportPlaceDTO;
import team2.kakigowherebackend.dto.PlaceDTO;
import team2.kakigowherebackend.dto.PlaceDetailDTO;
import team2.kakigowherebackend.service.ExportPlaceService;
import team2.kakigowherebackend.service.PlaceService;
import team2.kakigowherebackend.service.RetrievePlaceService;

@RestController
@RequestMapping("/api/places")
public class PlaceController {

    private final PlaceService placeService;
    private final RetrievePlaceService retrievePlaceService;
    private final ExportPlaceService exportPlaceService;

    public PlaceController(
            PlaceService placeService,
            RetrievePlaceService retrievePlaceService,
            ExportPlaceService exportPlaceService) {
        this.placeService = placeService;
        this.retrievePlaceService = retrievePlaceService;
        this.exportPlaceService = exportPlaceService;
    }

    @GetMapping
    public ResponseEntity<List<PlaceDTO>> getPlaces() {
        List<PlaceDTO> places = placeService.getPlaces();
        return ResponseEntity.ok(places);
    }

    @GetMapping("/id/{placeId}")
    public ResponseEntity<PlaceDetailDTO> getPlaceById(@PathVariable long placeId) {
        PlaceDetailDTO place = placeService.getPlaceDetail(placeId);

        if (place == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        return ResponseEntity.status(HttpStatus.OK).body(place);
    }

    @GetMapping("/ml/export")
    public ResponseEntity<List<ExportPlaceDTO>> exportPlaces() {
        List<ExportPlaceDTO> places = placeService.getPlacesForMl();

        // Runs service to export all places into CSV for ML
        exportPlaceService.buildCsv(places);

        return ResponseEntity.status(HttpStatus.OK).body(places);
    }

    @GetMapping("/image/{placeId}")
    public ResponseEntity<Resource> getPlaceImage(@PathVariable long placeId) {
        try {
            Resource image = placeService.getImageByPlaceId(placeId);

            if (image == null || !image.exists()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(image);

        } catch (MalformedURLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/retrieve")
    public ResponseEntity<String> retrievePlaces(@RequestBody String run) {
        // PLEASE DON'T RUN THIS RANDOMLY
        // It will call google places API to update every places we have

        if (run == null || !run.equals("team2")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not ran");
        }

        retrievePlaceService.retrievePlaces();
        return ResponseEntity.status(HttpStatus.OK).body("Ran retrieved places");
    }
}
