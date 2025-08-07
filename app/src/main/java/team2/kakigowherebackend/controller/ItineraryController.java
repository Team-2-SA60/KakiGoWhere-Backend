package team2.kakigowherebackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team2.kakigowherebackend.dto.ItineraryDTO;
import team2.kakigowherebackend.dto.ItineraryDetailDTO;
import team2.kakigowherebackend.model.Itinerary;
import team2.kakigowherebackend.model.ItineraryDetail;
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

    @PutMapping("/detail/add/{itineraryId}")
    public ResponseEntity<?> addItineraryItem(
            @PathVariable Long itineraryId,
            @RequestParam Long placeId,
            @RequestBody ItineraryDetail itineraryDetail
    ) {
        if (itineraryDetail == null) {
            return ResponseEntity.badRequest().build();
        } else {
            itineraryService.addItineraryDetail(itineraryId, itineraryDetail, placeId);
            return ResponseEntity.ok().build();
        }
    }

    @PutMapping("/detail/add/day/{itineraryId}")
    public ResponseEntity<?> addItineraryDay(
            @PathVariable Long itineraryId,
            @RequestBody ItineraryDetail itineraryDetail
    ) {
        if (itineraryDetail == null) {
            return ResponseEntity.badRequest().build();
        } else {
            itineraryService.addItineraryDay(itineraryId, itineraryDetail);
            return ResponseEntity.ok().build();
        }
    }

    @PutMapping("detail/edit/{detailId}")
    public ResponseEntity<?> editItineraryItem(
            @PathVariable Long detailId,
            @RequestBody ItineraryDetail itineraryDetail
    ) {
        if (itineraryDetail == null) {
            return ResponseEntity.badRequest().build();
        }
        else {
            itineraryService.editItineraryDetail(detailId, itineraryDetail);
            return ResponseEntity.ok().build();
        }
    }

    @DeleteMapping("detail/delete/{detailId}")
    public ResponseEntity<?> deleteItineraryItem(
            @PathVariable Long detailId
    ) {
        if (itineraryService.deleteItineraryDetail(detailId)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createItinerary(
            @RequestHeader("user-email") String email,
            @RequestBody Itinerary itinerary
    ) {
        if (email.isEmpty() || itinerary == null) {
            return ResponseEntity.badRequest().build();
        } else {
            itineraryService.createTouristItinerary(email, itinerary);
            return ResponseEntity.ok().build();
        }
    }
}
