package com.bash.boundbackend.modules.book.service;

import com.bash.boundbackend.common.exception.OperationNotPermittedException;
import com.bash.boundbackend.common.utils.PageResponse;
import com.bash.boundbackend.modules.auth.entity.User;
import com.bash.boundbackend.modules.book.dto.FeedbackMapper;
import com.bash.boundbackend.modules.book.dto.request.FeedbackRequest;
import com.bash.boundbackend.modules.book.dto.response.FeedbackResponse;
import com.bash.boundbackend.modules.book.entity.Book;
import com.bash.boundbackend.modules.book.entity.Feedback;
import com.bash.boundbackend.modules.book.repository.BookRepository;
import com.bash.boundbackend.modules.book.repository.BookTransactionHistoryRepository;
import com.bash.boundbackend.modules.book.repository.FeedbackRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final BookRepository bookRepository;
    private final FeedbackMapper feedbackMapper;
    private final BookTransactionHistoryRepository bookTransactionHistoryRepository;

    public Integer saveFeedback(FeedbackRequest feedbackRequest, Authentication connectedUser) {
        Book book = bookRepository.findById(feedbackRequest.bookId())
                .orElseThrow(()-> new EntityNotFoundException("No book found with Id: " + feedbackRequest.bookId()));

        if (book.isArchived() || !book.isShareable()) {
            throw new OperationNotPermittedException("You cannot give feedback to an archived or not shareable book");
        }

        User user = (User) connectedUser.getPrincipal();

        if (Objects.equals(user.getId(), book.getOwner().getId())) {
            throw new OperationNotPermittedException("You cannot give feedback on your own book");
        }

        bookTransactionHistoryRepository.findBookByUserIdAndBookId(book.getId(), user.getId())
                .orElseThrow(() -> new OperationNotPermittedException("You cannot give feedback on a book you did not borrow"));
//        if (!transactionHistory.isBookReturned()) {
//            throw new OperationNotPermittedException("You cannot give feedback on a non-returned book");
//        }
        Feedback feedback = feedbackMapper.toFeedback(feedbackRequest);
        return feedbackRepository.save(feedback).getId();

    }

    public PageResponse<FeedbackResponse> FindAllFeedbacksByBook(Authentication connectedUser, int page, int size, Integer bookId) {

        Pageable pageable = PageRequest.of(page, size);
        User user = (User) connectedUser.getPrincipal();
        Page<Feedback> feedbacks = feedbackRepository.findAllFeedbacksByBookId(bookId, pageable);
        List<FeedbackResponse> feedbackResponses = feedbacks.stream()
                .map(feedback -> feedbackMapper.toFeedbackResponse(feedback, user.getId()))
                .toList();

        return new PageResponse<>(
                feedbackResponses,
                feedbacks.getNumber(),
                feedbacks.getSize(),
                feedbacks.getTotalElements(),
                feedbacks.getTotalPages(),
                feedbacks.isFirst(),
                feedbacks.isLast()
        );
    }
}
