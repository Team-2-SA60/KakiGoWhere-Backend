package team2.kakigowherebackend.service;

import team2.kakigowherebackend.model.User;

public interface AuthService {
    User findUserByEmail(String email);

    boolean authenticate(String password, String storedEncodedPassword);
}
