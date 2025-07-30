package team2.kakigowherebackend.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team2.kakigowherebackend.model.Place;
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
}
