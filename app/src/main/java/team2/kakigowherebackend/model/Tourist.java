package team2.kakigowherebackend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Tourist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long touristId;
    private String touristEmail;
    private String touristPassword;
    private String touristName;

    public Tourist() { }
    public Tourist(String touristEmail, String touristPassword, String touristName) {
        this.touristEmail = touristEmail;
        this.touristPassword = touristPassword;
        this.touristName = touristName;
    }
}
