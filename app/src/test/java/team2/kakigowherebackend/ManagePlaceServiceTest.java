package team2.kakigowherebackend;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;
import org.springframework.web.multipart.MultipartFile;
import team2.kakigowherebackend.dto.ManagePlaceDetailDTO;
import team2.kakigowherebackend.model.InterestCategory;
import team2.kakigowherebackend.model.OpeningHours;
import team2.kakigowherebackend.model.Place;
import team2.kakigowherebackend.repository.PlaceRepository;
import team2.kakigowherebackend.service.ImageService;
import team2.kakigowherebackend.service.ManagePlaceServiceImpl;

class ManagePlaceServiceTest {

    @Mock private PlaceRepository placeRepo;

    @Mock private ImageService imageService;

    @InjectMocks private ManagePlaceServiceImpl managePlaceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetPlaces() {
        int page = 0;
        int pageSize = 5;
        String keyword = "park";

        Place place = new Place();
        place.setName("Central Park");
        place.setInterestCategories(List.of(new InterestCategory()));
        place.setOpeningHours(List.of(new OpeningHours()));

        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Place> pageResult = new PageImpl<>(List.of(place), pageable, 1);

        when(placeRepo.getPlacesBySearch(keyword, pageable)).thenReturn(pageResult);

        Page<Place> result = managePlaceService.getPlaces(page, pageSize, keyword);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Central Park", result.getContent().get(0).getName());

        verify(placeRepo).getPlacesBySearch(keyword, pageable);
    }

    @Test
    void testUpdatePlace_PlaceExists() {
        Place existingPlace = new Place();
        existingPlace.setId(1L);
        existingPlace.setName("Old Name");

        Place updatedPlace = new Place();
        updatedPlace.setId(1L);
        updatedPlace.setName("New Name");
        updatedPlace.setAddress("New Address");
        updatedPlace.setDescription("New Description");
        updatedPlace.setLatitude(1.1);
        updatedPlace.setLongitude(2.2);
        updatedPlace.setURL("http://newurl.com");
        updatedPlace.setActive(true);
        updatedPlace.setAutoFetch(false);
        updatedPlace.setInterestCategories(List.of(new InterestCategory()));
        updatedPlace.setOpeningHours(List.of(new OpeningHours()));

        ManagePlaceDetailDTO dto = new ManagePlaceDetailDTO(updatedPlace);

        when(placeRepo.findById(1L)).thenReturn(Optional.of(existingPlace));
        when(placeRepo.save(existingPlace)).thenReturn(existingPlace);

        Place result = managePlaceService.updatePlace(dto);

        assertNotNull(result);
        assertEquals("New Name", result.getName());
        assertEquals("New Address", result.getAddress());
        assertEquals("New Description", result.getDescription());
        assertEquals(1.1, result.getLatitude());
        assertEquals(2.2, result.getLongitude());
        assertEquals("http://newurl.com", result.getURL());
        assertTrue(result.isActive());
        assertFalse(result.isAutoFetch());

        verify(placeRepo).findById(1L);
        verify(placeRepo).save(existingPlace);
    }

    @Test
    void testUpdatePlace_PlaceDoesNotExist() {
        Place updatedPlace = new Place();
        updatedPlace.setId(1L);
        ManagePlaceDetailDTO dto = new ManagePlaceDetailDTO(updatedPlace);

        when(placeRepo.findById(1L)).thenReturn(Optional.empty());

        Place result = managePlaceService.updatePlace(dto);

        assertNull(result);
        verify(placeRepo).findById(1L);
        verify(placeRepo, never()).save(any());
    }

    @Test
    void testUploadPlaceImage_Success() throws Exception {
        long placeId = 1L;
        Place place = new Place();
        place.setId(placeId);
        place.setGoogleId("google123");

        MultipartFile image = mock(MultipartFile.class);

        when(placeRepo.findById(placeId)).thenReturn(Optional.of(place));
        when(imageService.upload(image, "google123", 800, 600)).thenReturn("/images/google123.jpg");
        when(placeRepo.save(place)).thenReturn(place);

        String result = managePlaceService.uploadPlaceImage(placeId, image);

        assertEquals("/images/google123.jpg", result);
        assertEquals("/images/google123.jpg", place.getImagePath());

        verify(placeRepo).findById(placeId);
        verify(imageService).upload(image, "google123", 800, 600);
        verify(placeRepo).save(place);
    }

    @Test
    void testUploadPlaceImage_PlaceNotFound() {
        long placeId = 1L;
        MultipartFile image = mock(MultipartFile.class);

        when(placeRepo.findById(placeId)).thenReturn(Optional.empty());

        String result = managePlaceService.uploadPlaceImage(placeId, image);

        assertNull(result);

        verify(placeRepo).findById(placeId);
        verifyNoInteractions(imageService);
        verify(placeRepo, never()).save(any());
    }

    @Test
    void testUploadPlaceImage_ExceptionDuringUpload() throws Exception {
        long placeId = 1L;
        Place place = new Place();
        place.setId(placeId);
        place.setGoogleId("google123");

        MultipartFile image = mock(MultipartFile.class);

        when(placeRepo.findById(placeId)).thenReturn(Optional.of(place));
        when(imageService.upload(image, "google123", 800, 600))
                .thenThrow(new RuntimeException("Upload failed"));

        String result = managePlaceService.uploadPlaceImage(placeId, image);

        assertNull(result);

        verify(placeRepo).findById(placeId);
        verify(imageService).upload(image, "google123", 800, 600);
        verify(placeRepo, never()).save(any());
    }
}
