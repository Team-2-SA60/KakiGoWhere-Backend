package team2.kakigowherebackend.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team2.kakigowherebackend.model.InterestCategory;
import team2.kakigowherebackend.service.InterestService;

@RestController
@RequestMapping("/api/interests")
public class InterestController {
    private final InterestService interestService;

    public InterestController(InterestService interestService) {
        this.interestService = interestService;
    }

    @GetMapping
    public ResponseEntity<List<InterestCategory>> getAllInterestCategories() {
        List<InterestCategory> interestCategories = interestService.getAllInterestCategories();
        return ResponseEntity.status(HttpStatus.OK).body(interestCategories);
    }
}
