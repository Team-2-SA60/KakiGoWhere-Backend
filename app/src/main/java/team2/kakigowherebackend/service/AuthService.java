package team2.kakigowherebackend.service;

import team2.kakigowherebackend.model.Tourist;

public interface AuthService {
    Tourist findTouristByEmail(String email);

    boolean authenticate(String password, String storedEncodedPassword);
}
