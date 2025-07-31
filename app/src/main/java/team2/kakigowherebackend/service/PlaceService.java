package team2.kakigowherebackend.service;

import java.net.MalformedURLException;
import java.util.List;
import org.springframework.core.io.Resource;
import team2.kakigowherebackend.dto.PlaceDTO;
import team2.kakigowherebackend.model.Place;

public interface PlaceService {
    void savePlace(Place place);

    List<PlaceDTO> getAllPlaces();

    Resource getImageByPlaceId(long placeId) throws MalformedURLException;
}
