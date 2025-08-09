package team2.kakigowherebackend.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import team2.kakigowherebackend.model.InterestCategory;
import team2.kakigowherebackend.model.Tourist;
import team2.kakigowherebackend.model.User;

@Data
public class UserDTO {
    private long id;
    private String email;
    private String name;
    private String role;
    private List<InterestCategory> interestCategories;

    public UserDTO(User user, String role) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.role = role;
        this.interestCategories = new ArrayList<>();
        if (user instanceof Tourist) {
            this.interestCategories = ((Tourist) user).getInterestCategories();
        }
    }
}
