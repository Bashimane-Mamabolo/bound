package com.bash.boundbackend.common.utils;

import com.bash.boundbackend.modules.book.entity.Book;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.io.File.separator;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileStorageService {

    @Value("${application.file.upload.images-output-path}")
    private String fileUploadPath;

    public String saveFile(
            @NonNull MultipartFile sourceFile,
            @NonNull Integer userId
    ) {

        final String fileUploadSubPath = "users" + separator + userId;
        return uploadSourceFile(sourceFile, fileUploadSubPath);
    }

    private String uploadSourceFile(
            @NonNull MultipartFile sourceFile,
            @NonNull String fileUploadSubPath
    ) {
        final String fullUploadPath = fileUploadPath + separator + fileUploadSubPath;

        // Create the folder book-images/user/{userId}
        // This is where we will upload sourceFiles in
        File file = new File(fullUploadPath);
        if (!file.exists()) {
            boolean folderCreated = file.mkdirs();
            if (!folderCreated) {
                log.warn("Could not create target-folder {}", fullUploadPath);
                return null;  // Do not proceed to upload the file
            }
        }
        // Extract file extension from the sourceFile (e.g. .png, jpeg, pdf...etc)
        final String sourceFileExtension = getSourceFileExtension(sourceFile.getOriginalFilename());
        // ./book-images/user/1/1730419652136.jpg
        String targetDestFilePath = fullUploadPath + separator + System.currentTimeMillis() + "." + sourceFileExtension;

        Path targetPath = Paths.get(targetDestFilePath);
        try {
            Files.write(targetPath, sourceFile.getBytes()); // write bytes to a files
            log.info("File saved to {}", targetDestFilePath);
            return targetDestFilePath;

        } catch (IOException e) {
//            throw new RuntimeException(e);
            log.info("File not saved", e);
        }
        return null;
    }
    // e.g. harry-potter.jpg
    // we want string jpg and must be lowercase
    private String getSourceFileExtension(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            log.warn("File name is null or empty {}", fileName);
            return null;
        }
        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex == -1) {
            log.warn("File name extension does not exist or incorrect {}", lastDotIndex);
            return null;
        }

        return fileName.substring(lastDotIndex + 1).toLowerCase(); // jpg or jpeg ... do not include dot.
    }
}
