package team2.kakigowherebackend.service;

import com.fasterxml.jackson.databind.JsonNode;
import java.io.ByteArrayInputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import reactor.core.publisher.Mono;

@Service
public class RetrievePlaceServiceImpl implements RetrievePlaceService {

    private static final Logger log = LoggerFactory.getLogger(RetrievePlaceServiceImpl.class);

    private static final String dataset = "d_15a8ecc14700107f2b5696335a697b9c";
    private static final String uri =
            "https://api-open.data.gov.sg/v1/public/api/datasets/" + dataset + "/poll-download";

    private final WebClient webClient;

    public RetrievePlaceServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient =
                webClientBuilder
                        .defaultHeader(HttpHeaders.ACCEPT, "application/json")
                        .defaultHeader(HttpHeaders.USER_AGENT, "SpringBootClient/1.0")
                        .codecs(
                                configurer ->
                                        configurer
                                                .defaultCodecs()
                                                .maxInMemorySize(10 * 1024 * 1024))
                        .build();
    }

    @Override
    public boolean updatePlaces() {
        String kmlContent = fetchPlacesKml();
        if (kmlContent.isEmpty()) return false;

        List<Map<String, String>> placesList = parseKML(kmlContent);

        log.info(placesList.toString());

        return true;
    }

    @Override
    public String fetchPlacesKml() {
        URI kmlUri =
                webClient
                        .get()
                        .uri(uri)
                        .retrieve()
                        .bodyToMono(JsonNode.class)
                        .map(
                                response -> {
                                    URI uri = null;
                                    try {
                                        uri = new URI(response.path("data").path("url").asText());
                                    } catch (URISyntaxException e) {
                                        throw new RuntimeException(e);
                                    }
                                    return uri;
                                })
                        .block();

        if (kmlUri == null) return null;

        return webClient.get().uri(kmlUri).retrieve().bodyToMono(String.class).block();
    }

    @Override
    public List<Map<String, String>> parseKML(String kmlContent) {
        List<Map<String, String>> result = new ArrayList<>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc =
                    builder.parse(
                            new ByteArrayInputStream(kmlContent.getBytes(StandardCharsets.UTF_8)));

            NodeList placemarks = doc.getElementsByTagNameNS("*", "Placemark");

            for (int i = 0; i < placemarks.getLength(); i++) {
                Map<String, String> map = new HashMap<>();

                Element placemark = (Element) placemarks.item(i);
                String placemarkId =
                        placemark.getElementsByTagName("name").item(0).getTextContent();
                map.put("kmlId", placemarkId);

                NodeList simpleData = placemark.getElementsByTagNameNS("*", "SimpleData");

                String placeTitle = null;
                String image_path = null;

                for (int j = 0; j < simpleData.getLength(); j++) {
                    Element simpleDataElement = (Element) simpleData.item(j);

                    if (simpleDataElement.getAttribute("name").equals("PAGETITLE")) {
                        placeTitle = simpleDataElement.getTextContent();
                        map.put("pageTitle", placeTitle);
                    }

                    if (simpleDataElement.getAttribute("name").equals("IMAGE_PATH")) {
                        image_path = simpleDataElement.getTextContent();
                        map.put("imagePath", image_path);
                    }
                }

                result.add(map);
            }
        } catch (Exception e) {
            log.error("Failed to parse KML file", e);
        }

        return result;
    }

    @Override
    public Mono<JsonNode> fetchGooglePlace(String placeTitle) {
        return null;
    }

    @Override
    public String downloadImage(URI imageUri) {
        return "";
    }
}
