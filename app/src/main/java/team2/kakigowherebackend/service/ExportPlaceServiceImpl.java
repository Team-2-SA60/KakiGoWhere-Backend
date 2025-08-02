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
import team2.kakigowherebackend.dto.ExportPlaceDTO;
import team2.kakigowherebackend.model.Place;

@Slf4j
@Service
public class ExportPlaceServiceImpl implements ExportPlaceService {

    @Value("${csv.dir}")
    private String csvDir;

    private final PlaceService placeService;

    public ExportPlaceServiceImpl(PlaceService placeService) {
        this.placeService = placeService;
    }

    @Override
    public void exportPlaces() {
        List<Place> places = placeService.getPlaces();
        List<ExportPlaceDTO> exportPlaceDTOS = places.stream().map(ExportPlaceDTO::new).toList();
        buildCsv(exportPlaceDTOS);
    }

    @Override
    public void buildCsv(List<ExportPlaceDTO> places) {
        String header = "id,googleId,name,description,averageRating,interestCategories\n";

        StringBuilder csvContent = new StringBuilder();
        csvContent.append(header);

        places.forEach(
                p -> {
                    csvContent.append(p.getId()).append(",");
                    csvContent.append(p.getGoogleId()).append(",");
                    csvContent.append(escapeCsv(p.getName())).append(",");
                    csvContent.append(escapeCsv(p.getDescription())).append(",");
                    csvContent.append(p.getAverageRating()).append(",");

                    String interestCategories = String.join(",", p.getInterestCategories());
                    csvContent.append("\"").append(interestCategories).append("\"").append("\n");
                });
        writeToCsv(csvContent.toString());
    }

    // Escapes double quotes and wraps in quotes if necessary
    @Override
    public String escapeCsv(String input) {
        if (input == null) return "";
        String escaped = input.replace("\"", "\"\"");
        if (escaped.contains(",") || escaped.contains("\"") || escaped.contains("\n")) {
            return "\"" + escaped + "\"";
        }
        return escaped;
    }

    @Override
    public void writeToCsv(String csvContent) {
        try {
            Path exportPath = Paths.get(csvDir + "places.csv");
            Files.createDirectories(exportPath.getParent());
            Files.writeString(exportPath, csvContent);
            log.info("Exported CSV to: {}", exportPath);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to write CSV", e);
        }
    }
}
