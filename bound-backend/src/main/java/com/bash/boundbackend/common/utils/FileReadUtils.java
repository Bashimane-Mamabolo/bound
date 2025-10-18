package com.bash.boundbackend.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class FileReadUtils {

    /**
     * Reads a file from a given path and returns its contents as a byte array.
     *
     * @param filePath The path to the file as a string.
     * @return The file contents as a byte array.
     */
    public static byte[] readFileFromLocation(String filePath) {
        if (StringUtils.isBlank(filePath)) {
            log.warn("File path is blank. Returning empty byte array.");
            return null;
        }

        Path path = Paths.get(filePath);

        // Optional: Check if the file exists before attempting to read.
        if (Files.notExists(path)) {
            log.warn("No file found in the path: {}", filePath);
            // Propagate the specific exception.
            // throw new java.nio.file.NoSuchFileException(filePath);
        }

        try {
            byte[] fileBytes = Files.readAllBytes(path);
            log.info("Successfully read {} bytes from file: {}", fileBytes.length, path);
            return fileBytes;
        } catch (IOException e) {
            log.warn("Failed to read file from path: {}", filePath);
            // throw e; // Rethrow the exception to allow caller to handle it.
        }
        return null;
    }
}
