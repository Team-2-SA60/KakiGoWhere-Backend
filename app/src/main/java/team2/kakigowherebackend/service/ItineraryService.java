package team2.kakigowherebackend.service;

import java.util.List;
import team2.kakigowherebackend.model.Itinerary;
import team2.kakigowherebackend.model.ItineraryDetail;

public interface ItineraryService {

    List<Itinerary> findTouristItineraries(String email);

    List<ItineraryDetail> findItineraryDetails(Long id);

    void createTouristItinerary(String touristEmail, Itinerary itinerary);
}
