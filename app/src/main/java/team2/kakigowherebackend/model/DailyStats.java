package team2.kakigowherebackend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class DailyStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long dailyStatsId;
    private LocalDate date;
    private int numberOfUniqueVisits;
    private int numberOfSignUps;

    public DailyStats() { }
    public DailyStats(LocalDate date, int numberOfUniqueVisits, int numberOfSignUps) {
        this.date = date;
        this.numberOfUniqueVisits = numberOfUniqueVisits;
        this.numberOfSignUps = numberOfSignUps;
    }

}
