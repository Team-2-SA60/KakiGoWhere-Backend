package team2.kakigowherebackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class ItineraryDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private LocalDate date;
    private String notes;
    private int sequentialOrder;

    @ManyToOne
    private Itinerary itinerary;

    @ManyToOne
    private Place place;

    public ItineraryDetail() { }
    public ItineraryDetail(LocalDate date, String notes, int sequentialOrder) {
        this.date = date;
        this.notes = notes;
        this.sequentialOrder = sequentialOrder;
    }
}
