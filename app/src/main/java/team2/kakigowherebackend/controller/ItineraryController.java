package team2.kakigowherebackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team2.kakigowherebackend.dto.ItineraryDTO;
import team2.kakigowherebackend.dto.ItineraryDetailDTO;
import team2.kakigowherebackend.service.ItineraryService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/itinerary")
public class ItineraryController {

    private final ItineraryService itineraryService;

    public ItineraryController(ItineraryService itineraryService) {
        this.itineraryService = itineraryService;
    }

    @GetMapping("/{email}")
    public ResponseEntity<List<ItineraryDTO>> getItineraries(@PathVariable String email) {
        List<ItineraryDTO> itineraryDtos = new ArrayList<>();
        itineraryService
                .findTouristItineraries(email)
                .forEach(itinerary -> {
                    itineraryDtos.add(new ItineraryDTO(itinerary));
                });
        return ResponseEntity.ok(itineraryDtos);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<List<ItineraryDetailDTO>> getItineraryDetails(@PathVariable long id) {
        List<ItineraryDetailDTO> detailDtos = new ArrayList<>();
        itineraryService
                .findItineraryDetails(id)
                .forEach(detail -> {
                    detailDtos.add(new ItineraryDetailDTO(detail));
                });
        return ResponseEntity.ok(detailDtos);
    }
}
