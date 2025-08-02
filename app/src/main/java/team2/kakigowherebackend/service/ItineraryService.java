package team2.kakigowherebackend.service;

import team2.kakigowherebackend.model.Itinerary;

import java.util.List;

public interface ItineraryService {

    List<Itinerary> findTouristItineraries(String email);
    void saveTouristItinerary(String touristEmail, Itinerary itinerary);

}
