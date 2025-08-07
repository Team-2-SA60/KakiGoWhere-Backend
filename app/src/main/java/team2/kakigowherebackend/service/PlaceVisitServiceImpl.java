package team2.kakigowherebackend.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import team2.kakigowherebackend.model.Tourist;
import team2.kakigowherebackend.model.Place;
import team2.kakigowherebackend.model.TouristPlaceVisit;
import team2.kakigowherebackend.repository.TouristPlaceVisitRepository;
import team2.kakigowherebackend.repository.TouristRepository;
import team2.kakigowherebackend.repository.PlaceRepository;
import team2.kakigowherebackend.service.PlaceVisitService;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.time.YearMonth;

@Service
public class PlaceVisitServiceImpl implements PlaceVisitService {

    private final TouristPlaceVisitRepository visitRepo;
    private final TouristRepository touristRepo;
    private final PlaceRepository placeRepo;

    public PlaceVisitServiceImpl(TouristPlaceVisitRepository visitRepo, TouristRepository touristRepo, PlaceRepository placeRepo) {
        this.visitRepo = visitRepo;
        this.touristRepo = touristRepo;
        this.placeRepo = placeRepo;
    }

    @Override
    @Transactional
    public void recordVisit(long touristId, long placeId, LocalDate date){
        boolean alreadyExists = visitRepo.existsByTouristIdAndPlaceIdAndVisitDate(touristId, placeId, date);
        if(!alreadyExists) {
            Tourist tourist = touristRepo.getReferenceById(touristId);
            Place place = placeRepo.getReferenceById(placeId);
            TouristPlaceVisit placeVisit = new TouristPlaceVisit();
            placeVisit.setTourist(tourist);
            placeVisit.setPlace(place);
            placeVisit.setVisitDate(date);

            visitRepo.save(placeVisit);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Map<LocalDate, Integer> getDailyVisitCounts(long placeId, LocalDate month) {
        YearMonth ym = YearMonth.of(month.getYear(), month.getMonthValue());

        // pre-fill map with zeros for every day
        Map<LocalDate, Integer> map = new LinkedHashMap<>();
        for (int d = 1; d <= ym.lengthOfMonth(); d++) {
            map.put(ym.atDay(d), 0);
        }

        // list of [visitDate, count]
        var visits = visitRepo.findByPlaceAndMonth(placeId, month.getYear(), month.getMonthValue());

        visits.forEach(row -> {
            LocalDate date = (LocalDate) row[0];
            Integer count = ((Number) row[1]).intValue();
            map.put(date, count);
        });

        return map;
    }
}