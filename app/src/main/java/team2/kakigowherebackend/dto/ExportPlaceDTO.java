package team2.kakigowherebackend.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import team2.kakigowherebackend.model.InterestCategory;
import team2.kakigowherebackend.model.Place;

@Data
public class ExportPlaceDTO {
    private long id;
    private String googleId;
    private String name;
    private String description;
    private Double averageRating;
    private List<String> interestCategories;

    public ExportPlaceDTO(Place place) {
        this.id = place.getId();
        this.googleId = place.getGoogleId();
        this.name = place.getName();
        this.description = place.getDescription();
        this.averageRating = place.getAverageRating();

        this.interestCategories = new ArrayList<>();
        if (place.getInterestCategories() != null) {
            for (InterestCategory interestCategory : place.getInterestCategories()) {
                if (interestCategory != null && interestCategory.getName() != null) {
                    this.interestCategories.add(interestCategory.getName());
                }
            }
        }
    }
}
