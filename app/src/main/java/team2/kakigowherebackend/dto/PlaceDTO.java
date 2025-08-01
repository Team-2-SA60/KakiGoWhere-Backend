package team2.kakigowherebackend.dto;

import java.util.List;
import lombok.Data;
import team2.kakigowherebackend.model.InterestCategory;
import team2.kakigowherebackend.model.Place;

@Data
public class PlaceDTO {
    private long id;
    private String googleId;
    private String name;
    private Double latitude;
    private Double longitude;
    private boolean isActive;
    private boolean isOpen;
    private Double averageRating;
    private List<InterestCategory> interestCategories;

    public PlaceDTO(Place place) {
        this.id = place.getId();
        this.googleId = place.getGoogleId();
        this.name = place.getName();
        this.latitude = place.getLatitude();
        this.longitude = place.getLongitude();
        this.isActive = place.isActive();
        this.isOpen = place.isOpen();
        this.averageRating = place.getAverageRating();
        this.interestCategories = place.getInterestCategories();
    }
}
