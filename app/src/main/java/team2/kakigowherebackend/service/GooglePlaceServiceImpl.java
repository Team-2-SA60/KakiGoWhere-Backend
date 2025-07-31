package team2.kakigowherebackend.service;

import com.fasterxml.jackson.databind.JsonNode;
import java.net.URI;
import java.net.URISyntaxException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class GooglePlaceServiceImpl implements GooglePlaceService {
    private final WebClient webClient;
    private final ImageService iService;

    public GooglePlaceServiceImpl(
            @Value("${google.places.api.key}") String apiKey, ImageServiceImpl iService) {
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
    public Mono<JsonNode> searchPlace(String googleId) {

        String fieldMask =
                "id,displayName,websiteUri,types,photos,location,regularOpeningHours,businessStatus,editorialSummary,reviews";

        return webClient
                .get()
                .uri("https://places.googleapis.com/v1/places/" + googleId)
                .header("X-Goog-FieldMask", fieldMask)
                .retrieve()
                .bodyToMono(JsonNode.class);
    }

    @Override
    public String downloadPhoto(String photoName, String fileName) {
        URI imageUri =
                webClient
                        .get()
                        .uri(
                                "https://places.googleapis.com/v1/"
                                        + photoName
                                        + "/media?maxHeightPx=1600")
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

        return iService.download(imageUri, fileName);
    }
}
