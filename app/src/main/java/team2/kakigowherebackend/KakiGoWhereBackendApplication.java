package team2.kakigowherebackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class KakiGoWhereBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(KakiGoWhereBackendApplication.class, args);
    }
}
