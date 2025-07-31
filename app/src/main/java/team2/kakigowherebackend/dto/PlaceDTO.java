package team2.kakigowherebackend.dto;

import lombok.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaceDTO {
    private long id;
    private String googleId;
    private String name;
    private String description;
    private List<String> interestCategories;
}