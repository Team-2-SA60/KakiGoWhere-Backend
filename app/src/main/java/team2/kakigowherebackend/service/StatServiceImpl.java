package team2.kakigowherebackend.service;

import java.time.LocalDate;
import java.util.Optional;
import org.springframework.stereotype.Service;
import team2.kakigowherebackend.model.DailyStats;
import team2.kakigowherebackend.repository.DailyStatsRepository;

@Service
public class StatServiceImpl implements StatService {

    private final DailyStatsRepository dailyStatsRepo;

    public StatServiceImpl(DailyStatsRepository dailyStatsRepo) {
        this.dailyStatsRepo = dailyStatsRepo;
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
    public Optional<DailyStats> getByDate(LocalDate date) {
        return dailyStatsRepo.findByDate(date);
    }
}
