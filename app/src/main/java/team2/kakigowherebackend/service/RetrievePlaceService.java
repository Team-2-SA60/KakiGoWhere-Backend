package team2.kakigowherebackend.service;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;
import java.util.Map;
import team2.kakigowherebackend.model.Place;

public interface RetrievePlaceService {
    boolean retrievePlaces();

    String fetchPlacesKml();

    List<Map<String, String>> parseKML(String kmlContent);

    void mapGooglePlace(Place place, JsonNode googlePlace);

    void checkAndAddInterestCategories(Place place, JsonNode googlePlace);
}
