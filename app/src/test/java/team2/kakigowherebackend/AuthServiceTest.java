package team2.kakigowherebackend;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import team2.kakigowherebackend.model.Admin;
import team2.kakigowherebackend.model.Tourist;
import team2.kakigowherebackend.model.User;
import team2.kakigowherebackend.repository.AdminRepository;
import team2.kakigowherebackend.repository.TouristRepository;
import team2.kakigowherebackend.repository.UserRepository;
import team2.kakigowherebackend.service.AuthServiceImpl;
import team2.kakigowherebackend.utils.PasswordEncoderUtil;
import team2.kakigowherebackend.utils.UserConstants;

class AuthServiceTest {

    @Mock private UserRepository userRepo;

    @Mock private TouristRepository touristRepo;

    @Mock private AdminRepository adminRepo;

    @InjectMocks private AuthServiceImpl authService;

    private Admin mockAdmin;
    private Tourist mockTourist;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockAdmin = new Admin();
        mockAdmin.setId(1L);
        mockAdmin.setEmail("admin@example.com");
        mockAdmin.setPassword("encodedPassword");

        mockTourist = new Tourist();
        mockTourist.setId(2L);
        mockTourist.setEmail("tourist@example.com");
        mockTourist.setPassword("encodedPassword");
    }

    @Test
    void testFindUserByRoleAndId_Admin() {
        when(adminRepo.findById(1L)).thenReturn(Optional.of(mockAdmin));

        User result = authService.findUserByRoleAndId(UserConstants.ADMIN, 1L);

        assertNotNull(result);
        assertEquals("admin@example.com", result.getEmail());
        verify(adminRepo).findById(1L);
        verifyNoInteractions(touristRepo);
    }

    @Test
    void testFindUserByRoleAndId_Tourist() {
        when(touristRepo.findById(2L)).thenReturn(Optional.of(mockTourist));

        User result = authService.findUserByRoleAndId(UserConstants.TOURIST, 2L);

        assertNotNull(result);
        assertEquals("tourist@example.com", result.getEmail());
        verify(touristRepo).findById(2L);
        verifyNoInteractions(adminRepo);
    }

    @Test
    void testFindUserByRoleAndId_InvalidRole() {
        User result = authService.findUserByRoleAndId("INVALID", 1L);

        assertNull(result);
        verifyNoInteractions(adminRepo, touristRepo);
    }

    @Test
    void testFindUserByEmail_Found() {
        when(userRepo.findByEmail("user@example.com")).thenReturn(Optional.of(mockAdmin));

        User result = authService.findUserByEmail("user@example.com");

        assertNotNull(result);
        assertEquals("encodedPassword", result.getPassword());
        verify(userRepo).findByEmail("user@example.com");
    }

    @Test
    void testFindUserByEmail_NotFound() {
        when(userRepo.findByEmail("missing@example.com")).thenReturn(Optional.empty());

        User result = authService.findUserByEmail("missing@example.com");

        assertNull(result);
        verify(userRepo).findByEmail("missing@example.com");
    }

    @Test
    void testAuthenticate_MatchingPassword() {
        try (var mocked = mockStatic(PasswordEncoderUtil.class)) {
            mocked.when(() -> PasswordEncoderUtil.matches("raw", "encoded")).thenReturn(true);

            assertTrue(authService.authenticate("raw", "encoded"));
            mocked.verify(() -> PasswordEncoderUtil.matches("raw", "encoded"));
        }
    }

    @Test
    void testAuthenticate_NonMatchingPassword() {
        try (var mocked = mockStatic(PasswordEncoderUtil.class)) {
            mocked.when(() -> PasswordEncoderUtil.matches("raw", "encoded")).thenReturn(false);

            assertFalse(authService.authenticate("raw", "encoded"));
            mocked.verify(() -> PasswordEncoderUtil.matches("raw", "encoded"));
        }
    }
}
