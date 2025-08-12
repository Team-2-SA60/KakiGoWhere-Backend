package team2.kakigowherebackend.service;

import com.fasterxml.jackson.databind.JsonNode;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class GooglePlaceServiceImpl implements GooglePlaceService {
    private final WebClient webClient;
    private final ImageService iService;

    // Before using this service, make sure you put your Google Place API key in
    // "application.properties"
    public GooglePlaceServiceImpl(
            @Value("${google.places.api.key}") String apiKey, ImageService iService) {
        this.iService = iService;
        this.webClient =
                WebClient.builder()
                        .defaultHeader("X-Goog-Api-Key", apiKey)
                        .codecs(
                                configurer ->
                                        configurer
                                                .defaultCodecs()
                                                .maxInMemorySize(10 * 1024 * 1024))
                        .build();
    }

    @Override
    public Mono<JsonNode> searchPlacesByText(String text) {

        // Specify what fields to retrieve from request
        String fieldMask =
                "places.id,places.displayName,places.formattedAddress,places.googleMapsUri";

        return webClient
                .post()
                .uri("https://places.googleapis.com/v1/places:searchText")
                .header("Content-Type", "application/json")
                .header("X-Goog-FieldMask", fieldMask)
                .bodyValue(Map.of("textQuery", text))
                .retrieve()
                .bodyToMono(JsonNode.class);
    }

    // Fetch GET request to Google Places API (new) and retrieve a Place Detail by googleId
    // https://developers.google.com/maps/documentation/places/web-service/place-details
    @Override
    public Mono<JsonNode> getPlace(String googleId) {

        // Specify what fields to retrieve from request
        String fieldMask =
                "id,displayName,websiteUri,types,photos,location,regularOpeningHours,businessStatus,editorialSummary,reviews,formattedAddress";

        return webClient
                .get()
                .uri("https://places.googleapis.com/v1/places/" + googleId)
                .header("X-Goog-FieldMask", fieldMask)
                .retrieve()
                .bodyToMono(JsonNode.class);
    }

    // Fetch GET request to Google Places API (new) and retrieve imageURI for a Place Photo by
    // photoName
    // https://developers.google.com/maps/documentation/places/web-service/place-photos
    // Then call ImageService to download the retrieved imageURI
    // Get back and returns the downloaded image path
    @Override
    public String downloadPhoto(String photoName, String fileName) {
        URI imageUri =
                webClient
                        .get()
                        .uri(
                                "https://places.googleapis.com/v1/"
                                        + photoName
                                        + "/media?maxHeightPx=600")
                        .retrieve()
                        .bodyToMono(JsonNode.class)
                        .map(
                                response -> {
                                    URI uri = null;
                                    try {
                                        uri = new URI(response.path("photoUri").asText());
                                    } catch (URISyntaxException e) {
                                        throw new RuntimeException(e);
                                    }
                                    return uri;
                                })
                        .block();

        if (imageUri == null) return null;

        // Calls ImageService method to download image and returns downloaded image path
        return iService.download(imageUri, fileName);
    }
}
