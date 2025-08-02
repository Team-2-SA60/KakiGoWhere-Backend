package team2.kakigowherebackend.model;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Admin extends User {
    public Admin(String email, String password, String name) {
        super(email, password, name);
    }
}
