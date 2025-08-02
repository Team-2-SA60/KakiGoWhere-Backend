package team2.kakigowherebackend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team2.kakigowherebackend.model.Itinerary;
import team2.kakigowherebackend.model.Tourist;
import team2.kakigowherebackend.repository.ItineraryDetailRepository;
import team2.kakigowherebackend.repository.ItineraryRepository;
import team2.kakigowherebackend.repository.TouristRepository;

import java.util.List;

@Service
@Transactional
public class ItineraryServiceImpl implements ItineraryService {

    private final ItineraryRepository itineraryRepo;
    private final TouristRepository touristRepo;

    public ItineraryServiceImpl(
            ItineraryRepository itineraryRepo,
            ItineraryDetailRepository itineraryDetailRepo,
            TouristRepository touristRepo) {
        this.itineraryRepo = itineraryRepo;
        this.touristRepo = touristRepo;
    }

    @Override
    public List<Itinerary> findTouristItineraries(String email) {
        return itineraryRepo.findByTouristEmail(email);
    }

    @Override
    public void saveTouristItinerary(String touristEmail, Itinerary itinerary) {
        Tourist tourist = touristRepo.findByEmail(touristEmail);
        itinerary.setTourist(tourist);
        itineraryRepo.save(itinerary);
    }

}
