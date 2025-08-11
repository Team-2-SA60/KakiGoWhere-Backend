package team2.kakigowherebackend.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Map;
import java.util.Optional;
import team2.kakigowherebackend.model.DailyStats;
import team2.kakigowherebackend.model.PlaceStats;

public interface StatService {
    LocalDate addVisit();

    LocalDate addSignUp();

    LocalDate addPlaceVisit(Long placeId);

    Optional<DailyStats> getByDate(LocalDate date);

    Optional<PlaceStats> getPlaceStats(LocalDate date, Long placeId);

    Map<LocalDate, Integer> getDailyVisitCounts(long placeId, YearMonth month);
}
