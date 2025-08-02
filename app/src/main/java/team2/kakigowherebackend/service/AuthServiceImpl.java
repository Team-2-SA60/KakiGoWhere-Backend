package team2.kakigowherebackend.service;

import java.util.Optional;
import org.springframework.stereotype.Service;
import team2.kakigowherebackend.model.User;
import team2.kakigowherebackend.repository.UserRepository;
import team2.kakigowherebackend.utils.PasswordEncoderUtil;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepo;

    public AuthServiceImpl(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public User findUserByEmail(String email) {
        Optional<User> user = userRepo.findByEmail(email);
        return user.orElse(null);
    }

    @Override
    public boolean authenticate(String password, String storedEncodedPassword) {
        return PasswordEncoderUtil.matches(password, storedEncodedPassword);
    }
}
