package team2.kakigowherebackend.dto;

import lombok.Data;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

// used when the user submits or edits rating
@Data
public class RatingRequestDTO {
    @Min(1)
    @Max(5)
    private int rating;
    @NotBlank(message = "Comment cannot be empty")
    private String comment;
    public RatingRequestDTO() {}
    public RatingRequestDTO(int rating, String comment) {
        this.rating = rating;
        this.comment = comment;
    }
}