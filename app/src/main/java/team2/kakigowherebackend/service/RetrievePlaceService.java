package team2.kakigowherebackend.service;

import com.fasterxml.jackson.databind.JsonNode;
import java.net.URI;
import java.util.List;
import java.util.Map;
import reactor.core.publisher.Mono;

public interface RetrievePlaceService {
    boolean updatePlaces();

    String fetchPlacesKml();

    List<Map<String, String>> parseKML(String kmlContent);

    Mono<JsonNode> fetchGooglePlace(String placeTitle);

    String downloadImage(URI imageUri);
}
