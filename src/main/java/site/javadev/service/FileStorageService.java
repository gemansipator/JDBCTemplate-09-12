package site.javadev.service;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {   //сервис для загрузки изображений

    private static final String UPLOAD_DIR = "uploads/covers/"; // Папка для хранения обложек

    public FileStorageService() {
        // Создаем директорию, если она не существует
        try {
            Files.createDirectories(Paths.get(UPLOAD_DIR));
        } catch (IOException e) {
            throw new RuntimeException("Не удалось создать директорию для загрузки файлов: " + UPLOAD_DIR, e);
        }
    }

    public String storeFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Файл не может быть пустым");
        }

        try {
            // Генерируем уникальное имя файла
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(UPLOAD_DIR + fileName);

            // Сохраняем файл
            Files.copy(file.getInputStream(), filePath);

            // Возвращаем относительный путь
            return "/" + UPLOAD_DIR + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Не удалось сохранить файл: " + e.getMessage(), e);
        }
    }

    public Path getFilePath(String fileName) {
        return Paths.get(UPLOAD_DIR + fileName);
    }
}