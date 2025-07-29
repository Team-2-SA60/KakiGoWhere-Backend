package team2.kakigowherebackend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
public class PlaceDTO {
    private long id;
    private String name;
    private String description;
    private PlaceImageDTO image;
    private String URL;
    private LocalTime openingHour;
    private LocalTime closingHour;
    private double latitude;
    private double longitude;
    private boolean active;

    // inner class for imageDTO
    @Getter
    @Setter
    @NoArgsConstructor
    public static class PlaceImageDTO {
        private String path;
        private byte[] data;
        private String mimeType;
    }

}
