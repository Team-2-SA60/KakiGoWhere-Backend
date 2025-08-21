package team2.kakigowherebackend.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import team2.kakigowherebackend.dto.PlaceEventRequestDTO;
import team2.kakigowherebackend.dto.PlaceEventResponseDTO;
import team2.kakigowherebackend.model.Place;
import team2.kakigowherebackend.model.PlaceEvent;
import team2.kakigowherebackend.repository.PlaceEventRepository;
import team2.kakigowherebackend.repository.PlaceRepository;

@Service
@Transactional
public class PlaceEventServiceImpl implements PlaceEventService {
    private final PlaceEventRepository placeEventRepo;
    private final PlaceRepository placeRepo;

    public PlaceEventServiceImpl(PlaceEventRepository placeEventRepo, PlaceRepository placeRepo) {
        this.placeEventRepo = placeEventRepo;
        this.placeRepo = placeRepo;
    }

    @Override
    public PlaceEventResponseDTO createEvent(PlaceEventRequestDTO request) {
        // further layer of defensive checks
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Event name is required");
        }
        if (request.getPlaceId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Place is required");
        }
        if (request.getStartDate() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Start date is required");
        }
        if (request.getEndDate() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "End date is required");
        }
        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "End date must be on/after start date");
        }

        Place place =
                placeRepo
                        .findById(request.getPlaceId())
                        .orElseThrow(
                                () ->
                                        new ResponseStatusException(
                                                HttpStatus.BAD_REQUEST, "Place not found"));

        boolean exists =
                placeEventRepo.existsByNameAndStartDateAndEndDateAndPlace_Id(
                        request.getName().trim(),
                        request.getStartDate(),
                        request.getEndDate(),
                        request.getPlaceId());

        if (exists) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Event already exists for this place and date range");
        }

        PlaceEvent e = new PlaceEvent();
        e.setName(request.getName().trim());
        e.setDescription(request.getDescription());
        e.setStartDate(request.getStartDate());
        e.setEndDate(request.getEndDate());
        e.setPlace(place);

        PlaceEvent saved = placeEventRepo.save(e);
        // return the DTO of saved
        return toDTO(saved);
    }

    @Override
    public PlaceEventResponseDTO updateEvent(Long id, PlaceEventRequestDTO request) {
        // load existing event
        PlaceEvent e =
                placeEventRepo
                        .findById(id)
                        .orElseThrow(
                                () ->
                                        new ResponseStatusException(
                                                HttpStatus.NOT_FOUND, "Event not found"));

        // further layer of defensive checks
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Event name is required");
        }
        if (request.getPlaceId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Place is required");
        }
        if (request.getStartDate() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Start date is required");
        }
        if (request.getEndDate() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "End date is required");
        }

        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "End date must be on/after start date");
        }

        e.setName(request.getName().trim());
        e.setDescription(request.getDescription());
        e.setStartDate(request.getStartDate());
        e.setEndDate(request.getEndDate());

        // update place by id
        if (e.getPlace() == null || e.getPlace().getId() != request.getPlaceId()) {
            Place place =
                    placeRepo
                            .findById(request.getPlaceId())
                            .orElseThrow(
                                    () ->
                                            new ResponseStatusException(
                                                    HttpStatus.BAD_REQUEST, "Place not found"));
            e.setPlace(place);
        }

        PlaceEvent saved = placeEventRepo.save(e);
        // return the DTO of saved
        return toDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PlaceEventResponseDTO getEventById(Long id) {
        PlaceEvent e =
                placeEventRepo
                        .findById(id)
                        .orElseThrow(
                                () ->
                                        new ResponseStatusException(
                                                HttpStatus.NOT_FOUND, "Event not found"));
        return toDTO(e);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PlaceEventResponseDTO> searchEvents(String keyword, Pageable pageable) {
        return placeEventRepo.search(keyword, pageable).map(this::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlaceEventResponseDTO> getEventsByPlaceId(Long placeId) {
        return placeEventRepo.findByPlace_Id(placeId).stream().map(this::toDTO).toList();
    }

    // helper to map PlaceEvent to PlaceEventResponseDTO
    private PlaceEventResponseDTO toDTO(PlaceEvent e) {
        PlaceEventResponseDTO dto = new PlaceEventResponseDTO();
        dto.setId(e.getId());
        dto.setName(e.getName());
        dto.setDescription(e.getDescription());
        dto.setStartDate(e.getStartDate());
        dto.setEndDate(e.getEndDate());
        if (e.getPlace() != null) {
            dto.setPlaceId(e.getPlace().getId());
            dto.setPlaceName(e.getPlace().getName());
        }
        return dto;
    }
}
