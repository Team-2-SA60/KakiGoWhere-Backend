package team2.kakigowherebackend.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import team2.kakigowherebackend.dto.PlaceEventRequestDTO;
import team2.kakigowherebackend.dto.PlaceEventResponseDTO;

public interface PlaceEventService {
    PlaceEventResponseDTO createEvent(PlaceEventRequestDTO request);

    PlaceEventResponseDTO updateEvent(Long id, PlaceEventRequestDTO request);

    PlaceEventResponseDTO getEventById(Long id);

    Page<PlaceEventResponseDTO> searchEvents(String keyword, Pageable pageable);

    List<PlaceEventResponseDTO> getEventsByPlaceId(Long placeId);

    List<PlaceEventResponseDTO> getActiveEventsForPlace(long placeId);
}
