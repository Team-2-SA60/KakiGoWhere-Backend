package team2.kakigowherebackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Itinerary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;
    private LocalDate startDate;

    @ManyToOne private Tourist tourist;

    @OneToMany(mappedBy = "itinerary", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItineraryDetail> itineraryDetails;

    public Itinerary(String title, LocalDate startDate) {
        this.title = title;
        this.startDate = startDate;
    }

    // methods

    public Long getDisplayImageId() {
        if (itineraryDetails == null || itineraryDetails.isEmpty()) return null;
        return itineraryDetails.getFirst().getPlace().getId();
    }

    public Long getDays() {
        if (itineraryDetails == null || itineraryDetails.isEmpty()) return null;
        LocalDate lastDate = itineraryDetails.getLast().getDate();
        return ChronoUnit.DAYS.between(startDate, lastDate);
    }
}
