package team2.kakigowherebackend.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItineraryDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private LocalDate date;
    private String notes;
    private int sequentialOrder;

    @ManyToOne private Itinerary itinerary;

    @ManyToOne private Place place;

    public ItineraryDetail(LocalDate date, String notes, int sequentialOrder, Place place) {
        this.date = date;
        this.notes = notes;
        this.sequentialOrder = sequentialOrder;
        this.place = place;
    }
}
