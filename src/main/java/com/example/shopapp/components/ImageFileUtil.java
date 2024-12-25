package com.example.shopapp.components;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Component
public class ImageFileUtil {

    public String storeFile(MultipartFile file) throws IOException {
        if (!isImageFile(file) || file.getOriginalFilename() == null) {
            throw new IOException("Invalid image format");
        }
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());


        // Change file name
        String uniqueFileName = UUID.randomUUID() + "_" + fileName;

        Path uploadDir = Paths.get("uploads/images");
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }

        Path destination = uploadDir.resolve(uniqueFileName);
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFileName;
    }

    public boolean isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image");
    }

    public String validateAndStoreThumbnail(MultipartFile fileThumbnail) throws IOException {
        if (fileThumbnail == null || fileThumbnail.isEmpty()) {
            return "";
        }

        if (fileThumbnail.getSize() > 10 * 1024 * 1024) {
            throw new IllegalArgumentException("Thumbnail size exceeds 10MB");
        }

        return storeFile(fileThumbnail);
    }
}
