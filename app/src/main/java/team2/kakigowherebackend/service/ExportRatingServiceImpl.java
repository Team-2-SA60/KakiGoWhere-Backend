package team2.kakigowherebackend.service;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import team2.kakigowherebackend.dto.ExportRatingDTO;
import team2.kakigowherebackend.model.Rating;

@Slf4j
@Service
public class ExportRatingServiceImpl implements ExportRatingService {
    @Value("${csv.dir}")
    private String csvDir;

    private final RatingService ratingService;

    public ExportRatingServiceImpl(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @Override
    public void exportRatings() {
        List<Rating> ratings = ratingService.getAllRatings();
        List<ExportRatingDTO> dtos = ratings.stream()
                .map(ExportRatingDTO::new)
                .toList();
        buildCsv(dtos);
    }

    @Override
    public void buildCsv(List<ExportRatingDTO> ratings) {
        String header = "id,comment,rating,placeId,touristId\n";

        StringBuilder csv = new StringBuilder(header);
        for (ExportRatingDTO r : ratings) {
            csv.append(r.getId()).append(",");
            // Escape any quotes or commas in the comment, wrap in quotes
            csv.append("\"").append(escapeCsv(r.getComment())).append("\",");
            csv.append(r.getRating()).append(",");
            csv.append(r.getPlaceId()).append(",");
            csv.append(r.getTouristId()).append("\n");
        }

        writeToCsv(csv.toString());
    }

    @Override
    public String escapeCsv(String input) {
        if (input == null) {
            return "";
        }
        String escaped = input.replace("\"", "\"\"");
        if (escaped.contains(",") || escaped.contains("\"") || escaped.contains("\n")) {
            return "\"" + escaped + "\"";
        }
        return escaped;
    }

    @Override
    public void writeToCsv(String csvContent) {
        try {
            Path dirPath = Paths.get(csvDir);
            Path tmpPath = dirPath.resolve("ratings.tmp");
            Path outPath = dirPath.resolve("ratings.csv");

            Files.createDirectories(dirPath);
            Files.writeString(tmpPath, csvContent);
            Files.move(tmpPath, outPath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);

            log.info("Exported Ratings CSV to: {}", outPath);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to write ratings CSV", e);
        }
    }
}
