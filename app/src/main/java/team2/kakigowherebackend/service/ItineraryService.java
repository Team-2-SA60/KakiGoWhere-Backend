package team2.kakigowherebackend.service;

import java.util.List;
import team2.kakigowherebackend.model.Itinerary;
import team2.kakigowherebackend.model.ItineraryDetail;

public interface ItineraryService {

    List<Itinerary> findTouristItineraries(String email);

    List<ItineraryDetail> findItineraryDetails(Long itineraryId);

    Itinerary createItinerary(String touristEmail, Itinerary itinerary);

    boolean deleteItinerary(Long itineraryId);

    Itinerary addItineraryDay(Long itineraryId, ItineraryDetail detail);

    boolean deleteItineraryDay(Long itineraryId, String lastDate);

    Itinerary addItineraryDetail(Long itineraryId, ItineraryDetail newDetail, Long placeId);

    ItineraryDetail editItineraryDetail(Long detailId, ItineraryDetail newDetail);

    boolean deleteItineraryDetail(Long detailId);
}
