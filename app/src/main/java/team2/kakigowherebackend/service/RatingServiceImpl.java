package team2.kakigowherebackend.service;

import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import team2.kakigowherebackend.dto.RatingItemDTO;
import team2.kakigowherebackend.dto.RatingRequestDTO;
import team2.kakigowherebackend.dto.RatingSummaryDTO;
import team2.kakigowherebackend.model.Place;
import team2.kakigowherebackend.model.Rating;
import team2.kakigowherebackend.model.Tourist;
import team2.kakigowherebackend.repository.RatingRepository;

@Service
@Transactional
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepo;
    private final PlaceService placeService;

    public RatingServiceImpl(RatingRepository ratingRepo, PlaceService placeService) {
        this.ratingRepo = ratingRepo;
        this.placeService = placeService;
    }

    @Override
    public RatingSummaryDTO getSummary(long placeId) {
        List<Rating> ratings = ratingRepo.findByPlaceId(placeId);
        int count = ratings.size();
        if (count == 0) {
            return new RatingSummaryDTO(0.0, 0);
        }

        int total = 0;
        for (Rating r : ratings) {
            total += r.getRating();
        }
        double average = (double) total / count;
        return new RatingSummaryDTO(average, count);
    }

    @Override
    public RatingItemDTO getMyRatingItem(long placeId, long touristId) {
        return ratingRepo.findMyRatingItemDTO(placeId, touristId).orElse(null);
    }

    @Override
    public List<RatingItemDTO> getOtherRatings(long placeId, long touristId) {
        return ratingRepo.findOtherRatingItemDTOs(placeId, touristId);
    }

    @Override
    public RatingItemDTO submitOrUpdate(long placeId, long touristId, RatingRequestDTO request) {
        // defensive fallback: in case validation did not catch
        if (request.getRating() < 1 || request.getRating() > 5) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Rating must be between 1 and 5");
        }
        if (request.getComment() == null || request.getComment().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Comment cannot be empty");
        }

        Rating rating;
        Optional<Rating> existingRating = ratingRepo.findByPlaceIdAndTouristId(placeId, touristId);
        if (existingRating.isPresent()) {
            rating = existingRating.get();
        } else {
            rating = new Rating();
            Place place = new Place();
            place.setId(placeId);
            Tourist tourist = new Tourist();
            tourist.setId(touristId);
            rating.setPlace(place);
            rating.setTourist(tourist);
        }

        rating.setRating(request.getRating());
        rating.setComment(request.getComment());

        Rating saved = ratingRepo.save(rating);
        String touristName = saved.getTourist().getName();
        return new RatingItemDTO(
                saved.getId(),
                saved.getTourist().getId(),
                touristName,
                saved.getRating(),
                saved.getComment());
    }

    @Override
    public List<Rating> getAllRatings() {
        return ratingRepo.findAll();
    }
}
