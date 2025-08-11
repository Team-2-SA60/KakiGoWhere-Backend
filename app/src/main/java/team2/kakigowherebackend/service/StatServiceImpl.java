package team2.kakigowherebackend.service;

import java.time.LocalDate;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team2.kakigowherebackend.model.DailyStats;
import team2.kakigowherebackend.model.Place;
import team2.kakigowherebackend.model.PlaceStats;
import team2.kakigowherebackend.repository.DailyStatsRepository;
import team2.kakigowherebackend.repository.PlaceRepository;
import team2.kakigowherebackend.repository.PlaceStatsRepository;

@Service
public class StatServiceImpl implements StatService {

    private final DailyStatsRepository dailyStatsRepo;
    private final PlaceStatsRepository placeStatsRepo;
    private final PlaceRepository placeRepo;

    public StatServiceImpl(DailyStatsRepository dailyStatsRepo, PlaceStatsRepository placeStatsRepo, PlaceRepository placeRepo) {
        this.dailyStatsRepo = dailyStatsRepo;
        this.placeStatsRepo = placeStatsRepo;
        this.placeRepo = placeRepo;
    }

    @Override
    public LocalDate addVisit() {
        LocalDate today = LocalDate.now();

        DailyStats dailyStats = dailyStatsRepo.findByDate(today).orElse(null);

        if (dailyStats == null) {
            dailyStats = new DailyStats();
            dailyStats.setDate(today);
        }
        dailyStats.addUniqueVisits();

        dailyStatsRepo.save(dailyStats);

        return dailyStats.getDate();
    }

    @Override
    public LocalDate addSignUp() {
        LocalDate today = LocalDate.now();
        DailyStats dailyStats = dailyStatsRepo.findByDate(today).orElse(null);

        if (dailyStats == null) {
            dailyStats = new DailyStats();
            dailyStats.setDate(today);
        }

        dailyStats.addSignUps();
        dailyStatsRepo.save(dailyStats);

        return today;
    }

    @Override
    @Transactional
    public LocalDate addPlaceVisit(Long placeId) {
        LocalDate today = LocalDate.now();
        DailyStats dailyStats = dailyStatsRepo.findByDate(today).orElse(null);
        if(dailyStats == null) {
            dailyStats = new DailyStats();
            dailyStats.setDate(today);
            dailyStatsRepo.save(dailyStats);
        }

        Place place = placeRepo.findById(placeId).orElse(null);
        if(place == null) {
            return today;
        }

        PlaceStats placeStats = placeStatsRepo.findByDailyStatsAndPlace(dailyStats, place).orElse(null);
        if (placeStats == null) {
            placeStats = new PlaceStats();
            placeStats.setDailyStats(dailyStats);
            placeStats.setPlace(place);
        }

        placeStats.setNumberOfPageVisits(placeStats.getNumberOfPageVisits() + 1);
        placeStatsRepo.save(placeStats);

        return today;
    }

    @Override
    public Optional<DailyStats> getByDate(LocalDate date) {
        return dailyStatsRepo.findByDate(date);
    }

    @Override
    public Optional<PlaceStats> getPlaceStats(LocalDate date, Long placeId) {
        return placeStatsRepo.findByDateAndPlaceId(date, placeId);
    }
}
