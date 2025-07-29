package team2.kakigowherebackend.service.converter;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import team2.kakigowherebackend.dto.PlaceDTO;
import team2.kakigowherebackend.model.Place;

import java.io.IOException;

@Service
public class PlaceDTOConverter {

    private final ResourceLoader resourceLoader;

    public PlaceDTOConverter(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public PlaceDTO convertToDto(Place place) throws IOException {
        PlaceDTO dto = new PlaceDTO();
        dto.setId(place.getId());
        dto.setName(place.getName());
        dto.setDescription(place.getDescription());
        dto.setURL(place.getURL());
        dto.setOpeningHour(place.getOpeningHour());
        dto.setClosingHour(place.getClosingHour());
        dto.setLatitude(place.getLatitude());
        dto.setLongitude(place.getLongitude());
        dto.setActive(place.isActive());

        // set image resource from image path
        PlaceDTO.PlaceImageDTO imageDTO = new PlaceDTO.PlaceImageDTO();
        if (place.getImagePath() != null) imageDTO.setPath(place.getImagePath());
        else imageDTO.setPath("default_image");
        Resource resource = resourceLoader.getResource("classpath:/static/" + place.getImagePath() + ".jpg");
        byte[] imageBytes = StreamUtils.copyToByteArray(resource.getInputStream());
        imageDTO.setData(imageBytes);
        imageDTO.setMimeType("application/octet-stream");

        dto.setImage(imageDTO);

        return dto;
    }

}
