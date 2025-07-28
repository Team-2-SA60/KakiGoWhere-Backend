package team2.kakigowherebackend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class KakiGoWhereBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(KakiGoWhereBackendApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRun() {
        return args -> {};
    }
}
