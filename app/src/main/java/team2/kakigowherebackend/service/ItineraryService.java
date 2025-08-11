package team2.kakigowherebackend.service;

import team2.kakigowherebackend.model.Itinerary;
import team2.kakigowherebackend.model.ItineraryDetail;

import java.util.List;

public interface ItineraryService {

    List<Itinerary> findTouristItineraries(String email);

    List<ItineraryDetail> findItineraryDetails(Long itineraryId);

    Itinerary createTouristItinerary(String touristEmail, Itinerary itinerary);

    boolean deleteTouristItinerary(Long id);

    Itinerary addItineraryDetail(Long id, ItineraryDetail detail, Long placeId);

    Itinerary editItineraryDetail(Long id, ItineraryDetail detail);

    boolean deleteItineraryDetail(Long id);

    Itinerary addItineraryDay(Long id, ItineraryDetail detail);

    boolean deleteItineraryDay(Long id, String lastDate);

}
