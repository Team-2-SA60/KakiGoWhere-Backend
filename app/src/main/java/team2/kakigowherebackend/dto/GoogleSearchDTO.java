package team2.kakigowherebackend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GoogleSearchDTO {
    @NotBlank private String googleId;
    @NotBlank private String name;
    @NotBlank private String address;
    @NotBlank private String googleMapsUri;
}
