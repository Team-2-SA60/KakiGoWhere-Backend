package team2.kakigowherebackend.repository;

import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import team2.kakigowherebackend.model.DailyStats;

@Repository
public interface DailyStatsRepository extends JpaRepository<DailyStats, Long> {

    @Query("SELECT d FROM DailyStats d WHERE d.date = :date")
    DailyStats findByDate(@Param("date") LocalDate date);
}
