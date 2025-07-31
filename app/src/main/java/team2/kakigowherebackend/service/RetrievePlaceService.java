package team2.kakigowherebackend.service;

import com.fasterxml.jackson.databind.JsonNode;
import team2.kakigowherebackend.model.Place;

public interface RetrievePlaceService {
    void retrievePlaces();

    void mapGooglePlace(Place place, JsonNode placeNode);

    void addOpeningHours(Place place, JsonNode placeNode);

    void checkAndAddInterestCategories(Place place, JsonNode placeNode);

    void checkAndAddRatings(Place place, JsonNode placeNode);

    void downloadImages(Place place, JsonNode placeNode);
}
