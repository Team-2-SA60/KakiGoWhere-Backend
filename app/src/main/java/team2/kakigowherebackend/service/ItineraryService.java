package team2.kakigowherebackend.service;

import team2.kakigowherebackend.model.Itinerary;
import team2.kakigowherebackend.model.ItineraryDetail;

import java.util.List;

public interface ItineraryService {

    List<Itinerary> findTouristItineraries(String email);

    List<ItineraryDetail> findItineraryDetails(Long id);

    void createTouristItinerary(String touristEmail, Itinerary itinerary);

    boolean deleteTouristItinerary(Long id);

    void addItineraryDetail(Long id, ItineraryDetail detail, Long placeId);

    void editItineraryDetail(Long id, ItineraryDetail detail);

    void addItineraryDay(Long id, ItineraryDetail detail);

    boolean deleteItineraryDay(Long id, String lastDate);

    boolean deleteItineraryDetail(Long id);
}
