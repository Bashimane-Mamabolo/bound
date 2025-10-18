package com.bash.boundbackend.modules.book.service;

import com.bash.boundbackend.common.exception.OperationNotPermittedException;
import com.bash.boundbackend.common.utils.PageResponse;
import com.bash.boundbackend.modules.auth.entity.User;
import com.bash.boundbackend.modules.book.dto.BookMapper;
import com.bash.boundbackend.modules.book.dto.request.BookRequest;
import com.bash.boundbackend.modules.book.dto.response.BookResponse;
import com.bash.boundbackend.modules.book.dto.response.BorrowedBookResponse;
import com.bash.boundbackend.modules.book.entity.Book;
import com.bash.boundbackend.modules.book.entity.BookTransactionHistory;
import com.bash.boundbackend.modules.book.repository.BookRepository;
import com.bash.boundbackend.modules.book.repository.BookTransactionHistoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Objects;

import static com.bash.boundbackend.modules.book.service.BookSpecification.withOwnerId;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookMapper bookMapper;
    private final BookRepository bookRepository;
    private final BookTransactionHistoryRepository transactionRepository;

    public Integer saveBook(BookRequest bookRequest, Authentication connectedUser) {

        User user = (User) connectedUser.getPrincipal();
        Book book = bookMapper.toBook(bookRequest);
        book.setOwner(user);
        return bookRepository.save(book).getId();
    }

    public BookResponse findBookById(Integer bookId) {

        return bookRepository.findById(bookId)
                .map(bookMapper::toBookResponse)
                .orElseThrow(() -> new EntityNotFoundException("No book with Id: "+ bookId));
    }


    public PageResponse<BookResponse> findAllDisplayableBooks(int page, int size, Authentication connectedUser) {

        User user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("CreatedAt").descending());
        Page<Book> books = bookRepository.findAllDisplayableBooks(pageable, user.getId());
        List<BookResponse> bookResponses = books.stream()
                .map(bookMapper::toBookResponse)
                .toList();
        return new PageResponse<>(
                bookResponses,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast()
        );
    }

    public PageResponse<BookResponse> findAllBooksByOwner(int page, int size, Authentication connectedUser) {

        User user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("CreatedAt").descending());
        Page<Book> books = bookRepository.findAll(withOwnerId(user.getId()), pageable);
        List<BookResponse> bookResponses = books.stream()
                .map(bookMapper::toBookResponse)
                .toList();
        return new PageResponse<>(
                bookResponses,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast()
        );
    }

    public PageResponse<BorrowedBookResponse> findAllBorrowedBooks(int page, int size, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("CreatedAt").descending());
        Page<BookTransactionHistory> borrowedBooks = transactionRepository.findAllBorrowedBooks(pageable, user.getId());
        List<BorrowedBookResponse> bookResponses = borrowedBooks.stream()
                .map(bookMapper::toBorrowedBookResponse)
                .toList();
        return new PageResponse<>(
                bookResponses,
                borrowedBooks.getNumber(),
                borrowedBooks.getSize(),
                borrowedBooks.getTotalElements(),
                borrowedBooks.getTotalPages(),
                borrowedBooks.isFirst(),
                borrowedBooks.isLast()
        );
    }

    public PageResponse<BorrowedBookResponse> findAllReturnedBooks(int page, int size, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("CreatedAt").descending());
        Page<BookTransactionHistory> borrowedBooks = transactionRepository.findAllReturnedBooks(pageable, user.getId());
        List<BorrowedBookResponse> bookResponses = borrowedBooks.stream()
                .map(bookMapper::toBorrowedBookResponse)
                .toList();
        return new PageResponse<>(
                bookResponses,
                borrowedBooks.getNumber(),
                borrowedBooks.getSize(),
                borrowedBooks.getTotalElements(),
                borrowedBooks.getTotalPages(),
                borrowedBooks.isFirst(),
                borrowedBooks.isLast()
        );
    }

    // User with required id [OWNER] can update the book status
    public Integer updateBookShareableStatus(Integer bookId, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Book book = bookRepository.findById(bookId)
                .orElseThrow(()-> new EntityNotFoundException("No book found with Id: " + bookId));
        if (!Objects.equals(user.getId(), book.getOwner().getId())) {
            // throw custom exception and handle this exception
            throw new OperationNotPermittedException("Not authorized to update book's shareable status");
        }
        book.setShareable(!book.isShareable()); //inverse the value, no hardcoding
        bookRepository.save(book);

        return bookId;
    }

    // Owner archive or unarchive their book
    public Integer updateBookArchiveStatus(Integer bookId, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Book book = bookRepository.findById(bookId)
                .orElseThrow(()-> new EntityNotFoundException("No book found with Id: " + bookId));
        if (!Objects.equals(user.getId(), book.getOwner().getId())) {
            // throw custom exception and handle this exception
            throw new OperationNotPermittedException("Not authorized to update book's archived status");
        }
        book.setArchived(!book.isArchived()); //inverse the value, no hardcoding
        bookRepository.save(book);

        return bookId;
    }

    public Integer borrowBook(Integer bookId, Authentication connectedUser) {

        Book book = bookRepository.findById(bookId)
                .orElseThrow(()-> new EntityNotFoundException("No book found with Id: " + bookId));
        if (book.isArchived() || !book.isShareable()) {
            throw new OperationNotPermittedException("Book is archived or not shareable");
        }
        User user = (User) connectedUser.getPrincipal();
        if (Objects.equals(user.getId(), book.getOwner().getId())) {
            throw new OperationNotPermittedException("You cannot borrow your own book");
        }

        final boolean isAlreadyBorrowed = transactionRepository.isAlreadyBorrowedByAnotherUser(bookId, user.getId());

        if (isAlreadyBorrowed) {
            throw new OperationNotPermittedException("Book is already borrowed");
        }
        BookTransactionHistory transactionHistory = BookTransactionHistory.builder()
                .user(user)
                .book(book)
                .bookOwnerReturnApproved(false)
                .bookReturned(false)
                .build();
        return transactionRepository.save(transactionHistory).getId();
    }
}
