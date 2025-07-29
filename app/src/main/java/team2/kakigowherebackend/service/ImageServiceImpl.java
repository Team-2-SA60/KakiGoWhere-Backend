package team2.kakigowherebackend.service;

import java.net.URI;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageServiceImpl implements ImageService {
    @Override
    public String download(URI imageUri) {
        return "";
    }

    @Override
    public String upload(MultipartFile imageFile) {
        return "";
    }
}
