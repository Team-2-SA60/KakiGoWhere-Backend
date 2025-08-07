package team2.kakigowherebackend.controller;

import java.time.LocalDate;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

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
            HttpSession session
    ) {
        if (touristId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        visitService.recordVisit(touristId, placeId, LocalDate.now());

        return ResponseEntity.ok().build();
    }
}