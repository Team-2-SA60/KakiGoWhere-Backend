package team2.kakigowherebackend.controller;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team2.kakigowherebackend.dto.PlaceDTO;
import team2.kakigowherebackend.model.Place;
import team2.kakigowherebackend.service.ManagePlaceService;

@RestController
@RequestMapping("/api/admin/place")
public class ManagePlaceController {

    private final ManagePlaceService mpService;

    public ManagePlaceController(ManagePlaceService managePlaceService) {
        this.mpService = managePlaceService;
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
}
