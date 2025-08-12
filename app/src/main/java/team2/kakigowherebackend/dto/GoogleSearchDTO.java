package team2.kakigowherebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GoogleSearchDTO {
    private String googleId;
    private String name;
    private String address;
    private String googleMapsUri;
}
