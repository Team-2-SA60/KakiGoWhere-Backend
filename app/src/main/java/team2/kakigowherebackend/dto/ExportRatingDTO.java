package team2.kakigowherebackend.dto;

import lombok.Data;
import team2.kakigowherebackend.model.Rating;

@Data
public class ExportRatingDTO {
    private long id;
    private int rating;
    private String comment;
    private long placeId;
    private long touristId;

    public ExportRatingDTO(Rating rating) {
        this.id = rating.getId();
        this.rating = rating.getRating();
        this.comment = rating.getComment();
        this.placeId = rating.getPlace().getId();
        this.touristId = rating.getTourist().getId();
    }
}
