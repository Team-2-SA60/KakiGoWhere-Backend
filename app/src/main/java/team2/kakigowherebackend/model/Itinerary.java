package team2.kakigowherebackend.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
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
        if (itineraryDetails == null || itineraryDetails.isEmpty()) return 0L;
        return itineraryDetails.getFirst().getPlace().getId();
    }

    public Long getDays() {
        if (itineraryDetails == null || itineraryDetails.isEmpty()) return 0L;
        LocalDate lastDate = itineraryDetails.getLast().getDate();
        return ChronoUnit.DAYS.between(startDate, lastDate) + 1;
    }
}
