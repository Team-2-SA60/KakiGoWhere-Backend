package team2.kakigowherebackend.service;

import java.util.List;
import team2.kakigowherebackend.dto.ExportRatingDTO;

public interface ExportRatingService {
    void exportRatings();
    void buildCsv(List<ExportRatingDTO> ratings);
    String escapeCsv(String input);
    void writeToCsv(String csvContent);
}
