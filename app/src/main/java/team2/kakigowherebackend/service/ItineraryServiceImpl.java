package team2.kakigowherebackend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team2.kakigowherebackend.model.Itinerary;
import team2.kakigowherebackend.model.ItineraryDetail;
import team2.kakigowherebackend.model.Place;
import team2.kakigowherebackend.model.Tourist;
import team2.kakigowherebackend.repository.ItineraryDetailRepository;
import team2.kakigowherebackend.repository.ItineraryRepository;
import team2.kakigowherebackend.repository.PlaceRepository;
import team2.kakigowherebackend.repository.TouristRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ItineraryServiceImpl implements ItineraryService {

    private final ItineraryRepository itineraryRepo;
    private final ItineraryDetailRepository itineraryDetailRepo;
    private final PlaceRepository placeRepo;
    private final TouristRepository touristRepo;

    public ItineraryServiceImpl(
            ItineraryRepository itineraryRepo,
            ItineraryDetailRepository itineraryDetailRepo,
            PlaceRepository placeRepo,
            TouristRepository touristRepo) {
        this.itineraryRepo = itineraryRepo;
        this.itineraryDetailRepo = itineraryDetailRepo;
        this.placeRepo = placeRepo;
        this.touristRepo = touristRepo;
    }

    @Override
    public List<Itinerary> findTouristItineraries(String email) {
        return itineraryRepo.findByTouristEmail(email);
    }

    @Override
    public List<ItineraryDetail> findItineraryDetails(Long id) {
        return itineraryDetailRepo.findDetailsByItineraryId(id);
    }

    @Override
    public void createTouristItinerary(String touristEmail, Itinerary itinerary) {
        Optional<Tourist> tourist = touristRepo.findByEmail(touristEmail);
        itinerary.setTourist(tourist.get());
        itineraryRepo.save(itinerary);
    }

    @Override
    public void addItineraryDetail(Long id, ItineraryDetail detail, Long placeId) {
        List<ItineraryDetail> details = itineraryDetailRepo.findDetailsByItineraryId(id);
        Itinerary itinerary = itineraryRepo.findById(id).get();
        Place placeToAdd = placeRepo.findById(placeId).get();

        detail.setItinerary(itinerary);
        detail.setPlace(placeToAdd);
        details.add(detail);

        sortOrderByDate(details);

        itineraryDetailRepo.saveAll(details);
    }

    @Override
    public void editItineraryDetail(Long id, ItineraryDetail detail) {
        ItineraryDetail itineraryDetail = itineraryDetailRepo.findById(id).get();
        itineraryDetail.setNotes(detail.getNotes());
        itineraryDetailRepo.save(itineraryDetail);
    }

    @Override
    public boolean deleteItineraryDetail(Long id) {
        if (!itineraryDetailRepo.findById(id).isPresent()) {
            return false;
        }
        itineraryDetailRepo.deleteById(id);
        List<ItineraryDetail> details = itineraryDetailRepo.findDetailsByItineraryId(id);
        sortOrderByDate(details);
        itineraryDetailRepo.saveAll(details);
        return true;
    }

    // helper method to sort list by date and update sequential order

    public static void sortOrderByDate(List<ItineraryDetail> details) {
        details.sort(Comparator.comparing(ItineraryDetail::getDate));
        for (int i = 0; i < details.size(); i++) {
            details.get(i).setSequentialOrder(i + 1);
        }
    }
}
