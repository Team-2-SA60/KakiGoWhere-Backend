package team2.kakigowherebackend.service;

import java.time.LocalDate;
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

        DailyStats dailyStats = dailyStatsRepo.findByDate(today);

        if (dailyStats == null) {
            dailyStats = new DailyStats();
            dailyStats.setDate(today);
        }
        dailyStats.addUniqueVisits();

        dailyStatsRepo.save(dailyStats);

        return dailyStats.getDate();
    }
}
