package team2.kakigowherebackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
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

import java.util.List;

@RestController
@RequestMapping("/api/tourist")
public class TouristController {

    @Autowired
    private TouristRepository touristRepository;

    @Autowired
    private InterestCategoryRepository interestCategoryRepository;

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

        // create DTO response
        RegisterResponseDTO response = new RegisterResponseDTO(
                tourist.getId(),
                tourist.getName(),
                tourist.getEmail(),
                tourist.getInterestCategories()
        );

        return ResponseEntity.ok(response);
    }
}
