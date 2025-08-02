package team2.kakigowherebackend.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team2.kakigowherebackend.dto.LoginDTO;
import team2.kakigowherebackend.dto.UserDTO;
import team2.kakigowherebackend.model.Admin;
import team2.kakigowherebackend.model.Tourist;
import team2.kakigowherebackend.model.User;
import team2.kakigowherebackend.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> touristLogin(
            @Valid @RequestBody LoginDTO loginDTO, HttpSession session) {

        String email = loginDTO.getEmail();
        User user = authService.findUserByEmail(email);

        if (!(user instanceof Tourist))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid email");

        String password = loginDTO.getPassword();
        return handleLogin(user, password, "tourist", session);
    }

    @PostMapping("/admin/login")
    public ResponseEntity<?> adminLogin(
            @Valid @RequestBody LoginDTO loginDTO, HttpSession session) {

        String email = loginDTO.getEmail();
        User user = authService.findUserByEmail(email);

        if (!(user instanceof Admin))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid email");

        String password = loginDTO.getPassword();
        return handleLogin(user, password, "admin", session);
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.status(HttpStatus.OK).body("Logout successful");
    }

    private ResponseEntity<?> handleLogin(
            User user, String rawPassword, String sessionKey, HttpSession session) {
        if (!authService.authenticate(rawPassword, user.getPassword()))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
        session.setAttribute(sessionKey, user.getId());
        return ResponseEntity.status(HttpStatus.OK).body(new UserDTO(user, sessionKey));
    }
}
