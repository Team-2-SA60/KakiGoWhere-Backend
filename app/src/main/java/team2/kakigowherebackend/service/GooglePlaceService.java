package team2.kakigowherebackend.service;

import com.fasterxml.jackson.databind.JsonNode;
import reactor.core.publisher.Mono;

public interface GooglePlaceService {

    Mono<JsonNode> searchPlacesByText(String text);

    Mono<JsonNode> getPlace(String googleId);

    String downloadPhoto(String photoName, String fileName);
}
