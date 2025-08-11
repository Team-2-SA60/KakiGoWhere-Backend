package team2.kakigowherebackend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import team2.kakigowherebackend.model.DailyStats;
import team2.kakigowherebackend.model.Place;
import team2.kakigowherebackend.model.PlaceStats;
import team2.kakigowherebackend.repository.DailyStatsRepository;
import team2.kakigowherebackend.repository.PlaceRepository;
import team2.kakigowherebackend.repository.PlaceStatsRepository;
import team2.kakigowherebackend.service.StatServiceImpl;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class StatServiceImplTest {

    @Mock DailyStatsRepository dailyStatsRepo;
    @Mock PlaceStatsRepository placeStatsRepo;
    @Mock PlaceRepository placeRepo;

    @InjectMocks StatServiceImpl service;

    LocalDate today;

    @BeforeEach
    void setup() {
        today = LocalDate.now();
    }

    // ---------------- addVisit ----------------
    @Test
    void addVisit_createsWhenMissingAndIncrements() {
        when(dailyStatsRepo.findByDate(any(LocalDate.class))).thenReturn(Optional.empty());
        when(dailyStatsRepo.save(any(DailyStats.class))).thenAnswer(inv -> inv.getArgument(0));

        LocalDate out = service.addVisit();

        assertEquals(today, out);
    }

    @Test
    void addVisit_incrementsExisting() {
        DailyStats ds = new DailyStats();
        ds.setDate(today);
        ds.setNumberOfUniqueVisits(0);
        when(dailyStatsRepo.findByDate(any(LocalDate.class))).thenReturn(Optional.of(ds));
        when(dailyStatsRepo.save(any(DailyStats.class))).thenAnswer(inv -> inv.getArgument(0));

        LocalDate out = service.addVisit();

        assertEquals(today, out);
    }

    // ---------------- addSignUp ----------------
    @Test
    void addSignUp_createsWhenMissingAndIncrements() {
        when(dailyStatsRepo.findByDate(any(LocalDate.class))).thenReturn(Optional.empty());
        when(dailyStatsRepo.save(any(DailyStats.class))).thenAnswer(inv -> inv.getArgument(0));

        LocalDate out = service.addSignUp();

        assertEquals(today, out);
        verify(dailyStatsRepo).save(any(DailyStats.class));
    }

    @Test
    void addSignUp_incrementsExisting() {
        DailyStats ds = new DailyStats();
        ds.setDate(today);
        ds.setNumberOfSignUps(0);
        when(dailyStatsRepo.findByDate(any(LocalDate.class))).thenReturn(Optional.of(ds));
        when(dailyStatsRepo.save(any(DailyStats.class))).thenAnswer(inv -> inv.getArgument(0));

        LocalDate out = service.addSignUp();

        assertEquals(today, out);
        verify(dailyStatsRepo).save(ds);
    }

    // ---------------- addPlaceVisit ----------------
    @Test
    void addPlaceVisit_createsDailyAndPlaceStatsAndIncrements() {
        // daily missing -> create
        when(dailyStatsRepo.findByDate(any(LocalDate.class))).thenReturn(Optional.empty());
        when(dailyStatsRepo.save(any(DailyStats.class))).thenAnswer(inv -> inv.getArgument(0));

        // place exists (match any id)
        Place place = new Place();
        place.setId(42L);
        when(placeRepo.findById(anyLong())).thenReturn(Optional.of(place));

        // no existing placeStats for (daily, place)
        when(placeStatsRepo.findByDailyStatsAndPlace(any(DailyStats.class), any(Place.class)))
                .thenReturn(Optional.empty());
        when(placeStatsRepo.save(any(PlaceStats.class))).thenAnswer(inv -> inv.getArgument(0));

        LocalDate out = service.addPlaceVisit(42L);

        assertEquals(today, out);
    }

    @Test
    void addPlaceVisit_placeMissing() {
        when(dailyStatsRepo.findByDate(any(LocalDate.class))).thenReturn(Optional.empty());
        when(dailyStatsRepo.save(any(DailyStats.class))).thenAnswer(inv -> inv.getArgument(0));
        when(placeRepo.findById(anyLong())).thenReturn(Optional.empty());

        LocalDate out = service.addPlaceVisit(999L);

        assertEquals(today, out);
        verify(placeStatsRepo, never()).save(any());
    }

    @Test
    void addPlaceVisit_incrementsExisting() {
        DailyStats daily = new DailyStats();
        daily.setDate(today);
        when(dailyStatsRepo.findByDate(any(LocalDate.class))).thenReturn(Optional.of(daily));

        Place place = new Place();
        place.setId(7L);
        when(placeRepo.findById(anyLong())).thenReturn(Optional.of(place));

        PlaceStats existing = new PlaceStats();
        existing.setDailyStats(daily);
        existing.setPlace(place);
        existing.setNumberOfPageVisits(2);

        when(placeStatsRepo.findByDailyStatsAndPlace(any(DailyStats.class), any(Place.class)))
                .thenReturn(Optional.of(existing));
        when(placeStatsRepo.save(any(PlaceStats.class))).thenAnswer(inv -> inv.getArgument(0));

        LocalDate out = service.addPlaceVisit(7L);

        assertEquals(today, out);
    }

    // ---------------- getByDate / getPlaceStats ----------------
    @Test
    void getByDate_delegates() {
        DailyStats ds = new DailyStats();
        ds.setDate(today);
        when(dailyStatsRepo.findByDate(any(LocalDate.class))).thenReturn(Optional.of(ds));

        assertEquals(today, service.getByDate(today).get().getDate());
        verify(dailyStatsRepo).findByDate(any(LocalDate.class));
    }

    @Test
    void getPlaceStats_delegates() {
        PlaceStats ps = new PlaceStats();
        when(placeStatsRepo.findByDateAndPlaceId(any(LocalDate.class), anyLong()))
                .thenReturn(Optional.of(ps));

        assertEquals(ps, service.getPlaceStats(today, 5L).get());
        verify(placeStatsRepo).findByDateAndPlaceId(any(LocalDate.class), anyLong());
    }

    // ---------------- getDailyVisitCounts ----------------
    @Test
    void getDailyVisitCounts_prefillAndOverlay() {
        long placeId = 77L;
        YearMonth month = YearMonth.from(today);

        PlaceStats ps1 = new PlaceStats();
        DailyStats ds1 = new DailyStats();
        ds1.setDate(month.atDay(3));
        ps1.setDailyStats(ds1);
        ps1.setNumberOfPageVisits(3);

        PlaceStats ps2 = new PlaceStats();
        DailyStats ds2 = new DailyStats();
        ds2.setDate(month.atDay(11));
        ps2.setDailyStats(ds2);
        ps2.setNumberOfPageVisits(8);

        when(placeStatsRepo.findByPlace_IdAndDailyStats_DateBetween(
                        anyLong(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(List.of(ps1, ps2));

        Map<LocalDate, Integer> out = service.getDailyVisitCounts(placeId, month);

        assertEquals(month.lengthOfMonth(), out.size());
        assertEquals(3, out.get(month.atDay(3)));
        assertEquals(8, out.get(month.atDay(11)));
    }
}
