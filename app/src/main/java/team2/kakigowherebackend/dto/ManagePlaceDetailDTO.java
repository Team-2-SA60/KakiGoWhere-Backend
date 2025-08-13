package team2.kakigowherebackend.dto;

import jakarta.validation.constraints.*;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import team2.kakigowherebackend.model.InterestCategory;
import team2.kakigowherebackend.model.OpeningHours;
import team2.kakigowherebackend.model.Place;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManagePlaceDetailDTO {
    private long id;
    private String googleId;

    @NotBlank(message = "Name cannot be empty")
    @Size(max = 255, message = "Name cannot exceed 255 characters")
    private String name;

    @NotBlank(message = "Address cannot be empty")
    @Size(max = 255, message = "Address cannot exceed 255 characters")
    private String address;

    private String description;
    private String URL;
    private String openingDescription;

    @NotNull(message = "Latitude is required")
    @DecimalMin(
            value = "1.100000",
            inclusive = true,
            message = "Latitude not in Singapore (1.1 - 1.5)")
    @DecimalMax(
            value = "1.500000",
            inclusive = true,
            message = "Latitude not in Singapore (1.1 - 1.5)")
    private double latitude;

    @NotNull(message = "Longitude is required")
    @DecimalMin(
            value = "103.500000",
            inclusive = true,
            message = "Longitude not in Singapore (103.5 - 104.1)")
    @DecimalMax(
            value = "104.100000",
            inclusive = true,
            message = "Longitude not in Singapore (103.5 - 104.1)")
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
