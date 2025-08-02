package team2.kakigowherebackend.dto;

import lombok.Data;
import team2.kakigowherebackend.model.User;

@Data
public class UserDTO {
    private long id;
    private String email;
    private String name;
    private String role;

    public UserDTO(User user, String role) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.role = role;
    }
}
