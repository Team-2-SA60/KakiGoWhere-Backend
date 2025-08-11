package team2.kakigowherebackend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_placestats_day_place",
                        columnNames = {"daily_stats_id", "place_id"}
                )
        }
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlaceStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Min(0)
    private int numberOfBookmarks;

    @Min(0)
    private int numberOfItinerary;

    @Min(0)
    private int numberOfPageVisits;

    @NotNull
    @ManyToOne (optional = false)
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "daily_stats_id", nullable = false)
    private DailyStats dailyStats;
}
