package team2.kakigowherebackend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import team2.kakigowherebackend.model.Place;
import team2.kakigowherebackend.service.PlaceService;

@SpringBootApplication
public class KakiGoWhereBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(KakiGoWhereBackendApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRun(PlaceService placeService) {
        return args -> {
            Place place1 = new Place();
            place1.setName("Resorts World Sentosa");
            place1.setDescription(
                    "Experience endless excitement at Resorts World Sentosa with a variety of"
                            + " attractions and entertainment hot spots.");
            place1.setImagePath("resorts_world_sentosa.jpg");
            place1.setURL(
                    "https://www.visitsingapore.com/neighbourhood/featured-neighbourhood/sentosa-island/resorts-world-sentosa/");
            //            place1.setOpeningHour(LocalTime.of(8, 0));
            //            place1.setClosingHour(LocalTime.of(22, 0));
            place1.setLatitude(1.257);
            place1.setLongitude(103.82033);
            place1.setActive(true);
            placeService.savePlace(place1);

            Place place2 = new Place();
            place2.setName("Universal Studios Singapore");
            place2.setDescription(
                    "Step into the glamorous world of movies at this world-class theme park located"
                            + " within Resorts World Sentosa.");
            place2.setImagePath("universal_studios_singapore.jpg");
            place2.setURL(
                    "https://www.visitsingapore.com/travel-tips/travelling-to-singapore/itineraries/places-to-visit-with-family/");
            //            place2.setOpeningHour(LocalTime.of(8, 0));
            //            place2.setClosingHour(LocalTime.of(20, 0));
            place2.setLatitude(1.254);
            place2.setLongitude(103.823808);
            place2.setActive(true);
            placeService.savePlace(place2);

            Place place3 = new Place();
            place3.setName("Henderson Waves Bridge");
            place3.setDescription(
                    "With the Henderson Waves bridge, form meets function to stunning effect.");
            place3.setImagePath("henderson_waves_bridge.jpg");
            place3.setURL(
                    "https://www.visitsingapore.com/things-to-do/urban-wellness/green-spaces/");
            //            place3.setOpeningHour(LocalTime.of(9, 0));
            //            place3.setClosingHour(LocalTime.of(18, 0));
            place3.setLatitude(1.276);
            place3.setLongitude(103.815254);
            place3.setActive(true);
            placeService.savePlace(place3);
        };
    }
}
