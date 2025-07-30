package team2.kakigowherebackend.service;

import java.util.List;
import java.util.Map;
import team2.kakigowherebackend.model.Place;

public interface RetrievePlaceService {
    boolean updatePlaces();

    String fetchPlacesKml();

    List<Map<String, String>> parseKML(String kmlContent);

    Place fetchGooglePlace(Map<String, String> place);
}
