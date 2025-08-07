package team2.kakigowherebackend.service;

import java.time.LocalDate;
import java.util.Map;

public interface PlaceVisitService {

    void recordVisit(long touristId, long placeId, LocalDate date);

    // return no. of visits per month (first day of mth)
    Map<LocalDate, Integer> getDailyVisitCounts(long placeId, LocalDate month);

}