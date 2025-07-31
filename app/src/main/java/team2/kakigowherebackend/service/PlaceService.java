package team2.kakigowherebackend.service;

import java.util.List;
import team2.kakigowherebackend.model.Place;
import team2.kakigowherebackend.dto.PlaceDTO;

public interface PlaceService {
    void savePlace(Place place);
    List<Place> getAllPlaces();
    List<PlaceDTO> getAllPlaceDTOs();
}
