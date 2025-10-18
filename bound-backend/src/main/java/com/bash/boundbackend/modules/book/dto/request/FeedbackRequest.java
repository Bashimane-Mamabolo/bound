package com.bash.boundbackend.modules.book.dto.request;

import jakarta.validation.constraints.*;
import lombok.Builder;


@Builder
public record FeedbackRequest(
        @Positive(message = "200")
        @Min(value = 0, message = "201")
        @Max(value = 5, message = "202")
        Double rating,
        @NotBlank(message = "203")
        String comment,
        @NotNull(message = "204")
        Integer bookId
) {
}
