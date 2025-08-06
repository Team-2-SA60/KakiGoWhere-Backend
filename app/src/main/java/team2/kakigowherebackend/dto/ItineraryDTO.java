package team2.kakigowherebackend.dto;

import java.time.LocalDate;
import lombok.Data;
import team2.kakigowherebackend.model.Itinerary;

@Data
public class ItineraryDTO {
    private long id;
    private String title;
    private LocalDate startDate;
    private long days;
    private long placeDisplayId;

    public ItineraryDTO(Itinerary itinerary) {
        this.id = itinerary.getId();
        this.title = itinerary.getTitle();
        this.startDate = itinerary.getStartDate();
        this.days = itinerary.getDays();
        this.placeDisplayId = itinerary.getDisplayImageId();
    }
}
