package team2.kakigowherebackend.service;

import java.time.LocalDate;
import java.util.Optional;
import team2.kakigowherebackend.model.DailyStats;

public interface StatService {
    LocalDate addVisit();
    LocalDate addSignUp();
    Optional<DailyStats> getByDate(LocalDate date);
}
