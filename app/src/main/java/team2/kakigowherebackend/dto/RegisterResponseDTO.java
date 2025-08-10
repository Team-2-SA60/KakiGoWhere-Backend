package team2.kakigowherebackend.dto;

import java.util.List;
import lombok.Data;
import team2.kakigowherebackend.model.InterestCategory;

@Data
public class RegisterResponseDTO {
    private Long id;
    private String name;
    private String email;
    private List<InterestCategory> interestCategories;

    public RegisterResponseDTO(
            Long id, String name, String email, List<InterestCategory> interestCategories) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.interestCategories = interestCategories;
    }
}
