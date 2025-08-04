package team2.kakigowherebackend.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import team2.kakigowherebackend.model.Place;
import team2.kakigowherebackend.repository.PlaceRepository;

@Service
public class ManagePlaceServiceImpl implements ManagePlaceService {

    private final PlaceRepository placeRepo;

    public ManagePlaceServiceImpl(PlaceRepository placeRepo) {
        this.placeRepo = placeRepo;
    }

    @Override
    public Page<Place> getPlaces(int page, int pageSize, String keyword) {
        Pageable pageable;
        pageable = PageRequest.of(page, pageSize);
        return placeRepo.getPlacesBySearch(keyword, pageable);
    }
}
