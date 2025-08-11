package team2.kakigowherebackend;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import team2.kakigowherebackend.dto.TouristUpdateRequestDTO;
import team2.kakigowherebackend.exception.BadRequestException;
import team2.kakigowherebackend.exception.ResourceNotFoundException;
import team2.kakigowherebackend.model.InterestCategory;
import team2.kakigowherebackend.model.Tourist;
import team2.kakigowherebackend.repository.InterestCategoryRepository;
import team2.kakigowherebackend.repository.TouristRepository;
import team2.kakigowherebackend.service.TouristServiceImpl;

class TouristServiceTest {

    @Mock private TouristRepository touristRepository;

    @Mock private InterestCategoryRepository interestCategoryRepository;

    @InjectMocks private TouristServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private static InterestCategory ic() {
        return new InterestCategory();
    }

    @Test
    void updateTourist_existing_replacesNameAndInterests_andSavesSameInstance() {
        Long id = 7L;
        Tourist existing = new Tourist();
        existing.setName("Old");
        existing.setInterestCategories(new ArrayList<>());

        TouristUpdateRequestDTO dto = mock(TouristUpdateRequestDTO.class);
        when(dto.getName()).thenReturn("Alice");
        when(dto.getInterestCategoryIds()).thenReturn(List.of(1L, 2L));

        List<InterestCategory> interests = List.of(ic(), ic());

        when(touristRepository.findById(id)).thenReturn(Optional.of(existing));
        when(interestCategoryRepository.findAllById(List.of(1L, 2L))).thenReturn(interests);
        when(touristRepository.save(any(Tourist.class))).thenAnswer(inv -> inv.getArgument(0));

        Tourist saved = service.updateTourist(id, dto);

        assertSame(existing, saved);
        assertEquals("Alice", existing.getName());
        assertSame(interests, existing.getInterestCategories());
        verify(touristRepository).save(same(existing));
    }

    @Test
    void updateTourist_missing_throwsResourceNotFound() {
        Long id = 99L;
        TouristUpdateRequestDTO dto = mock(TouristUpdateRequestDTO.class);
        when(touristRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.updateTourist(id, dto));
        verify(touristRepository, never()).save(any());
    }

    @Test
    void updateTourist_moreThan3_throwsBadRequest_andDoesNotSave() {
        Long id = 7L;
        Tourist existing = new Tourist();
        TouristUpdateRequestDTO dto = mock(TouristUpdateRequestDTO.class);
        when(dto.getName()).thenReturn("Bob");
        when(dto.getInterestCategoryIds()).thenReturn(List.of(1L, 2L, 3L, 4L));

        when(touristRepository.findById(id)).thenReturn(Optional.of(existing));
        List<InterestCategory> tooMany = List.of(ic(), ic(), ic(), ic());
        when(interestCategoryRepository.findAllById(any())).thenReturn(tooMany);

        assertThrows(BadRequestException.class, () -> service.updateTourist(id, dto));
        verify(touristRepository, never()).save(any());
    }

    @Test
    void updateTourist_replacesNotAppends() {
        Long id = 7L;
        Tourist existing = new Tourist();
        InterestCategory old = ic();
        existing.setInterestCategories(new ArrayList<>(List.of(old)));

        TouristUpdateRequestDTO dto = mock(TouristUpdateRequestDTO.class);
        when(dto.getName()).thenReturn("Carl");
        when(dto.getInterestCategoryIds()).thenReturn(List.of(2L, 3L));

        List<InterestCategory> newList = List.of(ic(), ic());

        when(touristRepository.findById(id)).thenReturn(Optional.of(existing));
        when(interestCategoryRepository.findAllById(List.of(2L, 3L))).thenReturn(newList);
        when(touristRepository.save(any(Tourist.class))).thenAnswer(inv -> inv.getArgument(0));

        Tourist out = service.updateTourist(id, dto);

        assertSame(newList, out.getInterestCategories());
        assertTrue(out.getInterestCategories().stream().noneMatch(cat -> cat == old));
        verify(touristRepository).save(same(existing));
    }
}
