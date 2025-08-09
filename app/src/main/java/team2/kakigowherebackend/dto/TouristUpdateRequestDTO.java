package team2.kakigowherebackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TouristUpdateRequestDTO {
    @NotBlank
    private String name;

    @Size(max = 3)
    private List<Long> interestCategoryIds;

}