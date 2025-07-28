package team2.kakigowherebackend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Tourist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long touristId;
    private String touristEmail;
    private String touristPassword;
    private String touristName;

    @OneToOne
    private Itinerary itinerary;

    @ManyToMany
    @Size(max = 3)
    @JoinTable(name = "tourist_interests",
    joinColumns = @JoinColumn(name = "tourist_id"),
    inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<InterestCategory> interestCategories;

    @OneToMany
    @JoinColumn(name = "tourist_id")
    private List<Bookmark> bookmarks;

    @OneToMany
    @JoinColumn(name = "tourist_id")
    private List<Rating> ratings;

    public Tourist() { }
    public Tourist(String touristEmail, String touristPassword, String touristName) {
        this.touristEmail = touristEmail;
        this.touristPassword = touristPassword;
        this.touristName = touristName;
    }
}
