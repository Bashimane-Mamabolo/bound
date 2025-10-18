package com.bash.boundbackend.modules.book.dto;

import com.bash.boundbackend.common.utils.FileReadUtils;
import com.bash.boundbackend.modules.book.dto.request.BookRequest;
import com.bash.boundbackend.modules.book.dto.response.BookResponse;
import com.bash.boundbackend.modules.book.dto.response.BorrowedBookResponse;
import com.bash.boundbackend.modules.book.entity.Book;
import com.bash.boundbackend.modules.book.entity.BookTransactionHistory;
import org.springframework.stereotype.Service;

@Service
public class BookMapper {

    public Book toBook(BookRequest bookRequest) {

        return Book.builder()
                .id(bookRequest.id())
                .title(bookRequest.title())
                .authorName(bookRequest.authorName())
                .isbn(bookRequest.isbn())
                .synopsis(bookRequest.synopsis())
                .archived(false)
                .shareable(bookRequest.shareable())
                .build();

    }

    public BookResponse toBookResponse( Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .authorName(book.getAuthorName())
                .isbn(book.getIsbn())
                .synopsis(book.getSynopsis())
                .averageBookRating(book.getBookRating())
                .archived(book.isArchived())
                .shareable(book.isShareable())
                .ownerName(book.getOwner().getFullName())
                .bookCover(FileReadUtils.readFileFromLocation(book.getBookCover()))  // read book cover image from location
                .build();
    }


    public BorrowedBookResponse toBorrowedBookResponse(BookTransactionHistory bookHistory) {
        return BorrowedBookResponse.builder()
                .id(bookHistory.getBook().getId())
                .title(bookHistory.getBook().getTitle())
                .authorName(bookHistory.getBook().getAuthorName())
                .isbn(bookHistory.getBook().getIsbn())
                .averageBookRating(bookHistory.getBook().getBookRating())
                .bookReturned(bookHistory.isBookReturned())
                .bookOwnerReturnApproved(bookHistory.isBookOwnerReturnApproved())
                .build();
    }
}
