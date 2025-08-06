package team2.kakigowherebackend.service;

import java.util.List;
import org.springframework.stereotype.Service;
import team2.kakigowherebackend.model.InterestCategory;
import team2.kakigowherebackend.repository.InterestCategoryRepository;

@Service
public class InterestServiceImpl implements InterestService {
    private final InterestCategoryRepository interestCategoryRepo;

    public InterestServiceImpl(InterestCategoryRepository interestCategoryRepo) {
        this.interestCategoryRepo = interestCategoryRepo;
    }

    @Override
    public List<InterestCategory> getAllInterestCategories() {
        return interestCategoryRepo.findAllInOrder();
    }
}
