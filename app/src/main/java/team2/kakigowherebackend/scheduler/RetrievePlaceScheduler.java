package team2.kakigowherebackend.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import team2.kakigowherebackend.service.RetrievePlaceService;
import team2.kakigowherebackend.service.RetrievePlaceServiceImpl;

@Component
public class RetrievePlaceScheduler {

    private final RetrievePlaceService rpService;

    public RetrievePlaceScheduler(RetrievePlaceServiceImpl rpService) {
        this.rpService = rpService;
    }

    @Scheduled(cron = "0 0 0 ? * 6#3")
    public void retrievePlaces() {
        rpService.retrievePlaces();
    }
}
