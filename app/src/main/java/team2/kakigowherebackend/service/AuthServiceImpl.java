package team2.kakigowherebackend.service;

import java.util.Optional;
import org.springframework.stereotype.Service;
import team2.kakigowherebackend.model.Tourist;
import team2.kakigowherebackend.repository.TouristRepository;
import team2.kakigowherebackend.utils.PasswordEncoderUtil;

@Service
public class AuthServiceImpl implements AuthService {

    private final TouristRepository touristRepo;

    public AuthServiceImpl(TouristRepository touristRepo) {
        this.touristRepo = touristRepo;
    }

    @Override
    public Tourist findTouristByEmail(String email) {
        Optional<Tourist> tourist = touristRepo.findByEmail(email);
        return tourist.orElse(null);
    }

    @Override
    public boolean authenticate(String password, String storedEncodedPassword) {
        return PasswordEncoderUtil.matches(password, storedEncodedPassword);
    }
}
