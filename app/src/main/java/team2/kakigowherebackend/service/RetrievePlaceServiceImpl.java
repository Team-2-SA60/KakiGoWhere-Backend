package team2.kakigowherebackend.service;

import com.fasterxml.jackson.databind.JsonNode;
import java.io.ByteArrayInputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import reactor.netty.http.client.HttpClient;
import team2.kakigowherebackend.model.InterestCategory;
import team2.kakigowherebackend.model.OpeningHours;
import team2.kakigowherebackend.model.Place;
import team2.kakigowherebackend.repository.InterestCategoryRepository;
import team2.kakigowherebackend.repository.PlaceRepository;
import team2.kakigowherebackend.utils.TextEncoding;

@Service
public class RetrievePlaceServiceImpl implements RetrievePlaceService {

    private static final Logger log = LoggerFactory.getLogger(RetrievePlaceServiceImpl.class);

    private static final String KMLID = "KMLID";
    private static final String PAGETITLE = "PAGETITLE";
    private static final String OVERVIEW = "OVERVIEW";

    private static final String dataset = "d_15a8ecc14700107f2b5696335a697b9c";
    private static final String uri =
            "https://api-open.data.gov.sg/v1/public/api/datasets/" + dataset + "/poll-download";

    private final WebClient webClient;
    private final PlaceRepository pRepo;
    private final InterestCategoryRepository icRepo;
    private final GooglePlaceService gpService;

    public RetrievePlaceServiceImpl(
            PlaceRepository pRepo,
            InterestCategoryRepository icRepo,
            GooglePlaceService gpService) {
        this.pRepo = pRepo;
        this.icRepo = icRepo;
        this.gpService = gpService;

        HttpClient httpClient = HttpClient.create().followRedirect(true);
        this.webClient =
                WebClient.builder()
                        .clientConnector(new ReactorClientHttpConnector(httpClient))
                        .defaultHeader(HttpHeaders.ACCEPT, "application/json; charset=ISO-8859-1")
                        .defaultHeader(HttpHeaders.USER_AGENT, "SpringBootClient/1.0")
                        .codecs(
                                configurer ->
                                        configurer
                                                .defaultCodecs()
                                                .maxInMemorySize(10 * 1024 * 1024))
                        .build();
    }

    @Override
    public void retrievePlaces() {
        log.info("Retrieving places...");

        String kmlContent = fetchPlacesKml();
        if (kmlContent.isEmpty()) return;

        List<Map<String, String>> placesList = parseKML(kmlContent);

        for (Map<String, String> fetchedPlace : placesList) {
            try {
                Place newPlace = new Place();
                newPlace.setKmlId(fetchedPlace.get(KMLID));
                newPlace.setDescription(fetchedPlace.get(OVERVIEW));

                JsonNode googlePlace =
                        gpService
                                .searchPlace(fetchedPlace.get(PAGETITLE))
                                .map(response -> response.path("places").get(0))
                                .block();

                if (googlePlace == null) {
                    log.info(
                            "Failed to retrieve from Google place for: {}",
                            fetchedPlace.get(PAGETITLE));
                    continue;
                }

                mapGooglePlace(newPlace, googlePlace);
                checkAndAddInterestCategories(newPlace, googlePlace);

                Place existingPlace = pRepo.findByKmlId(fetchedPlace.get(KMLID));
                if (existingPlace != null) {
                    if (newPlace.equals(existingPlace)) {
                        log.info("Not updated as it already exists for: {}", newPlace.getName());
                        continue;
                    } else {
                        newPlace.setId(existingPlace.getId());
                    }
                }

                JsonNode photosNode = googlePlace.path("photos");
                if (!photosNode.isMissingNode()) {
                    String photoName = photosNode.get(0).path("name").asText();
                    String imageName =
                            newPlace.getKmlId() + "_" + newPlace.getName().replaceAll(" ", "");
                    String imagePath = gpService.downloadPhoto(photoName, imageName);
                    newPlace.setImagePath(imagePath);
                }

                pRepo.save(newPlace);
                log.info("Updated place for: {}", newPlace.getName());
            } catch (Exception e) {
                log.info(
                        "Failed to update place for: {}",
                        fetchedPlace.get(PAGETITLE) + "\n" + e.getMessage());
            }
        }

        return;
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
                map.put(KMLID, placemarkId);

                NodeList simpleData = placemark.getElementsByTagNameNS("*", "SimpleData");

                String placeTitle = null;
                String description = null;

                for (int j = 0; j < simpleData.getLength(); j++) {
                    Element simpleDataElement = (Element) simpleData.item(j);

                    if (simpleDataElement.getAttribute("name").equals(PAGETITLE)) {
                        placeTitle = simpleDataElement.getTextContent();
                        map.put(PAGETITLE, placeTitle);
                    }
                    if (simpleDataElement.getAttribute("name").equals(OVERVIEW)) {
                        description = simpleDataElement.getTextContent();
                        description = TextEncoding.fixEncoding(description);
                        map.put(OVERVIEW, description);
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
    public void mapGooglePlace(Place place, JsonNode googlePlace) {

        JsonNode displayNameNode = googlePlace.path("displayName").path("text");
        place.setName(displayNameNode.isMissingNode() ? "" : displayNameNode.asText());

        JsonNode websiteUriNode = googlePlace.path("websiteUri");
        place.setURL(websiteUriNode.isMissingNode() ? "" : websiteUriNode.asText());

        JsonNode openingDescNode =
                googlePlace.path("regularOpeningHours").path("weekdayDescriptions");
        StringBuilder openingDesc = new StringBuilder();
        if (openingDescNode.isArray()) {
            for (JsonNode desc : openingDescNode) {
                openingDesc.append(desc.asText()).append("\n");
            }
        }
        place.setOpeningDescription(openingDesc.toString());

        JsonNode latNode = googlePlace.path("location").path("latitude");
        place.setLatitude(latNode.isMissingNode() ? 0.0 : latNode.asDouble());

        JsonNode lngNode = googlePlace.path("location").path("longitude");
        place.setLongitude(lngNode.isMissingNode() ? 0.0 : lngNode.asDouble());
        place.setActive(true);

        JsonNode periodsNode = googlePlace.path("regularOpeningHours").path("periods");
        List<OpeningHours> openingHours = new ArrayList<>();
        if (periodsNode.isArray()) {
            for (JsonNode period : periodsNode) {
                OpeningHours oh = new OpeningHours();

                JsonNode openDay = period.path("open").path("day");
                JsonNode openHour = period.path("open").path("hour");
                JsonNode openMinute = period.path("open").path("minute");
                JsonNode closeDay = period.path("close").path("day");
                JsonNode closeHour = period.path("close").path("hour");
                JsonNode closeMinute = period.path("close").path("minute");

                if (!openDay.isMissingNode()) {
                    oh.setOpenDay(openDay.asInt());
                }
                if (!openHour.isMissingNode()) {
                    oh.setOpenHour(openHour.asInt());
                }
                if (!openMinute.isMissingNode()) {
                    oh.setOpenMinute(openMinute.asInt());
                }
                if (!closeDay.isMissingNode()) {
                    oh.setCloseDay(closeDay.asInt());
                }
                if (!closeHour.isMissingNode()) {
                    oh.setCloseHour(closeHour.asInt());
                }
                if (!closeMinute.isMissingNode()) {
                    oh.setCloseMinute(closeMinute.asInt());
                }
                openingHours.add(oh);
            }
        }
        place.setOpeningHours(openingHours);
    }

    @Override
    public void checkAndAddInterestCategories(Place place, JsonNode googlePlace) {
        String[] excludedCategories = {
            "point_of_interest", "tourist_attraction", "establishment", "event_venue"
        };

        JsonNode typesNode = googlePlace.path("types");
        List<InterestCategory> interestCategories = new ArrayList<>();
        if (typesNode.isArray()) {
            for (JsonNode type : typesNode) {
                String category = type.asText();

                boolean isExcluded = Arrays.asList(excludedCategories).contains(category);
                if (isExcluded) continue;

                InterestCategory interestCategory = icRepo.findByName(category);
                if (interestCategory == null) {
                    interestCategory = new InterestCategory();
                    String description = StringUtils.capitalize(category.replaceAll("_", " "));
                    interestCategory.setName(category);
                    interestCategory.setDescription(description);
                    icRepo.save(interestCategory);
                }

                interestCategories.add(interestCategory);
            }
        }
        place.setInterestCategories(interestCategories);
    }
}
