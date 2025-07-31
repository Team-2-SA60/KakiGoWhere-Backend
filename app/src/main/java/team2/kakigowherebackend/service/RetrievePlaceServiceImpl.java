package team2.kakigowherebackend.service;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import team2.kakigowherebackend.model.*;
import team2.kakigowherebackend.repository.InterestCategoryRepository;
import team2.kakigowherebackend.repository.PlaceRepository;
import team2.kakigowherebackend.repository.TouristRepository;

@Service
public class RetrievePlaceServiceImpl implements RetrievePlaceService {

    private static final Logger log = LoggerFactory.getLogger(RetrievePlaceServiceImpl.class);

    private final String[] EXCLUDED_CATEGORIES = {
        "point_of_interest", "tourist_attraction", "establishment", "event_venue"
    };

    private final PlaceRepository pRepo;
    private final TouristRepository tRepo;
    private final InterestCategoryRepository icRepo;
    private final GooglePlaceService gpService;

    public RetrievePlaceServiceImpl(
            PlaceRepository pRepo,
            TouristRepository tRepo,
            InterestCategoryRepository icRepo,
            GooglePlaceService gpService) {
        this.pRepo = pRepo;
        this.tRepo = tRepo;
        this.icRepo = icRepo;
        this.gpService = gpService;
    }

    @Override
    public void retrievePlaces() {
        log.info("Retrieving places...");

        List<Place> places = pRepo.findAll();

        for (int i = 0; i < places.size(); i++) {
            Place p = places.get(i);
            Place updatedPlace = new Place();

            long id = p.getId();
            String name = p.getName();
            String googleId = p.getGoogleId();

            updatedPlace.setId(id);
            updatedPlace.setName(name);
            updatedPlace.setGoogleId(googleId);
            updatedPlace.setDescription(p.getDescription());

            JsonNode placeNode = gpService.searchPlace(googleId).block();

            if (placeNode == null) {
                log.info("Failed to retrieve from Google place for: {}", name);
                continue;
            }

            mapGooglePlace(updatedPlace, placeNode);
            checkAndAddInterestCategories(updatedPlace, placeNode);
            if (p.getRatings().isEmpty()) {
                checkAndAddRatings(updatedPlace, placeNode);
            }

            if (updatedPlace.equals(p)) {
                log.info("Not updated as it already exists for: {}", name);
                continue;
            }

            JsonNode photosNode = placeNode.path("photos");
            if (!photosNode.isMissingNode()) {
                String photoName = photosNode.get(0).path("name").asText();
                String imageName = updatedPlace.getGoogleId();
                String imagePath = gpService.downloadPhoto(photoName, imageName);
                updatedPlace.setImagePath(imagePath);
            }

            pRepo.save(updatedPlace);
            log.info("Updated place for: {}", name);
        }
    }

    @Override
    public void mapGooglePlace(Place place, JsonNode placeNode) {

        JsonNode displayNameNode = placeNode.path("displayName").path("text");
        JsonNode websiteUriNode = placeNode.path("websiteUri");
        JsonNode latNode = placeNode.path("location").path("latitude");
        JsonNode lngNode = placeNode.path("location").path("longitude");
        JsonNode businessStatusNode = placeNode.path("businessStatus");
        JsonNode editorialSummaryNode = placeNode.path("editorialSummary").path("text");
        JsonNode openingDescNode =
                placeNode.path("regularOpeningHours").path("weekdayDescriptions");

        place.setName(displayNameNode.isMissingNode() ? "" : displayNameNode.asText());
        place.setURL(websiteUriNode.isMissingNode() ? "" : websiteUriNode.asText());
        place.setLatitude(latNode.isMissingNode() ? 0.0 : latNode.asDouble());
        place.setLongitude(lngNode.isMissingNode() ? 0.0 : lngNode.asDouble());
        place.setActive(
                businessStatusNode.isMissingNode()
                        || businessStatusNode.asText().equals("OPERATIONAL"));

        if (place.getDescription() == null) {
            place.setDescription(
                    editorialSummaryNode.isMissingNode() ? "" : editorialSummaryNode.asText());
        }

        StringBuilder openingDesc = new StringBuilder();
        if (openingDescNode.isArray()) {
            for (JsonNode desc : openingDescNode) {
                openingDesc.append(desc.asText()).append("\n");
            }
        }
        place.setOpeningDescription(openingDesc.toString());

        JsonNode periodsNode = placeNode.path("regularOpeningHours").path("periods");
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
    public void checkAndAddInterestCategories(Place place, JsonNode placeNode) {
        JsonNode typesNode = placeNode.path("types");
        List<InterestCategory> interestCategories = new ArrayList<>();
        if (typesNode.isArray()) {
            for (JsonNode type : typesNode) {
                String category = type.asText();

                boolean isExcluded = Arrays.asList(EXCLUDED_CATEGORIES).contains(category);
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

    @Override
    public void checkAndAddRatings(Place place, JsonNode placeNode) {
        JsonNode reviewsNode = placeNode.path("reviews");

        List<Tourist> tourists = tRepo.findAll();
        List<Rating> ratings = new ArrayList<>();

        if (reviewsNode.isArray()) {
            for (JsonNode reviewNode : reviewsNode) {
                JsonNode ratingNode = reviewNode.path("rating");
                JsonNode commentNode = reviewNode.path("text").path("text");

                if (ratingNode.isMissingNode() || commentNode.isMissingNode()) continue;

                Rating rating = new Rating();
                rating.setComment(commentNode.asText());
                rating.setRating(ratingNode.asInt());
                rating.setPlace(place);

                Random random = new Random();
                rating.setTourist(tourists.get(random.nextInt(tourists.size())));

                ratings.add(rating);
            }
        }
        if (!ratings.isEmpty()) {
            place.setRatings(ratings);
        }
    }
}
