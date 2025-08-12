package team2.kakigowherebackend.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import team2.kakigowherebackend.dto.GoogleSearchDTO;
import team2.kakigowherebackend.dto.ManagePlaceDetailDTO;
import team2.kakigowherebackend.model.Place;

public interface ManagePlaceService {
    Page<Place> getPlaces(int page, int pageSize, String keyword);

    Place createPlace(ManagePlaceDetailDTO newPlace);

    Place updatePlace(ManagePlaceDetailDTO updatedPlace);

    String uploadPlaceImage(long placeId, MultipartFile image);

    List<GoogleSearchDTO> searchPlacesByText(String text);

    Place savePlaceByGoogleId(String googleId);
}
