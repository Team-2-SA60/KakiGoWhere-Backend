package team2.kakigowherebackend;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;
import team2.kakigowherebackend.dto.RatingItemDTO;
import team2.kakigowherebackend.dto.RatingRequestDTO;
import team2.kakigowherebackend.dto.RatingSummaryDTO;
import team2.kakigowherebackend.model.Place;
import team2.kakigowherebackend.model.Rating;
import team2.kakigowherebackend.model.Tourist;
import team2.kakigowherebackend.repository.RatingRepository;
import team2.kakigowherebackend.service.RatingServiceImpl;

class RatingServiceTest {

    @Mock private RatingRepository ratingRepo;

    @InjectMocks private RatingServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetSummary_Empty_ReturnsZeroes() {
        long placeId = 1L;

        // simulate an empty list (no ratings)
        when(ratingRepo.findByPlaceId(placeId)).thenReturn(Collections.emptyList());

        RatingSummaryDTO out = service.getSummary(placeId);

        assertEquals(0.0, out.getAverageRating());
        assertEquals(0, out.getRatingCount());
    }

    @Test
    void testGetSummary_WithRatings_ComputesAverage() {
        long placeId = 2L;
        Rating r1 = new Rating();
        r1.setRating(4);
        Rating r2 = new Rating();
        r2.setRating(5);
        Rating r3 = new Rating();
        r3.setRating(3);
        when(ratingRepo.findByPlaceId(placeId)).thenReturn(Arrays.asList(r1, r2, r3));

        RatingSummaryDTO out = service.getSummary(placeId);

        assertEquals(4.0, out.getAverageRating()); // average is 4.0
        assertEquals(3, out.getRatingCount());
    }

    @Test
    void testGetMyRatingItem_Found_ReturnsDTO() {
        long placeId = 3L, touristId = 1L;
        RatingItemDTO dto = new RatingItemDTO(100L, touristId, "A", 5, "Great");
        when(ratingRepo.findMyRatingItemDTO(placeId, touristId)).thenReturn(Optional.of(dto));

        RatingItemDTO out = service.getMyRatingItem(placeId, touristId);

        assertSame(dto, out);
    }

    @Test
    void testGetMyRatingItem_NotFound_ReturnsNull() {
        long placeId = 3L, touristId = 10L;
        when(ratingRepo.findMyRatingItemDTO(placeId, touristId)).thenReturn(Optional.empty());

        RatingItemDTO out = service.getMyRatingItem(placeId, touristId);

        assertNull(out);
    }

    @Test
    void testGetOtherRatings_Passthrough() {
        long placeId = 4L, touristId = 11L;
        List<RatingItemDTO> list =
                Arrays.asList(
                        new RatingItemDTO(1L, 2L, "B", 4, "Nice"),
                        new RatingItemDTO(2L, 3L, "C", 3, "Ok"));
        when(ratingRepo.findOtherRatingItemDTOs(placeId, touristId)).thenReturn(list);

        List<RatingItemDTO> out = service.getOtherRatings(placeId, touristId);

        assertSame(list, out);
    }

    @Test
    void testSubmitOrUpdate_InvalidRatingLow_Throws400() {
        long placeId = 5L, touristId = 20L;
        RatingRequestDTO req = new RatingRequestDTO();
        req.setRating(0); // invalid (<1)
        req.setComment("ok");

        ResponseStatusException ex =
                assertThrows(
                        ResponseStatusException.class,
                        () -> service.submitOrUpdate(placeId, touristId, req));
        assertEquals(400, ex.getStatusCode().value());
    }

    @Test
    void testSubmitOrUpdate_InvalidRatingHigh_Throws400() {
        long placeId = 5L, touristId = 21L;
        RatingRequestDTO req = new RatingRequestDTO();
        req.setRating(6); // invalid (>5)
        req.setComment("ok");

        ResponseStatusException ex =
                assertThrows(
                        ResponseStatusException.class,
                        () -> service.submitOrUpdate(placeId, touristId, req));
        assertEquals(400, ex.getStatusCode().value());
    }

    @Test
    void testSubmitOrUpdate_EmptyComment_Throws400() {
        long placeId = 5L, touristId = 22L;
        RatingRequestDTO req = new RatingRequestDTO();
        req.setRating(4); // valid rating
        req.setComment("   "); // blank after trim

        ResponseStatusException ex =
                assertThrows(
                        ResponseStatusException.class,
                        () -> service.submitOrUpdate(placeId, touristId, req));
        assertEquals(400, ex.getStatusCode().value());
    }

    @Test
    void testSubmitOrUpdate_UpdateExisting_MutatesAndSaves() {
        long placeId = 6L, touristId = 30L;

        // simulate existing rating record
        Rating existing = new Rating();

        Tourist existingTourist = new Tourist();
        existingTourist.setId(touristId);
        existingTourist.setName("D");

        Place existingPlace = new Place();
        existingPlace.setId(placeId);
        existing.setTourist(existingTourist);
        existing.setPlace(existingPlace);
        existing.setId(77L);
        existing.setRating(2);
        existing.setComment("old");

        when(ratingRepo.findByPlaceIdAndTouristId(placeId, touristId))
                .thenReturn(Optional.of(existing));

        // incoming request
        RatingRequestDTO req = new RatingRequestDTO();
        req.setRating(5);
        req.setComment("awesome");

        // save returns the same but mutated entity
        when(ratingRepo.save(any(Rating.class))).thenAnswer(inv -> inv.getArgument(0));

        RatingItemDTO out = service.submitOrUpdate(placeId, touristId, req);

        assertEquals(77L, out.getRatingId());
        assertEquals(touristId, out.getTouristId());
        assertEquals("D", out.getTouristName());
        assertEquals(5, out.getRating());
        assertEquals("awesome", out.getComment());
        verify(ratingRepo).save(same(existing));
    }

    @Test
    void testSubmitOrUpdate_NewInsert_BuildsEntitiesAndSaves() {
        long placeId = 7L, touristId = 40L;

        when(ratingRepo.findByPlaceIdAndTouristId(placeId, touristId)).thenReturn(Optional.empty());

        RatingRequestDTO req = new RatingRequestDTO();
        req.setRating(3);
        req.setComment("okish");

        when(ratingRepo.save(any(Rating.class)))
                .thenAnswer(
                        inv -> {
                            Rating r = inv.getArgument(0);
                            r.setId(999L); // simulate DB id
                            if (r.getTourist() != null)
                                r.getTourist().setName("New User"); // name for DTO
                            return r;
                        });

        RatingItemDTO out = service.submitOrUpdate(placeId, touristId, req);

        assertEquals(999L, out.getRatingId());
        assertEquals(touristId, out.getTouristId());
        assertEquals("New User", out.getTouristName());
        assertEquals(3, out.getRating());
        assertEquals("okish", out.getComment());

        verify(ratingRepo)
                .save(
                        argThat(
                                r ->
                                        r.getPlace() != null
                                                && r.getPlace().getId() == placeId
                                                && r.getTourist() != null
                                                && r.getTourist().getId() == touristId
                                                && r.getRating() == 3
                                                && "okish".equals(r.getComment())));
    }

    @Test
    void testGetAllRatings_Passthrough() {
        List<Rating> list = Arrays.asList(new Rating(), new Rating());
        when(ratingRepo.findAll()).thenReturn(list);

        List<Rating> out = service.getAllRatings();

        assertSame(list, out);
    }
}
