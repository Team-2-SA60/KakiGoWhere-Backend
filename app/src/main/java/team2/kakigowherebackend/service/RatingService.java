package team2.kakigowherebackend.service;

import java.util.List;

import team2.kakigowherebackend.dto.RatingSummaryDTO;
import team2.kakigowherebackend.dto.RatingItemDTO;
import team2.kakigowherebackend.dto.RatingRequestDTO;

public interface RatingService {
    RatingSummaryDTO getSummary(long placeId);
    RatingItemDTO getMyRatingItem(long placeId, long touristId);
    List<RatingItemDTO> getOtherRatings(long placeId, long touristId);
    RatingItemDTO submitOrUpdate(long placeId, long touristId, RatingRequestDTO request);
}