package team2.kakigowherebackend.service;

import java.util.List;
import team2.kakigowherebackend.model.Place;

public interface PlaceService {
    void savePlace(Place place);

    List<Place> getAllPlaces();
}
