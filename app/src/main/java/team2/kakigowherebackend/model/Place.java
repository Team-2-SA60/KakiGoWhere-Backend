package team2.kakigowherebackend.model;

import jakarta.persistence.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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
    private String address;
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

    public Place(String name, String googleId, String description, String URL) {
        this.name = name;
        this.googleId = googleId;
        this.description = description;
        this.URL = URL;
    }

    // For copying of Place obj
    public Place(Place otherPlace) {
        this.id = otherPlace.getId();
        this.googleId = otherPlace.getGoogleId();
        this.name = otherPlace.getName();
        this.description = otherPlace.getDescription();
        this.address = otherPlace.getAddress();
        this.URL = otherPlace.getURL();
        this.imagePath = otherPlace.getImagePath();
        this.openingDescription = otherPlace.getOpeningDescription();
        this.latitude = otherPlace.getLatitude();
        this.longitude = otherPlace.getLongitude();
        this.active = otherPlace.isActive();
        this.interestCategories =
                otherPlace.getInterestCategories() != null
                        ? new ArrayList<>(otherPlace.getInterestCategories())
                        : new ArrayList<>();
    }

    public Double getAverageRating() {
        if (ratings == null || ratings.isEmpty()) return null;
        return ratings.stream().mapToDouble(Rating::getRating).average().orElse(Double.NaN);
    }

    public boolean isOpen() {

        if (!isActive()) return false;

        ZoneId zoneId = ZoneId.of("Asia/Singapore");
        ZonedDateTime now = ZonedDateTime.now(zoneId);

        int currentDay = now.getDayOfWeek().getValue() % 7;
        int currentMinutes = now.getHour() * 60 + now.getMinute();

        if (openingHours == null || openingHours.isEmpty()) return true;

        for (OpeningHours oh : openingHours) {
            int openDay = oh.getOpenDay();
            int openHour = oh.getOpenHour();
            int openMinute = oh.getOpenMinute();
            int closeDay = oh.getCloseDay();
            int closeHour = oh.getCloseHour();
            int closeMinute = oh.getCloseMinute();

            if (openDay == 0 && openHour == 0 && closeDay == 0 && closeHour == 0) {
                return true;
            }

            int openMinutes = openHour * 60 + openMinute;
            int closeMinutes = closeHour * 60 + closeMinute;

            if (openDay == closeDay) {
                if (currentDay == openDay
                        && currentMinutes >= openMinutes
                        && currentMinutes < closeMinutes) {
                    return true;
                }
            } else {
                int openTotal = (openDay * 1440) + openMinutes;
                int closeTotal = (closeDay * 1440) + closeMinutes;
                int nowTotal = (currentDay * 1440) + currentMinutes;

                if (closeTotal <= openTotal) {
                    closeTotal += 7 * 1440;
                    if (nowTotal < openTotal) nowTotal += 7 * 1440;
                }

                if (nowTotal >= openTotal && nowTotal < closeTotal) {
                    return true;
                }
            }
        }
        return false;
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
                || !Objects.equals(address, place.address)
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
                id,
                name,
                URL,
                address,
                latitude,
                longitude,
                openingDescription,
                interestCategories,
                active);
    }
}
