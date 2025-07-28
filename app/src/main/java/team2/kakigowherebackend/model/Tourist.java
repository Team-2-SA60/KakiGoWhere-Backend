package team2.kakigowherebackend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Tourist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String email;
    private String password;
    private String name;

    @OneToOne private Itinerary itinerary;

    @ManyToMany
    @Size(max = 3)
    @JoinTable(
            name = "tourist_interests",
            joinColumns = @JoinColumn(name = "tourist_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<InterestCategory> interestCategories;

    @OneToMany
    @JoinColumn(name = "tourist_id")
    private List<Bookmark> bookmarks;

    @OneToMany
    @JoinColumn(name = "tourist_id")
    private List<Rating> ratings;

    public Tourist() {}

    public Tourist(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }
}
