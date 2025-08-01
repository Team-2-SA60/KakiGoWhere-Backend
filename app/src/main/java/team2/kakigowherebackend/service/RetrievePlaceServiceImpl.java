package team2.kakigowherebackend.service;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import team2.kakigowherebackend.model.*;
import team2.kakigowherebackend.repository.InterestCategoryRepository;
import team2.kakigowherebackend.repository.PlaceRepository;
import team2.kakigowherebackend.repository.TouristRepository;

@Slf4j
@Service
public class RetrievePlaceServiceImpl implements RetrievePlaceService {

    // Excluded Interest categories from adding to our database
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

    // Retrieve all Places in our database and attempts to update each of them using Google Places
    // API
    @Override
    public void retrievePlaces() {
        log.info("Retrieving and updating places...");

        List<Place> places = pRepo.findAll();

        for (int i = 0; i < places.size(); i++) {
            try {
                Place p = places.get(i);

                // Copying original place, so that we can compare if there's any changes at the end
                // before downloading image and saving
                Place updatedPlace = new Place(p);

                String name = p.getName();
                String googleId = p.getGoogleId();

                // Fetch Place Detail from Google Places API
                JsonNode placeNode = gpService.searchPlace(googleId).block();

                if (placeNode == null) {
                    log.info("Failed to retrieve from Google place for: {}", name);
                    continue;
                }

                // Extracts information from Place Detail and maps it accordingly to updatedPlace
                // attributes
                mapGooglePlace(updatedPlace, placeNode);
                addOpeningHours(updatedPlace, placeNode);
                checkAndAddInterestCategories(updatedPlace, placeNode);

                if (p.getRatings().isEmpty()) checkAndAddRatings(updatedPlace, placeNode);

                // If updatedPlace is different from original Place, commit to download image and
                // save the updatedPlace
                if (!updatedPlace.equals(p)) {
                    downloadImages(updatedPlace, placeNode);
                    pRepo.save(updatedPlace);
                    log.info("Updated place for: {}", name);
                }
            } catch (Exception e) {
                log.info("Failed to update place for: {}", places.get(i).getName());
                log.error(Arrays.toString(e.getStackTrace()));
            }
        }
        log.info("Retrieved and updated places");
    }

    @Override
    public void mapGooglePlace(Place place, JsonNode placeNode) {
        JsonNode displayNameNode = placeNode.path("displayName").path("text");
        JsonNode websiteUriNode = placeNode.path("websiteUri");
        JsonNode addressNode = placeNode.path("shortFormattedAddress");
        JsonNode latNode = placeNode.path("location").path("latitude");
        JsonNode lngNode = placeNode.path("location").path("longitude");
        JsonNode businessStatusNode = placeNode.path("businessStatus");
        JsonNode editorialSummaryNode = placeNode.path("editorialSummary").path("text");
        JsonNode openingDescNode =
                placeNode.path("regularOpeningHours").path("weekdayDescriptions");

        if (!displayNameNode.isMissingNode()) place.setName(displayNameNode.asText());
        if (!websiteUriNode.isMissingNode()) place.setURL(websiteUriNode.asText());
        if (!addressNode.isMissingNode()) place.setAddress(addressNode.asText());
        if (!latNode.isMissingNode()) place.setLatitude(latNode.asDouble());
        if (!lngNode.isMissingNode()) place.setLongitude(lngNode.asDouble());

        // If businessStatus is NOT absent and NOT equals("OPERATIONAL"), set Place.active as False
        place.setActive(
                businessStatusNode.isMissingNode()
                        || businessStatusNode.asText().equals("OPERATIONAL"));

        // If editorialSummary is NOT absent and Place.description IS EMPTY, set new
        // Place.description
        // So that we don't overwrite descriptions we added ourselves
        if (!editorialSummaryNode.isMissingNode()) {
            String currentDesc = place.getDescription();
            if (currentDesc == null || currentDesc.isEmpty()) {
                place.setDescription(editorialSummaryNode.asText());
            }
        }

        StringBuilder openingDesc = new StringBuilder();
        if (openingDescNode.isArray()) {
            for (JsonNode desc : openingDescNode) {
                openingDesc.append(desc.asText()).append("\n");
            }
        }
        place.setOpeningDescription(openingDesc.toString());
    }

    @Override
    public void addOpeningHours(Place place, JsonNode placeNode) {
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

                if (!openDay.isMissingNode()) oh.setOpenDay(openDay.asInt());
                if (!openHour.isMissingNode()) oh.setOpenHour(openHour.asInt());
                if (!openMinute.isMissingNode()) oh.setOpenMinute(openMinute.asInt());
                if (!closeDay.isMissingNode()) oh.setCloseDay(closeDay.asInt());
                if (!closeHour.isMissingNode()) oh.setCloseHour(closeHour.asInt());
                if (!closeMinute.isMissingNode()) oh.setCloseMinute(closeMinute.asInt());

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
        int i = new Random().nextInt(6);

        if (reviewsNode.isArray()) {
            for (JsonNode reviewNode : reviewsNode) {
                JsonNode ratingNode = reviewNode.path("rating");
                JsonNode commentNode = reviewNode.path("text").path("text");

                if (ratingNode.isMissingNode() || commentNode.isMissingNode()) continue;

                Rating rating = new Rating();
                rating.setComment(commentNode.asText());
                rating.setRating(ratingNode.asInt());
                rating.setPlace(place);

                rating.setTourist(tourists.get(i));

                i += 1;
                if (i >= 6) {
                    i = 0;
                }

                ratings.add(rating);
            }
        }
        if (!ratings.isEmpty()) {
            place.setRatings(ratings);
        }
    }

    @Override
    public void downloadImages(Place place, JsonNode placeNode) {
        JsonNode photosNode = placeNode.path("photos");
        if (!photosNode.isMissingNode()) {
            String photoName = photosNode.get(0).path("name").asText();
            String imageName = place.getGoogleId();
            String imagePath = gpService.downloadPhoto(photoName, imageName);
            place.setImagePath(imagePath);
        }
    }
}
