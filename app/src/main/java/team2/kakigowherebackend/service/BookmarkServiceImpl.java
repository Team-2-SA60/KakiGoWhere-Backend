package team2.kakigowherebackend.service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import team2.kakigowherebackend.model.Bookmark;
import team2.kakigowherebackend.model.Place;
import team2.kakigowherebackend.model.Tourist;
import team2.kakigowherebackend.repository.BookmarkRepository;
import team2.kakigowherebackend.repository.PlaceRepository;
import team2.kakigowherebackend.repository.TouristRepository;

@Service
public class BookmarkServiceImpl implements BookmarkService {

    private final BookmarkRepository bookmarkRepo;
    private final TouristRepository touristRepo;
    private final PlaceRepository placeRepo;

    public BookmarkServiceImpl(
            BookmarkRepository bookmarkRepo,
            TouristRepository touristRepo,
            PlaceRepository placeRepo) {
        this.bookmarkRepo = bookmarkRepo;
        this.touristRepo = touristRepo;
        this.placeRepo = placeRepo;
    }

    @Override
    public List<Bookmark> findAllByTouristId(long touristId) {
        return bookmarkRepo.findAllByTouristId(touristId);
    }

    // Toggle bookmark: true = create, false = delete
    @Override
    public boolean toggleBookmark(long placeId, long touristId) {
        Bookmark existing =
                bookmarkRepo.findBookmarkByPlaceIdAndTouristId(placeId, touristId).orElse(null);

        if (existing != null) {
            deleteBookmark(existing.getId());
            return false;
        }

        addBookmark(placeId, touristId);
        return true;
    }

    @Override
    public Bookmark addBookmark(long placeId, long touristId) {
        Tourist tourist =
                touristRepo
                        .findById(touristId)
                        .orElseThrow(() -> new NoSuchElementException("Tourist not found"));
        Place place =
                placeRepo
                        .findById(placeId)
                        .orElseThrow(() -> new NoSuchElementException("Place not found"));

        Bookmark bookmark = new Bookmark();
        bookmark.setTourist(tourist);
        bookmark.setPlace(place);
        bookmark.setBookmarkedDate(LocalDate.now());

        return bookmarkRepo.save(bookmark);
    }

    @Override
    public void deleteBookmark(long bookmarkId) {
        Bookmark bookmark = bookmarkRepo.findById(bookmarkId).orElse(null);
        if (bookmark == null) throw new NoSuchElementException("Bookmark not found");
        bookmarkRepo.delete(bookmark);
    }
}
