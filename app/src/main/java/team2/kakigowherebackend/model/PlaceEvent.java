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
public class PlaceEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long eventId;
    private String eventName;
    private String eventDescription;
    private LocalDate eventStartDate;
    private LocalDate eventEndDate;

    public PlaceEvent() { }
    public PlaceEvent(String eventName, String eventDescription, LocalDate eventStartDate, LocalDate eventEndDate) {
        this.eventName = eventName;
        this.eventDescription = eventDescription;
        this.eventStartDate = eventStartDate;
        this.eventEndDate = eventEndDate;
    }
}
