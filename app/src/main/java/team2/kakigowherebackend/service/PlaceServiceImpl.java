package team2.kakigowherebackend.service;

import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team2.kakigowherebackend.model.Place;
import team2.kakigowherebackend.model.InterestCategory;
import team2.kakigowherebackend.dto.PlaceDTO;
import team2.kakigowherebackend.repository.PlaceRepository;

@Service
@Transactional
public class PlaceServiceImpl implements PlaceService {

    private final PlaceRepository placeRepo;

    public PlaceServiceImpl(PlaceRepository placeRepo) {
        this.placeRepo = placeRepo;
    }

    @Override
    public void savePlace(Place place) {
        placeRepo.save(place);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Place> getAllPlaces() {
        return placeRepo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlaceDTO> getAllPlaceDTOs() {
        List<Place> places = placeRepo.findAll();
        List<PlaceDTO> dtos = new ArrayList<>();

        for (Place place : places) {
            List<String> interests = new ArrayList<>();

            if (place.getInterestCategories() != null) {
                for (InterestCategory ic : place.getInterestCategories()) {
                    if (ic != null && ic.getName() != null) {
                        interests.add(ic.getName());
                    }
                }
            }

            PlaceDTO dto = new PlaceDTO(
                    place.getId(),
                    place.getGoogleId(),
                    place.getName(),
                    place.getDescription(),
                    interests
            );
            dtos.add(dto);
        }
        return dtos;
    }

}