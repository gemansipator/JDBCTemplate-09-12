package site.javadev.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {

    private static final String UPLOAD_DIR = "uploads/covers/";
    private final Path uploadPath;

    public FileStorageService() {
        this.uploadPath = Paths.get(UPLOAD_DIR);
        try {
            Files.createDirectories(uploadPath);
            System.out.println("Upload directory initialized at: " + uploadPath.toAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException("Не удалось создать директорию для загрузки файлов: " + UPLOAD_DIR, e);
        }
    }

    public String storeFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        try {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath);
            System.out.println("File saved at: " + filePath.toAbsolutePath());
            return "/uploads/covers/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Не удалось сохранить файл: " + file.getOriginalFilename(), e);
        }
    }

    public Path getFilePath(String fileName) {
        return uploadPath.resolve(fileName);
    }

    public boolean deleteFile(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return false;
        }
        try {
            Path filePath = uploadPath.resolve(fileName);
            return Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Не удалось удалить файл: " + fileName, e);
        }
    }
}