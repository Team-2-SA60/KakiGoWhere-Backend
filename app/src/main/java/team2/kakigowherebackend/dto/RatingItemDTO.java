package team2.kakigowherebackend.dto;

import lombok.Data;

@Data
public class RatingItemDTO {
    private long ratingId;
    private long touristId;
    private String touristName;
    private int rating;
    private String comment;

    public RatingItemDTO(
            long ratingId, long touristId, String touristName, int rating, String comment) {
        this.ratingId = ratingId;
        this.touristId = touristId;
        this.touristName = touristName;
        this.rating = rating;
        this.comment = comment;
    }
}
