package team2.kakigowherebackend.service;

import org.springframework.stereotype.Service;
import team2.kakigowherebackend.model.User;
import team2.kakigowherebackend.repository.AdminRepository;
import team2.kakigowherebackend.repository.TouristRepository;
import team2.kakigowherebackend.repository.UserRepository;
import team2.kakigowherebackend.utils.PasswordEncoderUtil;
import team2.kakigowherebackend.utils.UserConstants;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepo;
    private final TouristRepository touristRepo;
    private final AdminRepository adminRepo;

    public AuthServiceImpl(
            UserRepository userRepo, TouristRepository touristRepo, AdminRepository adminRepo) {
        this.userRepo = userRepo;
        this.touristRepo = touristRepo;
        this.adminRepo = adminRepo;
    }

    @Override
    public User findUserByRoleAndId(String role, long userId) {
        if (role.equals(UserConstants.ADMIN)) return adminRepo.findById(userId).orElse(null);
        if (role.equals(UserConstants.TOURIST)) return touristRepo.findById(userId).orElse(null);
        return null;
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepo.findByEmail(email).orElse(null);
    }

    @Override
    public boolean authenticate(String password, String storedEncodedPassword) {
        return PasswordEncoderUtil.matches(password, storedEncodedPassword);
    }
}
