package team2.kakigowherebackend.service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
        Tourist tourist = touristRepo.findByEmail(touristEmail).get();
        List<Itinerary> existingList = tourist.getItineraryList();
        existingList.add(itinerary);
        tourist.setItineraryList(existingList);

        itinerary.setTourist(tourist);
        itineraryRepo.save(itinerary);
    }

    @Override
    public void addItineraryDay(Long id, ItineraryDetail detail) {
        Itinerary itinerary = itineraryRepo.findById(id).get();
        List<ItineraryDetail> details = itineraryDetailRepo.findDetailsByItineraryId(id);

        detail.setItinerary(itinerary);
        detail.setSequentialOrder(details.size() + 1);
        details.add(detail);

        itinerary.setItineraryDetails(details);
        itineraryRepo.save(itinerary);
        itineraryDetailRepo.saveAll(details);
    }

    @Override
    public boolean deleteItineraryDay(Long id, String lastDate) {
        if (!itineraryRepo.findById(id).isPresent()) {
            return false;
        }

        List<ItineraryDetail> details = itineraryDetailRepo.findDetailsByItineraryId(id);
        for (ItineraryDetail detail : details) {
            if (detail.getDate().equals(LocalDate.parse(lastDate))) {
                itineraryDetailRepo.delete(detail);
            }
        }
        return true;
    }

    @Override
    public void addItineraryDetail(Long id, ItineraryDetail detail, Long placeId) {
        boolean found = false;

        List<ItineraryDetail> details = itineraryDetailRepo.findDetailsByItineraryId(id);
        Itinerary itinerary = itineraryRepo.findById(id).get();
        Place placeToAdd = placeRepo.findById(placeId).get();

        // if adding to existing itinerary day with no places saved, add the place to it
        for (ItineraryDetail itineraryDetail : details) {
            if (itineraryDetail.getDate().equals(detail.getDate())
                    && itineraryDetail.getPlace() == null) {
                itineraryDetail.setPlace(placeToAdd);
                found = true;
            }
        }

        // otherwise save a new entry of itinerary item to the itinerary day
        if (!found) {
            detail.setItinerary(itinerary);
            detail.setPlace(placeToAdd);
            details.add(detail);
        }

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

        ItineraryDetail deletedDetail = itineraryDetailRepo.findById(id).get();
        Long itineraryId = deletedDetail.getItinerary().getId();
        List<ItineraryDetail> details = itineraryDetailRepo.findDetailsByItineraryId(itineraryId);
        int count = 0;

        for (ItineraryDetail itineraryDetail : details) {
            if (itineraryDetail.getDate().equals(deletedDetail.getDate())) {
                count++;
            }
        }

        if (count == 1) { // if the itinerary detail is the only item for that day, remove only the Place
            deletedDetail.setPlace(null);
            deletedDetail.setNotes("");
            details =
                    details.stream()
                            .map(d -> d.getId() == deletedDetail.getId() ? deletedDetail : d)
                            .collect(Collectors.toList());
        } else if (count > 1) { // else delete the whole itinerary item and re-fetch list
            itineraryDetailRepo.deleteById(id);
            details = itineraryDetailRepo.findDetailsByItineraryId(itineraryId);
        }

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
