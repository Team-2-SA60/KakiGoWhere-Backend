package team2.kakigowherebackend.service;

import team2.kakigowherebackend.model.Place;

import java.util.List;

public interface PlaceService {
   void savePlace(Place place);
   List<Place> getAllPlaces();
}
