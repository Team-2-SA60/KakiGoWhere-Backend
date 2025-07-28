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
    private long itineraryDetailId;
    private LocalDate itemDate;
    private String itemNotes;
    private int sequentialOrder;

    @ManyToOne
    private Itinerary itinerary;

    @ManyToOne
    private Place place;

    public ItineraryDetail() { }
    public ItineraryDetail(LocalDate itemDate, String itemNotes, int sequentialOrder) {
        this.itemDate = itemDate;
        this.itemNotes = itemNotes;
        this.sequentialOrder = sequentialOrder;
    }
}
