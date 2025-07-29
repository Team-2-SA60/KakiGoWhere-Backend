package team2.kakigowherebackend.model;

import jakarta.persistence.*;
import java.time.LocalTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private String description;
    private String imagePath;
    private String URL;
    private LocalTime openingHour;
    private LocalTime closingHour;
    private double latitude;
    private double longitude;
    private boolean active;

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

    public Place() {}

    public Place(
            String name,
            String description,
            String URL,
            LocalTime openingHour,
            LocalTime closingHour,
            double latitude,
            double longitude,
            boolean active) {
        this.name = name;
        this.description = description;
        this.URL = URL;
        this.openingHour = openingHour;
        this.closingHour = closingHour;
        this.latitude = latitude;
        this.longitude = longitude;
        this.active = active;
    }

}
