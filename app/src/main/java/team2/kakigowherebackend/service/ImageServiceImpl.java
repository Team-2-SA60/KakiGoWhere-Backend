package team2.kakigowherebackend.service;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageServiceImpl implements ImageService {

    @Value("${upload.dir}")
    private String uploadDir;

    private final RestTemplate restTemplate;

    public ImageServiceImpl() {
        this.restTemplate = new RestTemplate();
    }

    @Override
    public String download(URI imageUrl, String fileName) {

        byte[] imageBytes = restTemplate.getForObject(imageUrl, byte[].class);

        if (imageBytes == null) return null;

        try {
            Path dirPath = Paths.get(uploadDir);
            Files.createDirectories(dirPath);
            Path filePath = dirPath.resolve(fileName + ".jpg");
            Files.write(filePath, imageBytes);

            return filePath.toString().replace("./", "");

        } catch (IOException e) {
            throw new RuntimeException("Failed to write image", e);
        }
    }

    @Override
    public String upload(MultipartFile imageFile) {
        return "";
    }
}
