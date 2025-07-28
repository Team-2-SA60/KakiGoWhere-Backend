package team2.kakigowherebackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;

@Entity
@Getter
@Setter
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long placeId;
    private String placeName;
    private String placeDescription;
    private String placeImagePath;
    private String placeURL;
    private LocalTime placeOpeningHour;
    private LocalTime placeClosingHour;
    private double placeLatitude;
    private double placeLongitude;
    private boolean activeStatus;

    @ManyToMany
    @JoinTable(name = "place_interests",
    joinColumns = @JoinColumn(name = "place_id"),
    inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<InterestCategory> interestCategories;

    @OneToMany(mappedBy = "place")
    private List<PlaceEvent> placeEvents;

    @OneToMany(mappedBy = "place")
    private List<ItineraryDetail> itineraryDetails;

    public Place() { }
    public Place(String placeName, String placeDescription, String placeURL, LocalTime placeOpeningHour, LocalTime placeClosingHour, double placeLatitude, double placeLongitude, boolean activeStatus) {
        this.placeName = placeName;
        this.placeDescription = placeDescription;
        this.placeURL = placeURL;
        this.placeOpeningHour = placeOpeningHour;
        this.placeClosingHour = placeClosingHour;
        this.placeLatitude = placeLatitude;
        this.placeLongitude = placeLongitude;
        this.activeStatus = activeStatus;
    }
}
