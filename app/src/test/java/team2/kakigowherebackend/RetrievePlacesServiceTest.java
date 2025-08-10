package team2.kakigowherebackend;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import reactor.core.publisher.Mono;
import team2.kakigowherebackend.model.*;
import team2.kakigowherebackend.repository.*;
import team2.kakigowherebackend.service.GooglePlaceService;
import team2.kakigowherebackend.service.RetrievePlaceServiceImpl;

public class RetrievePlacesServiceTest {

    @Mock private PlaceRepository placeRepo;

    @Mock private TouristRepository touristRepo;

    @Mock private InterestCategoryRepository icRepo;

    @Mock private GooglePlaceService gpService; // won't be used here

    @InjectMocks private RetrievePlaceServiceImpl service;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRetrievePlaces_updatesPlacesAndSaves() throws Exception {
        // Arrange: existing place with initial name
        Place place = new Place();
        place.setId(1L);
        place.setName("Old Name");
        place.setGoogleId("google-123");
        place.setRatings(new ArrayList<>());
        place.setInterestCategories(new ArrayList<>());
        place.setOpeningHours(new ArrayList<>());
        place.setAutoFetch(true);

        when(placeRepo.findAllByAutoFetch(true)).thenReturn(Collections.singletonList(place));

        // Create a JsonNode with a changed displayName
        ObjectNode placeNode = objectMapper.createObjectNode();
        ObjectNode displayNameNode = objectMapper.createObjectNode();
        displayNameNode.put("text", "New Name"); // Different from old name
        displayNameNode.put("shortFormattedAddress", "New address");
        placeNode.set("displayName", displayNameNode);

        // Mock gpService to return this changed placeNode
        when(gpService.searchPlace("google-123")).thenReturn(Mono.just(placeNode));

        // Act: call service method
        service.retrievePlaces();

        // Assert: verify placeRepo.save was called once
        verify(placeRepo, times(1)).save(any(Place.class));
    }

    @Test
    void testMapGooglePlace_setsExpectedFields() throws Exception {
        Place place = new Place();

        String jsonStr =
                """
            {
                "displayName": {"text": "Test Place"},
                "websiteUri": "http://test.com",
                "shortFormattedAddress": "123 Test Ave",
                "location": {"latitude": 1.23, "longitude": 4.56},
                "businessStatus": "OPERATIONAL",
                "editorialSummary": {"text": "Great place!"},
                "regularOpeningHours": {"weekdayDescriptions": ["Mon-Fri: 9-5"]}
            }
            """;

        JsonNode placeNode = objectMapper.readTree(jsonStr);

        service.mapGooglePlace(place, placeNode);

        assertEquals("Test Place", place.getName());
        assertEquals("http://test.com", place.getURL());
        assertEquals("123 Test Ave", place.getAddress());
        assertEquals(1.23, place.getLatitude());
        assertEquals(4.56, place.getLongitude());
        assertTrue(place.isActive());
        assertEquals("Great place!", place.getDescription());
        assertTrue(place.getOpeningDescription().contains("Mon-Fri"));
    }

    @Test
    void testAddOpeningHours_addsOpeningHoursCorrectly() throws Exception {
        Place place = new Place();

        String jsonStr =
                """
            {
                "regularOpeningHours": {
                    "periods": [
                        {
                            "open": {"day": 1, "hour": 9, "minute": 0},
                            "close": {"day": 1, "hour": 17, "minute": 30}
                        }
                    ]
                }
            }
            """;

        JsonNode placeNode = objectMapper.readTree(jsonStr);

        service.addOpeningHours(place, placeNode);

        assertEquals(1, place.getOpeningHours().size());
        OpeningHours oh = place.getOpeningHours().get(0);
        assertEquals(1, oh.getOpenDay());
        assertEquals(9, oh.getOpenHour());
        assertEquals(17, oh.getCloseHour());
        assertEquals(30, oh.getCloseMinute());
    }

    @Test
    void testCheckAndAddInterestCategories_addsCategoriesAndSavesNew() {
        Place place = new Place();

        String jsonStr =
                """
            {
                "types": ["restaurant", "museum", "point_of_interest"]
            }
            """;

        JsonNode placeNode = null;
        try {
            placeNode = objectMapper.readTree(jsonStr);
        } catch (Exception ignored) {
        }

        // Only "restaurant" and "museum" should be added, "point_of_interest" excluded
        when(icRepo.findByName("restaurant")).thenReturn(Optional.empty());
        when(icRepo.findByName("museum")).thenReturn(Optional.empty());
        when(icRepo.save(any(InterestCategory.class))).thenAnswer(i -> i.getArgument(0));

        service.checkAndAddInterestCategories(place, placeNode);

        assertEquals(2, place.getInterestCategories().size());
        assertTrue(
                place.getInterestCategories().stream()
                        .anyMatch(c -> c.getName().equals("restaurant")));
        assertTrue(
                place.getInterestCategories().stream().anyMatch(c -> c.getName().equals("museum")));

        verify(icRepo, times(2)).save(any(InterestCategory.class));
    }

    @Test
    void testCheckAndAddRatings_setsRatings() throws Exception {
        Place place = new Place();

        String jsonStr =
                """
            {
                "reviews": [
                    {"rating": 5, "text": {"text": "Excellent!"}},
                    {"rating": 3, "text": {"text": "Okay"}}
                ]
            }
            """;

        JsonNode placeNode = objectMapper.readTree(jsonStr);

        List<Tourist> tourists = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            tourists.add(new Tourist());
        }
        when(touristRepo.findAll()).thenReturn(tourists);

        service.checkAndAddRatings(place, placeNode);

        assertNotNull(place.getRatings());
        assertEquals(2, place.getRatings().size());
        assertEquals("Excellent!", place.getRatings().get(0).getComment());
        assertEquals(5, place.getRatings().get(0).getRating());
    }

    @Test
    void testDownloadImages_setsImagePath() {
        // Arrange
        Place place = new Place();
        place.setGoogleId("google-123");

        // Create JSON structure:
        // { "photos": [ { "name": "photo_1" } ] }
        ObjectNode placeNode = objectMapper.createObjectNode();
        ArrayNode photosArray = objectMapper.createArrayNode();
        ObjectNode photoNode = objectMapper.createObjectNode();
        photoNode.put("name", "photo_1");
        photosArray.add(photoNode);
        placeNode.set("photos", photosArray);

        // Stub gpService.downloadPhoto to return a sample path
        when(gpService.downloadPhoto("photo_1", "google-123")).thenReturn("/images/google-123.jpg");

        // Act
        service.downloadImages(place, placeNode);

        // Assert
        assertEquals("/images/google-123.jpg", place.getImagePath());
        verify(gpService, times(1)).downloadPhoto("photo_1", "google-123");
    }

    @Test
    void testDownloadImages_noPhotos_doesNothing() {
        // Arrange
        Place place = new Place();
        place.setGoogleId("google-123");

        ObjectNode placeNode = objectMapper.createObjectNode(); // no "photos" field

        // Act
        service.downloadImages(place, placeNode);

        // Assert
        assertNull(place.getImagePath());
        verify(gpService, never()).downloadPhoto(anyString(), anyString());
    }
}
