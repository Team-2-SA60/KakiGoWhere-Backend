package team2.kakigowherebackend.service;

import java.net.MalformedURLException;
import java.util.List;
import org.springframework.core.io.Resource;
import team2.kakigowherebackend.dto.PlaceNameDTO;
import team2.kakigowherebackend.model.Place;

public interface PlaceService {

    List<Place> getPlaces();

    Place getPlaceDetail(long placeId);

    Resource getImageByPlaceId(long placeId) throws MalformedURLException;

    List<PlaceNameDTO> getPlaceNames();
}
