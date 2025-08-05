package team2.kakigowherebackend.controller;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team2.kakigowherebackend.dto.PlaceDTO;
import team2.kakigowherebackend.model.Bookmark;
import team2.kakigowherebackend.service.BookmarkService;

@RestController
@RequestMapping("/api/bookmark")
public class BookmarkController {
    private final BookmarkService bookmarkService;

    public BookmarkController(BookmarkService bookmarkService) {
        this.bookmarkService = bookmarkService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<PlaceDTO>> getAllBookmarksByTourist(HttpSession session) {
        long touristId = (long) session.getAttribute("tourist");

        List<Bookmark> bookmarks = bookmarkService.findAllByTouristId(touristId);
        List<PlaceDTO> placeDTOs =
                bookmarks.stream().map(bookmark -> new PlaceDTO(bookmark.getPlace())).toList();

        return ResponseEntity.status(HttpStatus.OK).body(placeDTOs);
    }

    @PostMapping("/toggle")
    public ResponseEntity<String> toggleBookmark(@RequestParam long placeId, HttpSession session) {
        long touristId = (long) session.getAttribute("tourist");

        try {
            boolean toggled = bookmarkService.toggleBookmark(placeId, touristId);
            if (toggled) {
                return ResponseEntity.status(HttpStatus.CREATED).body("Bookmark created");
            } else {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Bookmark deleted");
            }
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected error: " + e.getMessage());
        }
    }
}
