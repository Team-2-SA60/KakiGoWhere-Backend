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
import team2.kakigowherebackend.dto.PlaceEventResponseDTO;
import team2.kakigowherebackend.dto.PlaceNameDTO;
import team2.kakigowherebackend.model.Place;
import team2.kakigowherebackend.service.ExportPlaceService;
import team2.kakigowherebackend.service.PlaceEventService;
import team2.kakigowherebackend.service.PlaceService;
import team2.kakigowherebackend.service.RetrievePlaceService;

@RestController
@RequestMapping("/api/places")
public class PlaceController {

    private final PlaceService placeService;
    private final RetrievePlaceService retrievePlaceService;
    private final ExportPlaceService exportPlaceService;
    private final PlaceEventService placeEventService;

    public PlaceController(
            PlaceService placeService,
            RetrievePlaceService retrievePlaceService,
            ExportPlaceService exportPlaceService,
            PlaceEventService placeEventService) {
        this.placeService = placeService;
        this.retrievePlaceService = retrievePlaceService;
        this.exportPlaceService = exportPlaceService;
        this.placeEventService = placeEventService;
    }

    @GetMapping
    public ResponseEntity<List<PlaceDTO>> getPlaces() {
        List<Place> places = placeService.getPlaces();
        List<PlaceDTO> placeDTOs = places.stream().map(PlaceDTO::new).toList();

        return ResponseEntity.status(HttpStatus.OK).body(placeDTOs);
    }

    @GetMapping("/id/{placeId}")
    public ResponseEntity<PlaceDetailDTO> getPlaceById(@PathVariable long placeId) {
        Place place = placeService.getPlaceDetail(placeId);

        if (place == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        return ResponseEntity.status(HttpStatus.OK).body(new PlaceDetailDTO(place));
    }

    @GetMapping("/ml/export")
    public ResponseEntity<List<ExportPlaceDTO>> exportPlaces() {
        List<Place> places = placeService.getPlaces();
        List<ExportPlaceDTO> exportPlaceDTOS = places.stream().map(ExportPlaceDTO::new).toList();

        // Runs service to export all places into CSV for ML
        exportPlaceService.buildCsv(exportPlaceDTOS);

        return ResponseEntity.status(HttpStatus.OK).body(exportPlaceDTOS);
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

    @GetMapping("/names")
    public ResponseEntity<List<PlaceNameDTO>> getPlaceNames() {
        List<PlaceNameDTO> names = placeService.getPlaceNames();
        return ResponseEntity.status(HttpStatus.OK).body(names);
    }

    @GetMapping("/id/{placeId}/events/active")
    public ResponseEntity<List<PlaceEventResponseDTO>> getActiveEventsForPlace(
            @PathVariable long placeId) {
        List<PlaceEventResponseDTO> events = placeEventService.getActiveEventsForPlace(placeId);

        if (events.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(events);
    }
}
