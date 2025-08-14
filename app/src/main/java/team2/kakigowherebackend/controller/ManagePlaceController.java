package team2.kakigowherebackend.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team2.kakigowherebackend.dto.GoogleSearchDTO;
import team2.kakigowherebackend.dto.ManagePlaceDetailDTO;
import team2.kakigowherebackend.dto.PlaceDTO;
import team2.kakigowherebackend.model.Place;
import team2.kakigowherebackend.service.ManagePlaceService;
import team2.kakigowherebackend.service.PlaceService;

@RestController
@RequestMapping("/api/admin/place")
public class ManagePlaceController {

    private final ManagePlaceService mpService;
    private final PlaceService placeService;

    public ManagePlaceController(ManagePlaceService managePlaceService, PlaceService placeService) {
        this.mpService = managePlaceService;
        this.placeService = placeService;
    }

    @GetMapping
    public ResponseEntity<Page<PlaceDTO>> getPlaces(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(defaultValue = "") String search) {

        Page<Place> places = mpService.getPlaces(page, pageSize, search);
        List<PlaceDTO> dtoList = places.stream().map(PlaceDTO::new).toList();

        Page<PlaceDTO> pageDtoList =
                new PageImpl<>(dtoList, places.getPageable(), places.getTotalElements());

        return ResponseEntity.status(HttpStatus.OK).body(pageDtoList);
    }

    @GetMapping("/id/{placeId}")
    public ResponseEntity<ManagePlaceDetailDTO> getPlaceById(@PathVariable long placeId) {
        Place place = placeService.getPlaceDetail(placeId);

        if (place == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        return ResponseEntity.status(HttpStatus.OK).body(new ManagePlaceDetailDTO(place));
    }

    @PostMapping("/create")
    public ResponseEntity<ManagePlaceDetailDTO> createPlace(
            @Valid @RequestBody ManagePlaceDetailDTO newPlace) {
        Place createdPlace = mpService.createPlace(newPlace);

        if (createdPlace == null) return ResponseEntity.status(HttpStatus.CONFLICT).build();

        return ResponseEntity.status(HttpStatus.OK).body(new ManagePlaceDetailDTO(createdPlace));
    }

    @PutMapping("/update")
    public ResponseEntity<ManagePlaceDetailDTO> updatePlace(
            @Valid @RequestBody ManagePlaceDetailDTO updatedPlace) {
        Place place = mpService.updatePlace(updatedPlace);

        if (place == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        return ResponseEntity.status(HttpStatus.OK).body(new ManagePlaceDetailDTO(place));
    }

    @PostMapping("/image/upload")
    public ResponseEntity<String> uploadImage(
            @RequestParam(value = "placeId", defaultValue = "-1") long placeId,
            @RequestParam("image") MultipartFile image) {
        if (placeId == -1)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No place ID provided");
        if (image == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No image provided");

        String contentType = image.getContentType();
        if (!("image/jpeg".equals(contentType) || "image/png".equals(contentType))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Only JPEG or PNG images are allowed");
        }

        String imagePath = mpService.uploadPlaceImage(placeId, image);
        return ResponseEntity.status(HttpStatus.OK).body(imagePath);
    }

    @GetMapping("/google")
    public ResponseEntity<List<GoogleSearchDTO>> searchPlacesByText(
            @RequestParam(defaultValue = "") String search) {

        if (search.isEmpty()) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        List<GoogleSearchDTO> searchList = mpService.searchPlacesByText(search);

        if (searchList.isEmpty()) return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        return ResponseEntity.status(HttpStatus.OK).body(searchList);
    }

    @PostMapping("/google/add")
    public ResponseEntity<Place> addPlaceByGoogleId(@RequestBody GoogleSearchDTO googleSearchDTO) {
        if (googleSearchDTO.getGoogleId() == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        Place createdPlace = mpService.savePlaceByGoogleId(googleSearchDTO.getGoogleId());
        if (createdPlace == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        return ResponseEntity.status(HttpStatus.OK).body(createdPlace);
    }
}
