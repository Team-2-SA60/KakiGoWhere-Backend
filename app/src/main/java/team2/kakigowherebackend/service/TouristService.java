package team2.kakigowherebackend.service;

import team2.kakigowherebackend.dto.RegisterRequestDTO;
import team2.kakigowherebackend.dto.TouristUpdateRequestDTO;
import team2.kakigowherebackend.model.Tourist;

public interface TouristService {
    boolean checkEmailExists(String email);

    Tourist registerTourist(RegisterRequestDTO request);

    Tourist updateTourist(Long id, TouristUpdateRequestDTO dto);
}
