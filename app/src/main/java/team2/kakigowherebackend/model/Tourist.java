package team2.kakigowherebackend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Tourist extends User {
    @OneToMany(mappedBy = "tourist")
    private List<Itinerary> itineraryList;

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

    public Tourist(String email, String password, String name) {
        super(email, password, name);
    }
}
