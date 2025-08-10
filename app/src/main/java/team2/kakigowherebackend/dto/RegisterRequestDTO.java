package team2.kakigowherebackend.dto;

import java.util.List;
import lombok.Data;

@Data
public class RegisterRequestDTO {
    private String name;
    private String email;
    private String password;
    private List<Long> interestCategoryIds;

    public RegisterRequestDTO(
            String name, String email, String password, List<Long> interestCategoryIds) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.interestCategoryIds = interestCategoryIds;
    }
}
