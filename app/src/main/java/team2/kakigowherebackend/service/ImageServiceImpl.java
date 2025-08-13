package team2.kakigowherebackend.service;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.imageio.ImageIO;
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
    public String upload(MultipartFile imageFile, String imageName, int maxWidth, int maxHeight) {
        try {
            Path dirPath = Paths.get(uploadDir);
            Files.createDirectories(dirPath);

            // original image
            BufferedImage original = ImageIO.read(imageFile.getInputStream());

            // calculate new dimensions to aspect ratio
            double ratio =
                    Math.min(
                            (double) maxWidth / original.getWidth(),
                            (double) maxHeight / original.getHeight());
            int newWidth = (int) (original.getWidth() * ratio);
            int newHeight = (int) (original.getHeight() * ratio);

            // resize
            Image tmp = original.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
            BufferedImage resized =
                    new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = resized.createGraphics();
            g2d.drawImage(tmp, 0, 0, null);
            g2d.dispose();

            // save
            Path filePath = dirPath.resolve(imageName + ".jpg");
            ImageIO.write(resized, "jpg", filePath.toFile());

            return filePath.toString().replace("./", "/");
        } catch (IOException e) {
            throw new RuntimeException("Failed to write image", e);
        }
    }
}
