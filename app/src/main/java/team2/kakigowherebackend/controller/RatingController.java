package team2.kakigowherebackend.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team2.kakigowherebackend.dto.ExportRatingDTO;
import team2.kakigowherebackend.dto.RatingItemDTO;
import team2.kakigowherebackend.dto.RatingRequestDTO;
import team2.kakigowherebackend.dto.RatingSummaryDTO;
import team2.kakigowherebackend.model.Rating;
import team2.kakigowherebackend.service.ExportRatingService;
import team2.kakigowherebackend.service.RatingService;

@RestController
@RequestMapping("/api/ratings")
public class RatingController {
    private final RatingService ratingService;
    private final ExportRatingService exportRatingService;

    public RatingController(RatingService ratingService, ExportRatingService exportRatingService) {
        this.ratingService = ratingService;
        this.exportRatingService = exportRatingService;
    }

    @GetMapping("/{placeId}/summary")
    public ResponseEntity<RatingSummaryDTO> getRatingSummary(@PathVariable long placeId) {
        RatingSummaryDTO summary = ratingService.getSummary(placeId);
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/{placeId}/me")
    public ResponseEntity<RatingItemDTO> getMyRating(
            @PathVariable long placeId, @RequestParam Long touristId) {
        RatingItemDTO dto = ratingService.getMyRatingItem(placeId, touristId);
        // handle "no rating yet"
        if (dto == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{placeId}/others")
    public ResponseEntity<List<RatingItemDTO>> getOthers(
            @PathVariable long placeId, @RequestParam Long touristId) {
        List<RatingItemDTO> list = ratingService.getOtherRatings(placeId, touristId);
        return ResponseEntity.ok(list == null? List.of() : list);
    }

    @PostMapping("/{placeId}")
    public ResponseEntity<RatingItemDTO> submitOrUpdateRating(
            @PathVariable long placeId,
            @RequestParam Long touristId,
            @Valid @RequestBody RatingRequestDTO request) {

        RatingItemDTO saved = ratingService.submitOrUpdate(placeId, touristId, request);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/ml/export")
    public ResponseEntity<List<ExportRatingDTO>> exportRatingsCsv() {
        List<Rating> ratings = ratingService.getAllRatings();
        List<ExportRatingDTO> dtos = ratings.stream().map(ExportRatingDTO::new).toList();
        exportRatingService.buildCsv(dtos);
        return ResponseEntity.status(HttpStatus.OK).body(dtos);
    }
}
