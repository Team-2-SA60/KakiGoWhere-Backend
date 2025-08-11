package team2.kakigowherebackend;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import team2.kakigowherebackend.model.Place;
import team2.kakigowherebackend.model.Tourist;
import team2.kakigowherebackend.model.TouristPlaceVisit;
import team2.kakigowherebackend.repository.PlaceRepository;
import team2.kakigowherebackend.repository.TouristPlaceVisitRepository;
import team2.kakigowherebackend.repository.TouristRepository;
import team2.kakigowherebackend.service.PlaceVisitServiceImpl;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class PlaceVisitServiceTest {
    @Mock private TouristPlaceVisitRepository visitRepo;

    @Mock private TouristRepository touristRepo;

    @Mock private PlaceRepository placeRepo;

    @InjectMocks private PlaceVisitServiceImpl service;

    // init @Mock/@InjectMocks fields
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRecordVisit_AlreadyExists_NoSave() {
        long touristId = 1L;
        long placeId = 2L;
        LocalDate date = LocalDate.of(2025, 8, 9);

        // simulate that the entry already exists (True)
        when(visitRepo.existsByTouristIdAndPlaceIdAndVisitDate(touristId, placeId, date))
                .thenReturn(true);

        service.recordVisit(touristId, placeId, date);

        verify(visitRepo, never()).save(any(TouristPlaceVisit.class));
        verifyNoInteractions(touristRepo, placeRepo);
    }

    @Test
    void testRecordVisit_NewVisit_SavedWithCorrectFields() {
        long touristId = 10L;
        long placeId = 20L;
        LocalDate date = LocalDate.of(2025, 8, 7);

        when(visitRepo.existsByTouristIdAndPlaceIdAndVisitDate(touristId, placeId, date))
                .thenReturn(false);

        Tourist t = new Tourist();
        Place p = new Place();
        when(touristRepo.getReferenceById(touristId)).thenReturn(t);
        when(placeRepo.getReferenceById(placeId)).thenReturn(p);

        service.recordVisit(touristId, placeId, date);

        // assert that it is saved with the correct fields
        verify(visitRepo)
                .save(
                        argThat(
                                v ->
                                        v != null
                                                && v.getTourist() == t
                                                && v.getPlace() == p
                                                && date.equals(v.getVisitDate())));
    }

    @Test
    void testGetDailyVisitCounts_PrefillAndMerge() {
        long placeId = 30L;
        YearMonth ym = YearMonth.of(2025, 8);

        List<Object[]> rows =
                List.of(
                        new Object[] {LocalDate.of(2025, 8, 2), 5L},
                        new Object[] {LocalDate.of(2025, 8, 15), 3L});
        when(visitRepo.findByPlaceAndMonth(placeId, 2025, 8)).thenReturn(rows);

        Map<LocalDate, Integer> map = service.getDailyVisitCounts(placeId, ym); // act

        assertEquals(31, map.size());
        assertEquals(0, map.get(ym.atDay(1)));
        assertEquals(5, map.get(ym.atDay(2)));
        assertEquals(3, map.get(ym.atDay(15)));
        assertEquals(0, map.get(ym.atDay(31)));
    }

    @Test
    void testGetDailyVisitCounts_EmptyResults_AllZero_LeapMonth() {
        long placeId = 66L;
        YearMonth ym = YearMonth.of(2024, 2); // Feb 2024 (leap year, 29 days)

        // simulate empty list
        when(visitRepo.findByPlaceAndMonth(placeId, 2024, 2)).thenReturn(Collections.emptyList());

        Map<LocalDate, Integer> map = service.getDailyVisitCounts(placeId, ym);

        assertEquals(29, map.size());
        // all values are zero
        assertTrue(map.values().stream().allMatch(v -> v == 0));
    }
}
