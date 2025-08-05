package team2.kakigowherebackend.service;

import java.util.List;
import team2.kakigowherebackend.model.Bookmark;

public interface BookmarkService {
    List<Bookmark> findAllByTouristId(long touristId);

    boolean toggleBookmark(long placeId, long touristId);

    Bookmark addBookmark(long placeId, long touristId);

    void deleteBookmark(long bookmarkId);
}
