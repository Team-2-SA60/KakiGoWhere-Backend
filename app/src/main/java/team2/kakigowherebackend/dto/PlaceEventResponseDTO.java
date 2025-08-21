package team2.kakigowherebackend.dto;

import java.time.LocalDate;
import lombok.Data;

// send data back to client
@Data
public class PlaceEventResponseDTO {
    private Long id;
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;

    private Long placeId;
    private String placeName;

    public PlaceEventResponseDTO() {}

    public PlaceEventResponseDTO(
            Long id,
            String name,
            String description,
            LocalDate startDate,
            LocalDate endDate,
            Long placeId,
            String placeName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.placeId = placeId;
        this.placeName = placeName;
    }
}
