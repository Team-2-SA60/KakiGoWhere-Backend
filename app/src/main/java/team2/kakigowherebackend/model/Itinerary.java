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
public class Itinerary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long itineraryId;
    private String itineraryTitle;
    private LocalDate itineraryStartDate;

    public Itinerary() { }
    public Itinerary(String itineraryTitle, LocalDate itineraryStartDate) {
        this.itineraryTitle = itineraryTitle;
        this.itineraryStartDate = itineraryStartDate;
    }
}
