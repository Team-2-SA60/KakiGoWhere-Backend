package team2.kakigowherebackend.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import team2.kakigowherebackend.model.TouristPlaceVisit;

public interface TouristPlaceVisitRepository extends JpaRepository<TouristPlaceVisit, Long> {

    boolean existsByTouristIdAndPlaceIdAndVisitDate(
            Long touristId, Long placeId, LocalDate visitDate);

    // uses SQL YEAR() and MONTH() fn on visitDate field, return [visitDate, count]
    @Query(
            """
        SELECT v.visitDate AS date, COUNT(v) AS cnt
          FROM TouristPlaceVisit v
         WHERE v.place.id = :placeId
           AND FUNCTION('YEAR', v.visitDate)  = :year
           AND FUNCTION('MONTH', v.visitDate) = :month
         GROUP BY v.visitDate
         ORDER BY v.visitDate
    """)
    List<Object[]> findByPlaceAndMonth(
            @Param("placeId") Long placeId, @Param("year") int year, @Param("month") int month);
}
