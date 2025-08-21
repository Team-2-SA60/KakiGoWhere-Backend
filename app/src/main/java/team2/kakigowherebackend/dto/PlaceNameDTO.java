package team2.kakigowherebackend.dto;

import lombok.Data;

@Data
public class PlaceNameDTO {
    private Long id;
    private String name;

    public PlaceNameDTO() {}

    public PlaceNameDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
