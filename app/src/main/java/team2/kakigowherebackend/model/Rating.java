package team2.kakigowherebackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private int rating;
    private String comment;

    @ManyToOne private Place place;

    public Rating() {}

    public Rating(int rating, String comment) {
        this.rating = rating;
        this.comment = comment;
    }
}
