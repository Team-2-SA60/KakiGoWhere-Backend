package team2.kakigowherebackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team2.kakigowherebackend.dto.RegisterRequestDTO;
import team2.kakigowherebackend.dto.RegisterResponseDTO;
import team2.kakigowherebackend.dto.TouristUpdateRequestDTO;
import team2.kakigowherebackend.model.Tourist;
import team2.kakigowherebackend.service.StatService;
import team2.kakigowherebackend.service.TouristServiceImpl;

@RestController
@RequestMapping("/api/tourist")
public class TouristController {

    private final StatService statService;
    private final TouristServiceImpl touristService;

    public TouristController(StatService statService, TouristServiceImpl touristService) {
        this.statService = statService;
        this.touristService = touristService;
    }

    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmailExists(@RequestParam String email) {
        boolean exists = touristService.checkEmailExists(email);
        return ResponseEntity.ok(exists);
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(@RequestBody RegisterRequestDTO request) {

        // create tourist
        Tourist tourist = touristService.registerTourist(request);

        // increment daily sign-up count
        statService.addSignUp();

        // create DTO response
        RegisterResponseDTO response = new RegisterResponseDTO(tourist);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tourist> updateTourist(
            @PathVariable Long id, @RequestBody TouristUpdateRequestDTO request) {
        // Delegate update logic to service
        Tourist updated = touristService.updateTourist(id, request);
        return ResponseEntity.ok(updated);
    }
}
