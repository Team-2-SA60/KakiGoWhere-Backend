package team2.kakigowherebackend.controller;

import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Map;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team2.kakigowherebackend.service.PlaceVisitService;
import team2.kakigowherebackend.utils.UserConstants;

@RestController
@RequestMapping("/api/places")
public class PlaceVisitController {
    private final PlaceVisitService visitService;

    public PlaceVisitController(PlaceVisitService visitService) {
        this.visitService = visitService;
    }

    @PostMapping("/{placeId}/visits")
    public ResponseEntity<Void> recordVisit(
            @PathVariable long placeId,
            @SessionAttribute(name = UserConstants.TOURIST, required = false) Long touristId,
            HttpSession session) {
        if (touristId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        visitService.recordVisit(touristId, placeId, LocalDate.now());

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{placeId}/visits")
    public ResponseEntity<Map<LocalDate, Integer>> getVisitsByMonth(
            @PathVariable long placeId,
            @RequestParam("month") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) YearMonth month) {
        Map<LocalDate, Integer> counts = visitService.getDailyVisitCounts(placeId, month);
        return ResponseEntity.ok(counts);
    }
}
