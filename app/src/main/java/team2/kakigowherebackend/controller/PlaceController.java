package team2.kakigowherebackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team2.kakigowherebackend.dto.PlaceDTO;
import team2.kakigowherebackend.model.Place;
import team2.kakigowherebackend.service.PlaceService;
import team2.kakigowherebackend.service.converter.PlaceDTOConverter;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class PlaceController {

    private final PlaceService placeService;
    private final PlaceDTOConverter dtoConverter;

    public PlaceController(PlaceService placeService, PlaceDTOConverter dtoConverter) {
        this.placeService = placeService;
        this.dtoConverter = dtoConverter;
    }

    @GetMapping("/places")
    public ResponseEntity<List<PlaceDTO>> getPlaces() {
        List<Place> places = placeService.getAllPlaces();
        List<PlaceDTO> placeDtos = new ArrayList<>();

        for (Place place : places) {
            try {
                placeDtos.add(dtoConverter.convertToDto(place));
            } catch (Exception e){
                // add error message if error
                System.out.println(e.getMessage());
            }
        }

        return ResponseEntity.ok(placeDtos);
    }

}
