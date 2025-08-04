package team2.kakigowherebackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private int rating;
    @Lob private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    private Place place;
    @ManyToOne(fetch = FetchType.LAZY)
    private Tourist tourist;
}
