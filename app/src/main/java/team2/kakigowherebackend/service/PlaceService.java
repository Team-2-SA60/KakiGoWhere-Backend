package team2.kakigowherebackend.service;

import org.springframework.core.io.Resource;
import team2.kakigowherebackend.model.Place;

import java.net.MalformedURLException;
import java.util.List;

public interface PlaceService {

    List<Place> getPlaces();

    Place getPlaceDetail(long placeId);

    Resource getImageByPlaceId(long placeId) throws MalformedURLException;
}
