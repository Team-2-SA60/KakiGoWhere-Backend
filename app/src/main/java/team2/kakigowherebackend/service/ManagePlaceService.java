package team2.kakigowherebackend.service;

import java.util.List;
import team2.kakigowherebackend.model.Place;

public interface ManagePlaceService {
    List<Place> getPlaces(int page, int pageSize, String keyword);
}
