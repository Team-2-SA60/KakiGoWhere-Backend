package team2.kakigowherebackend.service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
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
    public List<ItineraryDetail> findItineraryDetails(Long itineraryId) {
        return itineraryDetailRepo.findDetailsByItineraryId(itineraryId);
    }

    @Override
    public Itinerary createItinerary(String touristEmail, Itinerary itinerary) {
        Tourist tourist = touristRepo.findByEmail(touristEmail).orElse(null);
        if (tourist == null) return null;

        tourist.getItineraryList().add(itinerary);
        itinerary.setTourist(tourist);
        return itineraryRepo.save(itinerary);
    }

    @Override
    public boolean deleteItinerary(Long itineraryId) {
        Itinerary itinerary = itineraryRepo.findById(itineraryId).orElse(null);
        if (itinerary == null) return false;

        itineraryRepo.delete(itinerary);
        return true;
    }

    @Override
    public Itinerary addItineraryDay(Long itineraryId, ItineraryDetail detail) {
        Itinerary itinerary = itineraryRepo.findById(itineraryId).orElse(null);
        if (itinerary == null) return null;

        List<ItineraryDetail> itineraryDetails = itinerary.getItineraryDetails();
        itineraryDetails.add(detail);
        detail.setItinerary(itinerary);

        sortOrderByDate(itineraryDetails);
        return itineraryRepo.save(itinerary);
    }

    @Override
    public boolean deleteItineraryDay(Long itineraryId, String lastDate) {
        Itinerary itinerary = itineraryRepo.findById(itineraryId).orElse(null);
        if (itinerary == null) return false;

        List<ItineraryDetail> itineraryDetails =
                itineraryDetailRepo.findDetailsByItineraryId(itineraryId);
        for (ItineraryDetail detail : itineraryDetails) {
            if (detail.getDate().equals(LocalDate.parse(lastDate))) {
                itineraryDetailRepo.delete(detail);
            }
        }
        return true;
    }

    @Override
    public Itinerary addItineraryDetail(Long itineraryId, ItineraryDetail newDetail, Long placeId) {
        Itinerary itinerary = itineraryRepo.findById(itineraryId).orElse(null);
        Place placeToAdd = placeRepo.findById(placeId).orElse(null);
        if (itinerary == null || placeToAdd == null) return null;

        List<ItineraryDetail> details = itinerary.getItineraryDetails();
        boolean emptyDay = false;

        // if adding to existing itinerary day with no places saved, add the place to it
        for (ItineraryDetail detail : details) {
            if (detail.getDate().isEqual(newDetail.getDate()) && detail.getPlace() == null) {
                detail.setPlace(placeToAdd);
                emptyDay = true;
                break;
            }
        }

        // otherwise save a new entry of itinerary item to the itinerary day
        if (!emptyDay) {
            newDetail.setItinerary(itinerary);
            newDetail.setPlace(placeToAdd);
            details.add(newDetail);
        }

        sortOrderByDate(details);
        return itineraryRepo.save(itinerary);
    }

    @Override
    public ItineraryDetail editItineraryDetail(Long detailId, ItineraryDetail newDetail) {
        ItineraryDetail detail = itineraryDetailRepo.findById(detailId).orElse(null);
        if (detail == null) return null;

        detail.setNotes(newDetail.getNotes());
        return itineraryDetailRepo.save(detail);
    }

    @Override
    public boolean deleteItineraryDetail(Long detailId) {
        ItineraryDetail detail = itineraryDetailRepo.findById(detailId).orElse(null);
        if (detail == null) return false;

        Long itineraryId = detail.getItinerary().getId();
        List<ItineraryDetail> details = itineraryDetailRepo.findDetailsByItineraryId(itineraryId);

        int count = 0;
        for (ItineraryDetail itineraryDetail : details) {
            if (itineraryDetail.getDate().isEqual(detail.getDate())) {
                count++;
            }
        }

        // if the itinerary detail is the only item for that day, remove only the Place
        if (count == 1) {
            detail.setPlace(null);
            detail.setNotes("");
            details =
                    details.stream()
                            .map(d -> d.getId() == detail.getId() ? detail : d)
                            .collect(Collectors.toList());
        }
        // else delete the whole itinerary item and update current list
        else if (count > 1) {
            itineraryDetailRepo.deleteById(detailId);
            details.removeIf(d -> d.getId() == detailId);
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
