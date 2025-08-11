package team2.kakigowherebackend;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import team2.kakigowherebackend.model.Itinerary;
import team2.kakigowherebackend.model.ItineraryDetail;
import team2.kakigowherebackend.model.Place;
import team2.kakigowherebackend.model.Tourist;
import team2.kakigowherebackend.repository.ItineraryDetailRepository;
import team2.kakigowherebackend.repository.ItineraryRepository;
import team2.kakigowherebackend.repository.PlaceRepository;
import team2.kakigowherebackend.repository.TouristRepository;
import team2.kakigowherebackend.service.ItineraryServiceImpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ItineraryServiceTests {

    @Mock private ItineraryRepository itineraryRepo;
    @Mock private ItineraryDetailRepository itineraryDetailRepo;
    @Mock private TouristRepository touristRepo;
    @Mock private PlaceRepository placeRepo;

    @InjectMocks private ItineraryServiceImpl itineraryService;

    Place mockPlace;
    Tourist mockTourist;
    List<Itinerary> mockItineraries;
    List<ItineraryDetail> mockItineraryDetailsList;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);

        mockPlace = new Place();
        mockPlace.setId(1L);
        mockPlace.setName("Test Place");

        mockTourist = new Tourist();
        mockTourist.setId(1L);
        mockTourist.setEmail("test@example.com");

        mockItineraries = new ArrayList<>();
        mockItineraries.add(new Itinerary());
        mockItineraries.add(new Itinerary());

        mockItineraries.getFirst().setId(1L);
        mockItineraries.getFirst().setStartDate(LocalDate.of(2025, 8, 12));
        mockItineraries.getFirst().setTourist(mockTourist);

        mockItineraryDetailsList = new ArrayList<>();
        mockItineraryDetailsList.add(new ItineraryDetail());
        mockItineraryDetailsList.add(new ItineraryDetail());

        mockItineraryDetailsList.getFirst().setId(1L);
        mockItineraryDetailsList.getFirst().setDate(LocalDate.of(2025, 8, 12));
        mockItineraryDetailsList.getFirst().setItinerary(mockItineraries.getFirst());
        mockItineraryDetailsList.getLast().setId(2L);
        mockItineraryDetailsList.getLast().setDate(LocalDate.of(2025, 8, 13));
        mockItineraryDetailsList.getLast().setItinerary(mockItineraries.getFirst());

        mockItineraries.getFirst().setItineraryDetails(mockItineraryDetailsList);
        mockTourist.setItineraryList(mockItineraries);
    }

    @Test
    void testFindTouristItineraries_ReturnsList_IfFound() {
        String email = "test@example.com";

        // perform call
        when(itineraryRepo.findByTouristEmail(email)).thenReturn(mockItineraries);
        List<Itinerary> result = itineraryService.findTouristItineraries(email);

        // assert outcome
        verify(itineraryRepo, times(1)).findByTouristEmail(email);

        assertEquals(2, result.size());
        assertEquals(mockTourist, result.getFirst().getTourist());
    }

    @Test
    void testFindItineraryDetails_ReturnsList_IfFound() {
        Long itineraryId = 1L;

        // perform call
        when(itineraryDetailRepo.findDetailsByItineraryId(itineraryId))
                .thenReturn(mockItineraryDetailsList);
        List<ItineraryDetail> result = itineraryService.findItineraryDetails(itineraryId);

        // assert outcome
        verify(itineraryDetailRepo, times(1)).findDetailsByItineraryId(itineraryId);

        assertEquals(2, result.size());
        assertTrue(result.contains(mockItineraryDetailsList.getFirst()));
        assertTrue(result.contains(mockItineraryDetailsList.getLast()));
    }

    @Test
    void testCreateTouristItinerary_AddsNewItinerary() {
        String email = "test@example.com";
        Itinerary newItinerary = new Itinerary();

        // perform call
        when(touristRepo.findByEmail(email)).thenReturn(Optional.of(mockTourist));
        itineraryService.createItinerary(email, newItinerary);

        // assert outcome
        verify(touristRepo, times(1)).findByEmail(email);
        verify(itineraryRepo, times(1)).save(newItinerary);

        assertEquals(3, mockTourist.getItineraryList().size());
        assertTrue(mockTourist.getItineraryList().contains(newItinerary));
        assertEquals(mockTourist, newItinerary.getTourist());
    }

    @Test
    void testDeleteTouristItinerary_DeletesItinerary() {
        Long itineraryId = 2L;

        // perform call
        when(itineraryRepo.findById(itineraryId))
                .thenReturn(Optional.of(mockItineraries.getLast()));

        ArgumentCaptor<Itinerary> deletionCaptor = ArgumentCaptor.forClass(Itinerary.class);
        boolean result = itineraryService.deleteItinerary(itineraryId);

        // assert outcome
        verify(itineraryRepo, times(2)).findById(itineraryId);
        verify(itineraryRepo, times(1)).delete(deletionCaptor.capture());

        List<Itinerary> deletedItems = deletionCaptor.getAllValues();
        assertTrue(result);
        assertEquals(1, deletedItems.size());
        assertEquals(deletedItems.getFirst(), mockItineraries.getLast());
    }

    @Test
    void testAddItineraryDay_AddsNewDetailWithoutPlace() {
        Long itineraryId = 1L;
        ItineraryDetail newDetail = new ItineraryDetail();
        List<ItineraryDetail> newList = new ArrayList<>(mockItineraryDetailsList);
        newList.add(newDetail);

        // perform call
        when(itineraryRepo.findById(itineraryId))
                .thenReturn(Optional.of(mockItineraries.getFirst()));
        when(itineraryDetailRepo.findDetailsByItineraryId(itineraryId))
                .thenReturn(mockItineraryDetailsList);
        itineraryService.addItineraryDay(itineraryId, newDetail);

        // assert outcome
        verify(itineraryRepo, times(1)).findById(itineraryId);
        verify(itineraryDetailRepo, times(1)).findDetailsByItineraryId(itineraryId);
        verify(itineraryRepo, times(1)).save(mockItineraries.getFirst());
        verify(itineraryDetailRepo, times(1)).saveAll(newList);

        assertEquals(3, newList.size());
        assertTrue(newList.contains(newDetail));
        assertNull(newDetail.getPlace());
    }

    @Test
    void testDeleteItineraryDay_RemoveAllItineraryDetails() {
        // define sample data
        Long itineraryId = 1L;
        LocalDate targetDate = LocalDate.of(2025, 8, 13);
        ItineraryDetail newDetailOnDate = new ItineraryDetail();
        newDetailOnDate.setId(3L);
        newDetailOnDate.setDate(targetDate);
        newDetailOnDate.setItinerary(mockItineraries.getFirst());
        List<ItineraryDetail> newList = new ArrayList<>(mockItineraryDetailsList);
        newList.add(newDetailOnDate);

        // perform call
        when(itineraryRepo.findById(itineraryId))
                .thenReturn(Optional.of(mockItineraries.getFirst()));
        when(itineraryDetailRepo.findDetailsByItineraryId(itineraryId)).thenReturn(newList);

        ArgumentCaptor<ItineraryDetail> deletionCaptor =
                ArgumentCaptor.forClass(ItineraryDetail.class);
        boolean result = itineraryService.deleteItineraryDay(itineraryId, targetDate.toString());

        // assert outcome
        verify(itineraryRepo, times(1)).findById(itineraryId);
        verify(itineraryDetailRepo, times(1)).findDetailsByItineraryId(itineraryId);
        verify(itineraryDetailRepo, times(2)).delete(deletionCaptor.capture());

        List<ItineraryDetail> deletedItems = deletionCaptor.getAllValues();
        assertEquals(2, deletedItems.size());
        assertTrue(deletedItems.contains(newDetailOnDate));
        assertFalse(deletedItems.contains(mockItineraryDetailsList.getFirst()));
        assertTrue(result);
    }

    @Test
    void testAddItineraryDetail_IfNoExistingItem_AddPlace() {
        Long itineraryId = 1L;
        Long placeId = 1L;
        LocalDate targetDate = LocalDate.of(2025, 8, 13);
        ItineraryDetail newDetail = new ItineraryDetail();
        newDetail.setDate(targetDate);

        // perform call
        // when(itineraryDetailRepo.findDetailsByItineraryId(itineraryId)).thenReturn(mockItineraryDetailsList);
        when(itineraryRepo.findById(itineraryId))
                .thenReturn(Optional.of(mockItineraries.getFirst()));
        when(placeRepo.findById(placeId)).thenReturn(Optional.of(mockPlace));
        itineraryService.addItineraryDetail(itineraryId, newDetail, placeId);

        // assert outcome
        // verify(itineraryDetailRepo, times(1)).findDetailsByItineraryId(itineraryId);
        verify(itineraryRepo, times(1)).findById(itineraryId);
        verify(placeRepo, times(1)).findById(placeId);
        verify(itineraryRepo, times(1)).save(mockItineraries.getFirst());

        assertEquals(2, mockItineraries.getFirst().getItineraryDetails().size());
    }

    @Test
    void testAddItineraryDetail_IfExistingItem_AddNewItem() {
        // add existing Place to itinerary item on same day
        List<ItineraryDetail> listWithPlace = new ArrayList<>(mockItineraryDetailsList);
        listWithPlace.getLast().setPlace(mockPlace);

        Long itineraryId = 1L;
        Long placeId = 1L;
        LocalDate targetDate = LocalDate.of(2025, 8, 13);
        ItineraryDetail newDetail = new ItineraryDetail();
        newDetail.setDate(targetDate);

        List<ItineraryDetail> newList = new ArrayList<>(mockItineraryDetailsList);
        newList.add(newDetail);

        // perform call
        // when(itineraryDetailRepo.findDetailsByItineraryId(itineraryId)).thenReturn(listWithPlace);
        when(itineraryRepo.findById(itineraryId))
                .thenReturn(Optional.of(mockItineraries.getFirst()));
        when(placeRepo.findById(placeId)).thenReturn(Optional.of(mockPlace));
        itineraryService.addItineraryDetail(itineraryId, newDetail, placeId);

        // assert outcome
        // verify(itineraryDetailRepo, times(1)).findDetailsByItineraryId(itineraryId);
        verify(itineraryRepo, times(1)).findById(itineraryId);
        verify(placeRepo, times(1)).findById(placeId);
        verify(itineraryRepo, times(1)).save(mockItineraries.getFirst());

        assertEquals(3, newList.size());
    }

    @Test
    void testDeleteItineraryDetail_IfOnlyItem_RemovePlace() {
        // add existing Place to itinerary item on same day
        List<ItineraryDetail> listWithPlace = new ArrayList<>(mockItineraryDetailsList);
        listWithPlace.getLast().setPlace(mockPlace);

        Long itineraryDetailId = 2L;
        Long itineraryId = 1L; // the itinerary this detail comes from

        // perform call
        when(itineraryDetailRepo.findById(itineraryDetailId))
                .thenReturn(Optional.of(listWithPlace.getLast()));
        when(itineraryDetailRepo.findDetailsByItineraryId(itineraryId)).thenReturn(listWithPlace);
        itineraryService.deleteItineraryDetail(itineraryDetailId);

        // assert outcome
        verify(itineraryDetailRepo, times(2)).findById(itineraryDetailId);
        verify(itineraryDetailRepo, times(1)).findDetailsByItineraryId(itineraryId);
        verify(itineraryDetailRepo, times(1)).saveAll(listWithPlace);

        assertEquals(2, listWithPlace.size());
        assertNull(listWithPlace.getLast().getPlace());
    }

    @Test
    void testDeleteItineraryDetail_IfMoreThanOneItem_RemoveItem() {
        List<ItineraryDetail> newList = new ArrayList<>(mockItineraryDetailsList);
        ItineraryDetail deletedDetail = new ItineraryDetail();
        deletedDetail.setId(3L);
        deletedDetail.setDate(LocalDate.of(2025, 8, 13));
        deletedDetail.setItinerary(mockItineraries.getFirst());
        newList.add(deletedDetail);

        Long itineraryDetailId = 3L;
        Long itineraryId = 1L; // the itinerary this detail comes from

        // perform call
        when(itineraryDetailRepo.findById(itineraryDetailId))
                .thenReturn(Optional.of(newList.getLast()));
        when(itineraryDetailRepo.findDetailsByItineraryId(itineraryId)).thenReturn(newList);

        ArgumentCaptor<Long> deletionCaptor = ArgumentCaptor.forClass(Long.class);
        boolean result = itineraryService.deleteItineraryDetail(itineraryDetailId);

        // assert outcome
        verify(itineraryDetailRepo, times(2)).findById(itineraryDetailId);
        verify(itineraryDetailRepo, times(2)).findDetailsByItineraryId(itineraryId);
        verify(itineraryDetailRepo, times(1)).deleteById(deletionCaptor.capture());
        verify(itineraryDetailRepo, times(1)).saveAll(newList);

        List<Long> deletedItems = deletionCaptor.getAllValues();
        assertEquals(1, deletedItems.size());
        assertEquals(deletedItems.getFirst(), newList.getLast().getId());
        assertTrue(result);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }
}
