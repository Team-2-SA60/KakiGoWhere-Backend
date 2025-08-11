package team2.kakigowherebackend.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import team2.kakigowherebackend.model.DailyStats;
import team2.kakigowherebackend.model.Place;
import team2.kakigowherebackend.model.PlaceStats;

@Repository
public interface PlaceStatsRepository extends JpaRepository<PlaceStats, Long> {

    @Query(
            """
           SELECT ps
           FROM PlaceStats ps
           WHERE ps.dailyStats = :daily
             AND ps.place = :place
           """)
    Optional<PlaceStats> findByDailyStatsAndPlace(
            @Param("daily") DailyStats daily, @Param("place") Place place);

    @Query(
            """
           SELECT ps
           FROM PlaceStats ps
           JOIN ps.dailyStats d
           WHERE d.date = :date
           AND ps.place.id = :placeId
            """)
    Optional<PlaceStats> findByDateAndPlaceId(
            @Param("date") LocalDate date, @Param("placeId") Long placeId);

    List<PlaceStats> findByPlace_IdAndDailyStats_DateBetween(
            long placeId, LocalDate start, LocalDate end);
}
