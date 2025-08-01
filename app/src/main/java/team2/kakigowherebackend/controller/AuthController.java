package team2.kakigowherebackend.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team2.kakigowherebackend.dto.LoginDTO;
import team2.kakigowherebackend.model.Tourist;
import team2.kakigowherebackend.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(
            @Valid @RequestBody LoginDTO loginDTO, HttpSession session) {

        String email = loginDTO.getEmail();
        Tourist tourist = authService.findTouristByEmail(email);

        if (tourist == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid email");

        String password = loginDTO.getPassword();
        boolean isPasswordValid = authService.authenticate(password, tourist.getPassword());

        if (!isPasswordValid)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");

        session.setAttribute("tourist", tourist.getId());

        return ResponseEntity.status(HttpStatus.OK).body(String.valueOf(tourist.getId()));
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.status(HttpStatus.OK).body("Logout successful");
    }
}
