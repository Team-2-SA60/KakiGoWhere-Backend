package team2.kakigowherebackend.service;

import java.net.URI;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    String download(URI imageUri);

    String upload(MultipartFile imageFile);
}
