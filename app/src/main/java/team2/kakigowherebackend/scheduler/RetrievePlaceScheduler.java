package team2.kakigowherebackend.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import team2.kakigowherebackend.service.ExportPlaceService;
import team2.kakigowherebackend.service.RetrievePlaceService;
import team2.kakigowherebackend.service.RetrievePlaceServiceImpl;

@Slf4j
@Component
public class RetrievePlaceScheduler {

    private final RetrievePlaceService rpService;
    private final ExportPlaceService epService;

    public RetrievePlaceScheduler(
            RetrievePlaceServiceImpl rpService, ExportPlaceService epService) {
        this.rpService = rpService;
        this.epService = epService;
    }

    @Scheduled(cron = "0 0 0 ? * 6#3")
    public void retrievePlaces() {
        log.info("Starting scheduled retrieve places...");
        rpService.retrievePlaces();
        log.info("Finished scheduled retrieve places");

        log.info("Exporting places to CSV...");
        epService.exportPlaces();
        log.info("Finished exporting places to CSV");
    }
}
