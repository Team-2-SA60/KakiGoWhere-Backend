package team2.kakigowherebackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team2.kakigowherebackend.dto.RegisterRequestDTO;
import team2.kakigowherebackend.dto.RegisterResponseDTO;
import team2.kakigowherebackend.model.InterestCategory;
import team2.kakigowherebackend.model.Tourist;
import team2.kakigowherebackend.repository.InterestCategoryRepository;
import team2.kakigowherebackend.repository.TouristRepository;
import team2.kakigowherebackend.service.StatService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import team2.kakigowherebackend.dto.TouristUpdateRequestDTO;
import team2.kakigowherebackend.service.TouristService;

import java.util.List;

@RestController
@RequestMapping("/api/tourist")
public class TouristController {

    private final TouristRepository touristRepository;
    private final InterestCategoryRepository interestCategoryRepository;
    private final StatService statService;
    private final TouristService touristService;

    public TouristController(
            TouristRepository touristRepository,
            InterestCategoryRepository interestCategoryRepository,
            StatService statService, TouristService touristService
    ){
        this.touristRepository = touristRepository;
        this.interestCategoryRepository = interestCategoryRepository;
        this.statService = statService;
        this.touristService = touristService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(@RequestBody RegisterRequestDTO request) {

        //create entity
        Tourist tourist = new Tourist(
                request.getEmail(),
                request.getPassword(),
                request.getName()
        );
        List<InterestCategory> interests = interestCategoryRepository.findAllById(request.getInterestCategoryIds());
        tourist.setInterestCategories(interests);
        touristRepository.save(tourist);

        // increment daily sign-up count
        statService.addSignUp();

        // create DTO response
        RegisterResponseDTO response = new RegisterResponseDTO(
                tourist.getId(),
                tourist.getName(),
                tourist.getEmail(),
                tourist.getInterestCategories()
        );

        return ResponseEntity.ok(response);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Tourist> updateTourist(
            @PathVariable Long id,
            @RequestBody TouristUpdateRequestDTO request
    ) {
        // Delegate update logic to service
        Tourist updated = touristService.updateTourist(id, request);
        return ResponseEntity.ok(updated);
    }
}
