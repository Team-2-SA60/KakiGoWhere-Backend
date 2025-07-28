package team2.kakigowherebackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class PlaceStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long placeStatsId;
    private int numberOfBookmarks;
    private int numberOfItinerary;

    @ManyToOne
    private Place place;

    @ManyToOne
    private DailyStats dailyStats;

    public PlaceStats() { }
    public PlaceStats(int numberOfBookmarks, int numberOfItinerary) {
        this.numberOfBookmarks = numberOfBookmarks;
        this.numberOfItinerary = numberOfItinerary;
    }
}