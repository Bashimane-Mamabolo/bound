package com.bash.boundbackend.modules.book.dto;

import com.bash.boundbackend.modules.book.dto.request.FeedbackRequest;
import com.bash.boundbackend.modules.book.entity.Book;
import com.bash.boundbackend.modules.book.entity.Feedback;
import org.springframework.stereotype.Service;

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
}
