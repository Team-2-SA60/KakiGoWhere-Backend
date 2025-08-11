package team2.kakigowherebackend.dto;

import java.util.List;
import lombok.Data;
import team2.kakigowherebackend.model.InterestCategory;
import team2.kakigowherebackend.model.Tourist;

@Data
public class RegisterResponseDTO {
    private Long id;
    private String name;
    private String email;
    private List<InterestCategory> interestCategories;

    public RegisterResponseDTO(Tourist tourist) {
        this.id = tourist.getId();
        this.name = tourist.getName();
        this.email = tourist.getEmail();
        this.interestCategories = tourist.getInterestCategories();
    }
}
