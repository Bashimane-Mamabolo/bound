package com.bash.boundbackend.modules.book.dto.request;

import jakarta.validation.constraints.NotBlank;

public record BookRequest(
        Integer id,
        @NotBlank(message = "100 error, needs to be defined")
        String title,
        @NotBlank(message = "101 error, needs to be defined")
        String authorName,
        @NotBlank(message = "102 error, needs to be defined")
        String isbn,
        @NotBlank(message = "103  error, needs to be defined")
        String synopsis,
        boolean shareable
) {
}
