package com.bash.boundbackend.modules.book.controller;

import com.bash.boundbackend.common.utils.PageResponse;
import com.bash.boundbackend.modules.book.dto.request.FeedbackRequest;
import com.bash.boundbackend.modules.book.dto.response.FeedbackResponse;
import com.bash.boundbackend.modules.book.service.FeedbackService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/feedbacks")
@RequiredArgsConstructor
@Tag(name = "Feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;


    @PostMapping("/save-feedback")
    public ResponseEntity<Integer> saveFeedback(
            @RequestBody @Valid FeedbackRequest feedbackRequest,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(feedbackService.saveFeedback(feedbackRequest, connectedUser));
    }

    @GetMapping("/book/{book-id}")
    public ResponseEntity<PageResponse<FeedbackResponse>> findAllFeedbacksByBook(
            @PathVariable("book-id") Integer bookId,
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(feedbackService.FindAllFeedbacksByBook(connectedUser, page, size, bookId));
    }

}
