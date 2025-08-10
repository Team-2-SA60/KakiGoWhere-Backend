package team2.kakigowherebackend.controller;

import java.time.LocalDate;
import java.util.Optional;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team2.kakigowherebackend.model.DailyStats;
import team2.kakigowherebackend.service.StatService;

@RestController
@RequestMapping("/api/stats")
public class StatController {

    private final StatService statService;

    public StatController(StatService statService) {
        this.statService = statService;
    }

    // parse input as YYYY-MM-DD
    @GetMapping
    public ResponseEntity<DailyStats> getStatsByDate(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        Optional<DailyStats> statsOpt = statService.getByDate(date);

        if (statsOpt.isPresent()) {
            DailyStats stats = statsOpt.get();
            return ResponseEntity.ok().body(stats);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
