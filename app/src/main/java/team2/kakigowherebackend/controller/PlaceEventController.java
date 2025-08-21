package team2.kakigowherebackend.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team2.kakigowherebackend.dto.PlaceEventRequestDTO;
import team2.kakigowherebackend.dto.PlaceEventResponseDTO;
import team2.kakigowherebackend.service.PlaceEventService;

@RestController
@RequestMapping("/api/admin/event")
public class PlaceEventController {
    private final PlaceEventService placeEventService;

    public PlaceEventController(PlaceEventService placeEventService) {
        this.placeEventService = placeEventService;
    }

    @PostMapping("/create")
    public ResponseEntity<PlaceEventResponseDTO> createEvent(
            @Valid @RequestBody PlaceEventRequestDTO request) {
        PlaceEventResponseDTO created = placeEventService.createEvent(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<PlaceEventResponseDTO> updateEvent(
            @PathVariable Long id, @Valid @RequestBody PlaceEventRequestDTO request) {
        PlaceEventResponseDTO updated = placeEventService.updateEvent(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<PlaceEventResponseDTO> getEventById(@PathVariable Long id) {
        PlaceEventResponseDTO dto = placeEventService.getEventById(id);
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @GetMapping("/place/{placeId}")
    public ResponseEntity<List<PlaceEventResponseDTO>> getEventsByPlace(
            @PathVariable Long placeId) {
        List<PlaceEventResponseDTO> list = placeEventService.getEventsByPlaceId(placeId);
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<PlaceEventResponseDTO>> searchEvents(
            @RequestParam(required = false) String keyword, Pageable pageable) {
        Page<PlaceEventResponseDTO> page = placeEventService.searchEvents(keyword, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(page);
    }
}
