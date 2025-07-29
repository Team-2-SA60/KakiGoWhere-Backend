package team2.kakigowherebackend.model;

import jakarta.persistence.*;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String kmlId;
    private String name;
    private String description;
    private String imagePath;
    private String URL;
    private String openingDescription;
    private double latitude;
    private double longitude;
    private boolean activeStatus;

    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OpeningHours> openingHours;

    @ManyToMany
    @JoinTable(
            name = "place_interests",
            joinColumns = @JoinColumn(name = "place_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<InterestCategory> interestCategories;

    @OneToMany(mappedBy = "place")
    private List<PlaceEvent> placeEvents;

    @OneToMany(mappedBy = "place")
    private List<ItineraryDetail> itineraryDetails;
}
