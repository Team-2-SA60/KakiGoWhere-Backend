package team2.kakigowherebackend;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.*;

import team2.kakigowherebackend.model.DailyStats;
import team2.kakigowherebackend.repository.DailyStatsRepository;
import team2.kakigowherebackend.service.StatServiceImpl;

class StatServiceTest {
    @Mock
    private DailyStatsRepository dailyStatsRepo;

    @InjectMocks
    private StatServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddVisit_ExistingRow_UpdatesAndSavesSameInstance() {
        LocalDate today = LocalDate.now();
        DailyStats existing = new DailyStats();
        existing.setDate(today);

        when(dailyStatsRepo.findByDate(today)).thenReturn(Optional.of(existing));
        when(dailyStatsRepo.save(any(DailyStats.class))).thenAnswer(inv -> inv.getArgument(0));

        LocalDate result = service.addVisit();

        assertEquals(today, result); // returns today's date
        // ensure repo saves the same instance
        verify(dailyStatsRepo).save(same(existing));
    }


    @Test
    void testAddVisit_MissingRow_CreatesNewWithTodayAndSaves() {
        LocalDate today = LocalDate.now();
        when(dailyStatsRepo.findByDate(today)).thenReturn(Optional.empty());
        when(dailyStatsRepo.save(any(DailyStats.class))).thenAnswer(inv -> inv.getArgument(0));

        LocalDate result = service.addVisit();

        assertEquals(today, result); // returns today's date
        // ensure the saved entity has its date set to today
        verify(dailyStatsRepo).save(argThat(ds -> ds != null && today.equals(ds.getDate())));
    }

    @Test
    void testAddSignUp_ExistingRow_UpdatesAndSavesSameInstance() {
        LocalDate today = LocalDate.now();
        DailyStats existing = new DailyStats();
        existing.setDate(today);

        when(dailyStatsRepo.findByDate(today)).thenReturn(Optional.of(existing));
        when(dailyStatsRepo.save(any(DailyStats.class))).thenAnswer(inv -> inv.getArgument(0));

        LocalDate result = service.addSignUp();

        assertEquals(today, result);
        verify(dailyStatsRepo).save(same(existing));
    }

    @Test
    void testAddSignUp_MissingRow_CreatesNewWithTodayAndSaves() {
        LocalDate today = LocalDate.now();

        when(dailyStatsRepo.findByDate(today)).thenReturn(Optional.empty());
        when(dailyStatsRepo.save(any(DailyStats.class))).thenAnswer(inv -> inv.getArgument(0));

        LocalDate result = service.addSignUp();

        assertEquals(today, result);
        verify(dailyStatsRepo).save(argThat(ds -> ds != null && today.equals(ds.getDate())));
    }

    @Test
    void testGetByDate_Passthrough() {
        LocalDate date = LocalDate.of(2025, 8, 9);
        DailyStats row = new DailyStats();
        row.setDate(date);

        when(dailyStatsRepo.findByDate(date)).thenReturn(Optional.of(row));

        Optional<DailyStats> out = service.getByDate(date);

        assertTrue(out.isPresent());
        assertEquals(date, out.get().getDate());
        // verify that the service called the method (void return)
        verify(dailyStatsRepo).findByDate(date);
    }
}