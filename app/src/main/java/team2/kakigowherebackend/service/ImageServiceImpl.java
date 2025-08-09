package team2.kakigowherebackend.service;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import team2.kakigowherebackend.repository.PlaceRepository;

@Service
public class ImageServiceImpl implements ImageService {

    // Where to download or upload image files to, indicated in "application.properties"
    @Value("${upload.dir}")
    private String uploadDir;

    private final RestTemplate restTemplate;
    private final PlaceRepository placeRepo;

    public ImageServiceImpl(PlaceRepository placeRepo) {
        this.restTemplate = new RestTemplate();
        this.placeRepo = placeRepo;
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

            return filePath.toString().replace("./", "/");

        } catch (IOException e) {
            throw new RuntimeException("Failed to write image", e);
        }
    }

    @Override
    public String upload(MultipartFile imageFile, String imageName) {
        try {
            Path dirPath = Paths.get(uploadDir);
            Files.createDirectories(dirPath);
            Path filePath = dirPath.resolve(imageName + ".jpg");
            Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return filePath.toString().replace("./", "/");
        } catch (IOException e) {
            throw new RuntimeException("Failed to write image", e);
        }
    }
}
