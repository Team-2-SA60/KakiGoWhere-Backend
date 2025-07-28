package team2.kakigowherebackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
public class Itinerary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
    private LocalDate startDate;

    @OneToOne(mappedBy = "itinerary")
    private Tourist tourist;

    @OneToMany(mappedBy = "itinerary")
    private List<ItineraryDetail> itineraryDetails;

    public Itinerary() { }
    public Itinerary(String title, LocalDate startDate) {
        this.title = title;
        this.startDate = startDate;
    }
}
