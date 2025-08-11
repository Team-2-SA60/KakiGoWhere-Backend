package team2.kakigowherebackend.interceptors;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import team2.kakigowherebackend.service.StatService;

class PlaceVisitInterceptorTest {

    private MockMvc mockMvc;
    private StatService statService;

    // stub controller to exercise the interceptor
    @RestController
    static class PlaceControllerStub {
        // match your real controller: /api/places/id/{placeId}
        @GetMapping("/api/places/id/{placeId}")
        public ResponseEntity<String> get(@PathVariable("placeId") Long placeId) {
            if (placeId == 999999L) {
                return ResponseEntity.status(404).body("not found");
            }
            return ResponseEntity.ok("ok");
        }
    }

    @BeforeEach
    void setup() {
        statService = mock(StatService.class);
        when(statService.addPlaceVisit(ArgumentMatchers.anyLong())).thenReturn(LocalDate.now());

        PlaceVisitInterceptor interceptor = new PlaceVisitInterceptor(statService);

        mockMvc =
                MockMvcBuilders.standaloneSetup(new PlaceControllerStub())
                        .addInterceptors(interceptor)
                        .build();
    }

    @Test
    void samePlace_onlyCountsOnce_perSession() throws Exception {
        MockHttpSession session = new MockHttpSession();

        mockMvc.perform(get("/api/places/id/1").session(session)).andExpect(status().isOk());
        mockMvc.perform(get("/api/places/id/1").session(session)).andExpect(status().isOk());

        verify(statService, times(1)).addPlaceVisit(1L);
    }

    @Test
    void differentPlaces_countSeparately_perSession() throws Exception {
        MockHttpSession session = new MockHttpSession();

        mockMvc.perform(get("/api/places/id/1").session(session)).andExpect(status().isOk());
        mockMvc.perform(get("/api/places/id/2").session(session)).andExpect(status().isOk());

        verify(statService, times(1)).addPlaceVisit(1L);
        verify(statService, times(1)).addPlaceVisit(2L);
    }

    @Test
    void newSession_countsAgain() throws Exception {
        MockHttpSession s1 = new MockHttpSession();
        MockHttpSession s2 = new MockHttpSession();

        mockMvc.perform(get("/api/places/id/1").session(s1)).andExpect(status().isOk());
        mockMvc.perform(get("/api/places/id/1").session(s2)).andExpect(status().isOk());

        verify(statService, times(2)).addPlaceVisit(1L);
    }

    @Test
    void non2xx_isNotCounted() throws Exception {
        MockHttpSession session = new MockHttpSession();

        mockMvc.perform(get("/api/places/id/999999").session(session))
                .andExpect(status().isNotFound());

        verify(statService, never()).addPlaceVisit(999999L);
    }

    @Test
    void nextDay_resetsVisitedSet_countsAgain() throws Exception {
        MockHttpSession session = new MockHttpSession();

        // day 1
        mockMvc.perform(get("/api/places/id/1").session(session)).andExpect(status().isOk());
        verify(statService, times(1)).addPlaceVisit(1L);

        // simulate overnight: set lastDate to yesterday and seed visited set with 1
        session.setAttribute("visitedPlacesDate", LocalDate.now().minusDays(1));
        Set<Long> visited = new HashSet<>();
        visited.add(1L);
        session.setAttribute("visitedPlaces", visited);

        // day 2 (today again in code): should count again after reset
        mockMvc.perform(get("/api/places/id/1").session(session)).andExpect(status().isOk());
        verify(statService, times(2)).addPlaceVisit(1L);
    }
}
