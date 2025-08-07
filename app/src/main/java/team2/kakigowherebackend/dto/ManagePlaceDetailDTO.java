package team2.kakigowherebackend.dto;

import java.util.List;
import lombok.Data;
import team2.kakigowherebackend.model.InterestCategory;
import team2.kakigowherebackend.model.OpeningHours;
import team2.kakigowherebackend.model.Place;

@Data
public class ManagePlaceDetailDTO {
    private long id;
    private String googleId;
    private String name;
    private String address;
    private String description;
    private String URL;
    private String openingDescription;
    private double latitude;
    private double longitude;
    private boolean isActive;
    private List<InterestCategory> interestCategories;
    private List<OpeningHours> openingHours;
    private boolean isAutoFetch;

    public ManagePlaceDetailDTO(Place place) {
        this.id = place.getId();
        this.googleId = place.getGoogleId();
        this.name = place.getName();
        this.address = place.getAddress();
        this.description = place.getDescription();
        this.URL = place.getURL();
        this.openingDescription = place.getOpeningDescription();
        this.latitude = place.getLatitude();
        this.longitude = place.getLongitude();
        this.isActive = place.isActive();
        this.interestCategories = place.getInterestCategories();
        this.openingHours = place.getOpeningHours();
        this.isAutoFetch = place.isAutoFetch();
    }
}
