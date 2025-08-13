package team2.kakigowherebackend.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import team2.kakigowherebackend.service.RetrievePlaceService;

@Slf4j
@Component
public class RetrievePlaceScheduler {

    private final RetrievePlaceService rpService;

    public RetrievePlaceScheduler(RetrievePlaceService rpService) {
        this.rpService = rpService;
    }

    @Scheduled(cron = "0 0 0 ? * 6#3")
    public void retrievePlaces() {
        log.info("Starting scheduled retrieve places...");
        rpService.retrievePlaces();
        log.info("Finished scheduled retrieve places");
    }
}
