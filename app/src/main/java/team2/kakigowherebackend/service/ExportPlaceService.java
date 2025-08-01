package team2.kakigowherebackend.service;

import java.util.List;
import team2.kakigowherebackend.dto.ExportPlaceDTO;

public interface ExportPlaceService {
    void exportPlaces();

    void buildCsv(List<ExportPlaceDTO> places);

    String escapeCsv(String input);

    void writeToCsv(String csvContent);
}
