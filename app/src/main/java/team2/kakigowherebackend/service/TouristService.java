package team2.kakigowherebackend.service;

import org.springframework.stereotype.Service;
import team2.kakigowherebackend.dto.TouristUpdateRequestDTO;
import team2.kakigowherebackend.exception.BadRequestException;
import team2.kakigowherebackend.exception.ResourceNotFoundException;
import team2.kakigowherebackend.model.InterestCategory;
import team2.kakigowherebackend.model.Tourist;
import team2.kakigowherebackend.repository.InterestCategoryRepository;
import team2.kakigowherebackend.repository.TouristRepository;

import java.util.List;

@Service
public class TouristService {
    private final TouristRepository touristRepository;
    private final InterestCategoryRepository interestCategoryRepository;

    public TouristService(TouristRepository touristRepository,
                          InterestCategoryRepository interestCategoryRepository) {
        this.touristRepository = touristRepository;
        this.interestCategoryRepository = interestCategoryRepository;
    }

    /**
     * Update an existing Tourist: set name and replace interest categories.
     * Enforces a maximum of 3 categories.
     */
    public Tourist updateTourist(Long id, TouristUpdateRequestDTO dto) {
        Tourist existing = touristRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tourist", "id", id));

        existing.setName(dto.getName());

        List<InterestCategory> interests =
                interestCategoryRepository.findAllById(dto.getInterestCategoryIds());
        if (interests.size() > 3) {
            throw new BadRequestException("A tourist can have at most 3 interest categories");
        }
        existing.setInterestCategories(interests);

        return touristRepository.save(existing);
    }
}