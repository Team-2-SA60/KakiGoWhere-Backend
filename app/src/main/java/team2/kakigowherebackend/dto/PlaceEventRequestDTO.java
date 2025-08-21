package team2.kakigowherebackend.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import lombok.Data;

// payload for both create and update
@Data
public class PlaceEventRequestDTO {

    @NotBlank(message = "Event name is required")
    @Size(max = 100, message = "Event name is too long")
    private String name;

    @NotNull(message = "Place is required")
    private Long placeId;

    @Size(max = 300, message = "Description is too long")
    private String description;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    public PlaceEventRequestDTO() {}

    public PlaceEventRequestDTO(
            String name, Long placeId, String description, LocalDate startDate, LocalDate endDate) {
        this.name = name;
        this.placeId = placeId;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
