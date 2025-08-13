package team2.kakigowherebackend.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import team2.kakigowherebackend.service.ExportPlaceService;
import team2.kakigowherebackend.service.ExportRatingService;

@Slf4j
@Component
public class CsvScheduler {
    private final ExportPlaceService exportPlaceService;
    private final ExportRatingService exportRatingService;

    public CsvScheduler(
            ExportPlaceService exportPlaceService, ExportRatingService exportRatingService) {
        this.exportPlaceService = exportPlaceService;
        this.exportRatingService = exportRatingService;
    }

    @Scheduled(cron = "0 50 23 * * *")
    public void exportCsvs() {
        log.info("Starting scheduled export CSV files for ML...");
        exportPlaceService.exportPlaces();
        log.info("Exported places");
        exportRatingService.exportRatings();
        log.info("Exported ratings");
        log.info("Finished exporting CSV files for ML...");
    }
}
