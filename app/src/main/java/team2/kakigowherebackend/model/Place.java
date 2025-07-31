package team2.kakigowherebackend.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String googleId;
    private String name;
    @Lob private String description;
    private String imagePath;
    private String URL;
    @Lob private String openingDescription;
    private double latitude;
    private double longitude;
    private boolean active;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "place_id")
    private List<OpeningHours> openingHours;

    @ManyToMany
    @JoinTable(
            name = "place_interests",
            joinColumns = @JoinColumn(name = "place_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<InterestCategory> interestCategories;

    @OneToMany(mappedBy = "place")
    private List<PlaceEvent> placeEvents;

    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL)
    private List<Rating> ratings;

    public Place(String name, String googleId) {
        this.name = name;
        this.googleId = googleId;
    }

    // For copying of Place obj
    public Place(Place otherPlace) {
        this.id = otherPlace.getId();
        this.googleId = otherPlace.getGoogleId();
        this.name = otherPlace.getName();
        this.description = otherPlace.getDescription();
        this.URL = otherPlace.getURL();
        this.openingDescription = otherPlace.getOpeningDescription();
        this.latitude = otherPlace.getLatitude();
        this.longitude = otherPlace.getLongitude();
        this.active = otherPlace.isActive();
        this.interestCategories =
                otherPlace.getInterestCategories() != null
                        ? new ArrayList<>(otherPlace.getInterestCategories())
                        : new ArrayList<>();
    }

    // To compare a Place to another Place, for scheduler to determine if need to save an updated
    // Place
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Place place = (Place) o;

        if (!Objects.equals(name, place.name)
                || !Objects.equals(URL, place.URL)
                || latitude != place.latitude
                || longitude != place.longitude
                || active != place.active
                || !Objects.equals(openingDescription, place.openingDescription)) {
            return false;
        }

        if (interestCategories.size() != place.interestCategories.size()) return false;
        for (InterestCategory cat : interestCategories) {
            if (!place.interestCategories.contains(cat)) return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                id, name, URL, latitude, longitude, openingDescription, interestCategories, active);
    }
}
