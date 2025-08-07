package team2.kakigowherebackend.dto;

import lombok.Data;
import team2.kakigowherebackend.model.ItineraryDetail;

import java.time.LocalDate;

@Data
public class ItineraryDetailDTO {
    private long id;
    private LocalDate date = null;
    private String notes = "";
    private int sequentialOrder;
    private long placeId;
    private String placeTitle;
    private boolean placeIsOpen;
    private String placeOpenHours;

    public ItineraryDetailDTO(ItineraryDetail itineraryDetail) {
        this.id = itineraryDetail.getId();
        this.date = itineraryDetail.getDate();
        this.notes = itineraryDetail.getNotes();
        this.sequentialOrder = itineraryDetail.getSequentialOrder();
        this.placeId = itineraryDetail.getPlace().getId();
        this.placeTitle = itineraryDetail.getPlace().getName();
        this.placeIsOpen = itineraryDetail.getPlace().isOpen();
        this.placeOpenHours = itineraryDetail.getPlace().getTodayOpeningHours();
    }
}
