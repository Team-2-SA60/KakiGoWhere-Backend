package team2.kakigowherebackend.dto;

import lombok.Data;

@Data
public class RatingSummaryDTO {
    private double averageRating;
    private int ratingCount;

    public RatingSummaryDTO(double averageRating, int ratingCount) {
        this.averageRating = averageRating;
        this.ratingCount = ratingCount;
    }
}
