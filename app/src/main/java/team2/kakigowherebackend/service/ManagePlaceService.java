package team2.kakigowherebackend.service;

import org.springframework.data.domain.Page;
import team2.kakigowherebackend.model.Place;

public interface ManagePlaceService {
    Page<Place> getPlaces(int page, int pageSize, String keyword);
}
