package team2.kakigowherebackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class InterestCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private String description;

    public InterestCategory() {}

    public InterestCategory(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
