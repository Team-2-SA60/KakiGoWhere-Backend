package team2.kakigowherebackend.service;

import com.fasterxml.jackson.databind.JsonNode;
import reactor.core.publisher.Mono;

public interface GooglePlaceService {
    Mono<JsonNode> searchPlace(String googleId);

    String downloadPhoto(String photoName, String fileName);
}
