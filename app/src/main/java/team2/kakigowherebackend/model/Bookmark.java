package team2.kakigowherebackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Bookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long bookmarkId;
    private LocalDate bookmarkDate;

    @ManyToOne
    private Place place;

    public Bookmark() { }
    public Bookmark(LocalDate bookmarkDate) {
        this.bookmarkDate = bookmarkDate;
    }
}
