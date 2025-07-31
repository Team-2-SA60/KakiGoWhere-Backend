package team2.kakigowherebackend.dto;

import java.util.List;
import lombok.Data;
import team2.kakigowherebackend.model.*;

@Data
public class PlaceDTO {
    private long id;
    private String googleId;
    private String name;
    private String description;
    private String imagePath;
    private String URL;
    private String openingDescription;
    private double latitude;
    private double longitude;
    private boolean active;
    private List<InterestCategory> interestCategories;
    private List<OpeningHours> openingHours;
    private List<PlaceEvent> placeEvents;
    private Double averageRating;

    public PlaceDTO(Place place) {
        this.id = place.getId();
        this.googleId = place.getGoogleId();
        this.name = place.getName();
        this.description = place.getDescription();
        this.imagePath = place.getImagePath();
        this.URL = place.getURL();
        this.openingDescription = place.getOpeningDescription();
        this.latitude = place.getLatitude();
        this.longitude = place.getLongitude();
        this.active = place.isActive();
        this.interestCategories = place.getInterestCategories();
        this.openingHours = place.getOpeningHours();
        this.placeEvents = place.getPlaceEvents();
        this.averageRating = place.getAverageRating();
    }
}
