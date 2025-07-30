package team2.kakigowherebackend.model;

import jakarta.persistence.*;
import java.util.List;
import lombok.*;

@Entity
@Data
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

    @OneToMany(mappedBy = "place")
    private List<ItineraryDetail> itineraryDetails;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Place place = (Place) o;

        if (!kmlId.equals(place.kmlId)
                || !name.equals(place.name)
                || !description.equals(place.description)
                || !URL.equals(place.URL)
                || latitude != place.latitude
                || longitude != place.longitude
                || !openingDescription.equals(place.openingDescription)
                || interestCategories.isEmpty()
                || interestCategories.size() != place.interestCategories.size()) {
            return false;
        }

        for (int i = 0; i < interestCategories.size(); i++) {
            if (!interestCategories.get(i).equals(place.interestCategories.get(i))) {
                return false;
            }
        }

        return true;
    }
}
