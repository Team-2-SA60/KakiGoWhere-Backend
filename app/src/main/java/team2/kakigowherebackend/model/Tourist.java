package team2.kakigowherebackend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
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

    @CreatedDate
    @Column(
            nullable = false,
            updatable = false,
            columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private Instant createdDate;

    @LastModifiedDate
    @Column(
            nullable = false,
            columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Instant lastModifiedDate;

    public Tourist(String email, String password, String name) {
        super(email, password, name);
    }
}
