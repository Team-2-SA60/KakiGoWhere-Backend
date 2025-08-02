package team2.kakigowherebackend.service;

import org.springframework.core.io.Resource;
import team2.kakigowherebackend.dto.ExportPlaceDTO;
import team2.kakigowherebackend.dto.PlaceDTO;
import team2.kakigowherebackend.dto.PlaceDetailDTO;

import java.net.MalformedURLException;
import java.util.List;

public interface PlaceService {

    List<PlaceDTO> getPlaces();

    List<ExportPlaceDTO> getPlacesForMl();

    PlaceDetailDTO getPlaceDetail(long placeId);

    Resource getImageByPlaceId(long placeId) throws MalformedURLException;
}
