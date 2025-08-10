package team2.kakigowherebackend;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.util.ReflectionTestUtils;
import team2.kakigowherebackend.model.Place;
import team2.kakigowherebackend.repository.PlaceRepository;
import team2.kakigowherebackend.service.PlaceServiceImpl;

class PlaceServiceTest {

    @Mock private PlaceRepository placeRepo;

    @InjectMocks private PlaceServiceImpl placeService;

    private Path tempDir;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        // Create temp upload directory for testing
        tempDir = Files.createTempDirectory("upload-dir");
        ReflectionTestUtils.setField(placeService, "uploadDir", tempDir.toString());
    }

    @Test
    void testGetPlaces() {
        Place place1 = new Place();
        Place place2 = new Place();
        when(placeRepo.findAll()).thenReturn(Arrays.asList(place1, place2));

        List<Place> result = placeService.getPlaces();

        assertEquals(2, result.size());
        verify(placeRepo, times(1)).findAll();
    }

    @Test
    void testGetPlaceDetailFound() {
        Place place = new Place();
        when(placeRepo.findById(1L)).thenReturn(Optional.of(place));

        Place result = placeService.getPlaceDetail(1L);

        assertNotNull(result);
        assertSame(place, result);
    }

    @Test
    void testGetPlaceDetailNotFound() {
        when(placeRepo.findById(1L)).thenReturn(Optional.empty());

        Place result = placeService.getPlaceDetail(1L);

        assertNull(result);
    }

    @Test
    void testGetImageByPlaceId_ImageExists() throws Exception {
        // Arrange: create a fake image file
        String googleId = "abc123";
        Path imagePath = tempDir.resolve(googleId + ".jpg");
        Files.writeString(imagePath, "fake image content");

        Place place = new Place();
        place.setGoogleId(googleId);
        place.setImagePath(imagePath.toString());

        when(placeRepo.findById(1L)).thenReturn(Optional.of(place));

        // Act
        Resource resource = placeService.getImageByPlaceId(1L);

        // Assert
        assertNotNull(resource);
        assertTrue(resource.exists());
        assertTrue(resource.isReadable());
        assertEquals(imagePath.toUri(), resource.getURI());
    }

    @Test
    void testGetImageByPlaceId_ImageMissing_ReturnsDefault() throws MalformedURLException {
        Place place = new Place();
        place.setGoogleId("nonexistent");
        place.setImagePath("some/path.jpg");

        when(placeRepo.findById(1L)).thenReturn(Optional.of(place));

        Resource resource = placeService.getImageByPlaceId(1L);

        assertNotNull(resource);
        assertTrue(resource instanceof ClassPathResource);
        assertTrue(resource.exists());
    }

    @Test
    void testGetImageByPlaceId_PlaceNotFound_ReturnsNull() throws MalformedURLException {
        when(placeRepo.findById(1L)).thenReturn(Optional.empty());

        Resource resource = placeService.getImageByPlaceId(1L);

        assertNull(resource);
    }
}
