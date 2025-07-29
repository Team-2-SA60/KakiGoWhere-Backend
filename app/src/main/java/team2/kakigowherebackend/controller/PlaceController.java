package team2.kakigowherebackend.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
}
