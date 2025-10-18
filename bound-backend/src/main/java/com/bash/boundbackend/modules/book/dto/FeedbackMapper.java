package com.bash.boundbackend.modules.book.dto;

import com.bash.boundbackend.modules.book.dto.request.FeedbackRequest;
import com.bash.boundbackend.modules.book.dto.response.FeedbackResponse;
import com.bash.boundbackend.modules.book.entity.Book;
import com.bash.boundbackend.modules.book.entity.Feedback;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class FeedbackMapper {

    public Feedback toFeedback(FeedbackRequest feedbackRequest) {
        return Feedback.builder()
                .rating(feedbackRequest.rating()) // No rating update
                .comment(feedbackRequest.comment())  // we can only update the comment // TODO -later
                .book(Book.builder()
                        .id(feedbackRequest.bookId())
                        .build()
                )
                .build();
    }

    public FeedbackResponse toFeedbackResponse(Feedback feedback, Integer userId) {
        return FeedbackResponse.builder()
                .rating(feedback.getRating())
                .comment(feedback.getComment())
                .ownFeedback(Objects.equals(feedback.getCreatedBy(),userId)) // boolean
                .build();
    }
}
