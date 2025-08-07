package team2.kakigowherebackend.service;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import team2.kakigowherebackend.model.Place;

public interface ManagePlaceService {
    Page<Place> getPlaces(int page, int pageSize, String keyword);

    Place updatePlace(Place place);

    String uploadPlaceImage(long placeId, MultipartFile image);
}
