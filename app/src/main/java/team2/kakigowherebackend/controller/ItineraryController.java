package team2.kakigowherebackend.controller;

import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team2.kakigowherebackend.dto.ItineraryDTO;
import team2.kakigowherebackend.dto.ItineraryDetailDTO;
import team2.kakigowherebackend.model.Itinerary;
import team2.kakigowherebackend.model.ItineraryDetail;
import team2.kakigowherebackend.service.ItineraryService;

@RestController
@RequestMapping("/api/itinerary")
public class ItineraryController {

    private final ItineraryService itineraryService;

    public ItineraryController(ItineraryService itineraryService) {
        this.itineraryService = itineraryService;
    }

    @GetMapping
    public ResponseEntity<List<ItineraryDTO>> getItineraries(
            @RequestHeader("user-email") String email) {
        List<ItineraryDTO> itineraryDtos = new ArrayList<>();
        itineraryService
                .findTouristItineraries(email)
                .forEach(itinerary -> itineraryDtos.add(new ItineraryDTO(itinerary)));
        return ResponseEntity.ok(itineraryDtos);
    }

    @GetMapping("/detail/{itineraryId}")
    public ResponseEntity<List<ItineraryDetailDTO>> getItineraryDetails(
            @PathVariable long itineraryId) {
        List<ItineraryDetailDTO> detailDtos = new ArrayList<>();
        itineraryService
                .findItineraryDetails(itineraryId)
                .forEach(detail -> detailDtos.add(new ItineraryDetailDTO(detail)));
        return ResponseEntity.ok(detailDtos);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createItinerary(
            @RequestHeader("user-email") String email, @Valid @RequestBody Itinerary itinerary) {
        if (email.isEmpty() || itinerary == null) return ResponseEntity.badRequest().build();

        Itinerary createdItinerary = itineraryService.createItinerary(email, itinerary);
        if (createdItinerary == null) return ResponseEntity.notFound().build();
        else return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{itineraryId}")
    public ResponseEntity<?> deleteItinerary(@PathVariable Long itineraryId) {
        if (itineraryService.deleteItinerary(itineraryId)) return ResponseEntity.ok().build();
        else return ResponseEntity.notFound().build();
    }

    @PutMapping("/detail/add/day/{itineraryId}")
    public ResponseEntity<?> addItineraryDay(
            @PathVariable Long itineraryId, @Valid @RequestBody ItineraryDetail itineraryDetail) {
        if (itineraryDetail == null) return ResponseEntity.badRequest().build();

        Itinerary updatedItinerary = itineraryService.addItineraryDay(itineraryId, itineraryDetail);
        if (updatedItinerary == null) return ResponseEntity.notFound().build();
        else return ResponseEntity.ok().build();
    }

    @DeleteMapping("/detail/delete/day/{itineraryId}")
    public ResponseEntity<?> deleteItineraryDay(
            @PathVariable Long itineraryId, @RequestParam String lastDate) {
        if (itineraryService.deleteItineraryDay(itineraryId, lastDate))
            return ResponseEntity.ok().build();
        else return ResponseEntity.notFound().build();
    }

    @PutMapping("/detail/add/{itineraryId}")
    public ResponseEntity<?> addItineraryItem(
            @PathVariable Long itineraryId,
            @RequestParam Long placeId,
            @Valid @RequestBody ItineraryDetail itineraryDetail) {
        if (itineraryDetail == null) return ResponseEntity.badRequest().build();

        Itinerary updatedItinerary =
                itineraryService.addItineraryDetail(itineraryId, itineraryDetail, placeId);
        if (updatedItinerary == null) return ResponseEntity.notFound().build();
        else return ResponseEntity.ok().build();
    }

    @PutMapping("/detail/edit/{detailId}")
    public ResponseEntity<?> editItineraryItem(
            @PathVariable Long detailId, @Valid @RequestBody ItineraryDetail itineraryDetail) {
        if (itineraryDetail == null) return ResponseEntity.badRequest().build();

        ItineraryDetail updatedDetail =
                itineraryService.editItineraryDetail(detailId, itineraryDetail);
        if (updatedDetail == null) return ResponseEntity.notFound().build();
        else return ResponseEntity.ok().build();
    }

    @DeleteMapping("/detail/delete/{detailId}")
    public ResponseEntity<?> deleteItineraryItem(@PathVariable Long detailId) {
        if (itineraryService.deleteItineraryDetail(detailId)) return ResponseEntity.ok().build();
        else return ResponseEntity.badRequest().build();
    }
}
