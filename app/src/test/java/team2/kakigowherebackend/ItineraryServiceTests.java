package team2.kakigowherebackend;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import team2.kakigowherebackend.model.Itinerary;
import team2.kakigowherebackend.model.ItineraryDetail;
import team2.kakigowherebackend.model.Tourist;
import team2.kakigowherebackend.repository.ItineraryDetailRepository;
import team2.kakigowherebackend.repository.ItineraryRepository;
import team2.kakigowherebackend.repository.TouristRepository;
import team2.kakigowherebackend.service.ItineraryServiceImpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ItineraryServiceTests {

    @Mock
    private ItineraryRepository itineraryRepo;
    @Mock
    private ItineraryDetailRepository itineraryDetailRepo;
    @Mock
    private TouristRepository touristRepo;

    @InjectMocks
    private ItineraryServiceImpl itineraryService;

    @Test
    void findTouristItineraries_ReturnsList() {
        // define sample data
        String email = "test@kaki.com";
        Tourist mockTourist = new Tourist(email, "pw", "tester");
        List<Itinerary> mockItineraries = List.of(
                new Itinerary(1L, "My fun itinerary", LocalDate.of(2025, 8,5), mockTourist, null),
                new Itinerary(2L, "My awesome itinerary", LocalDate.of(2025, 8, 10), mockTourist, null)
        );

        // mock repository behaviour
        when(itineraryRepo.findByTouristEmail(email)).thenReturn(mockItineraries);

        // perform test
        List<Itinerary> result = itineraryService.findTouristItineraries(email);

        // assert outcome
        assertEquals(2, result.size());
        assertEquals(mockTourist, result.getFirst().getTourist());
    }

    @Test
    void createTouristItinerary_AddsNewItinerary() {
        // define sample data
        String email = "test@kaki.com";
        Tourist mockTourist = new Tourist(email, "pw", "tester");
        Itinerary mockItinerary = new Itinerary(1L, "My existing itinerary", LocalDate.of(2025, 8,12), mockTourist, null);
        Itinerary newItinerary = new Itinerary(2L, "My new itinerary", LocalDate.of(2025, 8, 18), mockTourist, null);
        mockTourist.setItineraryList(new ArrayList<>(List.of(mockItinerary)));

        // mock repository behaviour
        when(touristRepo.findByEmail(email)).thenReturn(Optional.of(mockTourist));

        // perform test
        itineraryService.createTouristItinerary(email, newItinerary);

        // assert outcome
        assertEquals(2, mockTourist.getItineraryList().size());
        assertTrue(mockTourist.getItineraryList().contains(newItinerary));
        assertEquals(mockTourist, newItinerary.getTourist());
    }

    @Test
    void addItineraryDay_AddsNewDetailWithoutPlace() {
        // define sample data
        Long itineraryId = 1L;
        Tourist mockTourist = new Tourist();
        Itinerary mockItinerary = new Itinerary(itineraryId, "My fun itinerary", LocalDate.of(2025, 8, 12), mockTourist, new ArrayList<>());
        ItineraryDetail newDetail = new ItineraryDetail(2L, LocalDate.of(2025, 8, 13), "New day", 1, mockItinerary, null);

        // mock repository behaviour
        when(itineraryRepo.findById(itineraryId)).thenReturn(Optional.of(mockItinerary));
        when(itineraryDetailRepo.findDetailsByItineraryId(itineraryId)).thenReturn(new ArrayList<>());

        // perform test
        itineraryService.addItineraryDay(itineraryId, newDetail);

        // assert outcome
        assertEquals(1, mockItinerary.getItineraryDetails().size());
        assertNull(mockItinerary.getItineraryDetails().getLast().getPlace());
    }

    @Test
    void deleteItineraryDay_RemoveAllItineraryDetails() {
        // define sample data
        Long itineraryId = 1L;
        LocalDate targetDate = LocalDate.of(2025, 8, 15);
        Itinerary mockItinerary = new Itinerary();
        mockItinerary.setId(itineraryId);

        ItineraryDetail matchingDetail1 = new ItineraryDetail();
        matchingDetail1.setItinerary(mockItinerary);
        matchingDetail1.setDate(targetDate);

        ItineraryDetail matchingDetail2 = new ItineraryDetail();
        matchingDetail2.setItinerary(mockItinerary);
        matchingDetail2.setDate(targetDate);

        ItineraryDetail nonMatchingDetail = new ItineraryDetail();
        nonMatchingDetail.setItinerary(mockItinerary);
        nonMatchingDetail.setDate(targetDate.plusDays(1));

        mockItinerary.setItineraryDetails(new ArrayList<>(List.of(matchingDetail1, nonMatchingDetail, matchingDetail2)));

        // mock repository behaviour
        when(itineraryRepo.findById(itineraryId)).thenReturn(Optional.of(mockItinerary));
        when(itineraryDetailRepo.findDetailsByItineraryId(itineraryId)).thenReturn(List.of(matchingDetail1, nonMatchingDetail, matchingDetail2));

        // perform test
        ArgumentCaptor<ItineraryDetail> deletionCaptor = ArgumentCaptor.forClass(ItineraryDetail.class);
        boolean result = itineraryService.deleteItineraryDay(itineraryId, targetDate.toString());

        // assert outcome
        verify(itineraryDetailRepo, times(2)).delete(deletionCaptor.capture());
        List<ItineraryDetail> deletedItems = deletionCaptor.getAllValues();
        assertEquals(2, deletedItems.size());
        assertTrue(deletedItems.contains(matchingDetail1));
        assertTrue(deletedItems.contains(matchingDetail2));
        assertFalse(deletedItems.contains(nonMatchingDetail));
        assertTrue(result);
    }

}
