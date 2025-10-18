package com.bash.boundbackend.modules.book.controller;

import com.bash.boundbackend.common.utils.PageResponse;
import com.bash.boundbackend.modules.book.dto.request.BookRequest;
import com.bash.boundbackend.modules.book.dto.response.BookResponse;
import com.bash.boundbackend.modules.book.dto.response.BorrowedBookResponse;
import com.bash.boundbackend.modules.book.service.BookService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("books")
@RequiredArgsConstructor
@Tag(name = "Book")
public class BookController {

    private  final BookService bookService;

    @PostMapping("/save-book")
    public ResponseEntity<Integer> createBook(
            @RequestBody @Valid BookRequest bookRequest,
            Authentication connectedUser) {
        return ResponseEntity.ok(bookService.saveBook(bookRequest, connectedUser));
    }

    @GetMapping("/get-book/{book-id}")
    public ResponseEntity<BookResponse> getBookById(
            @PathVariable("book-id") Integer bookId
    ){
        return ResponseEntity.ok(bookService.findBookById(bookId));
    }

    // Pagination
    // Get the books for other users and not the connected user.
    // Shareable and not archived
    @GetMapping("/all-books")
    public  ResponseEntity<PageResponse<BookResponse>> findAllDisplayableBooks(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(bookService.findAllDisplayableBooks(page, size, connectedUser));
    }

    // Implement specification (JpaSpecificationExecutor Interface). Avoid hardcoded SQL
    @GetMapping("/by-owner")
    public ResponseEntity<PageResponse<BookResponse>> findAllBooksByOwner(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(bookService.findAllBooksByOwner(page, size, connectedUser));
    }

    // Find all borrowed books by the user
    @GetMapping("/borrowed")
    public ResponseEntity<PageResponse<BorrowedBookResponse>> findAllBorrowedBooks(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(bookService.findAllBorrowedBooks(page, size, connectedUser));
    }

    // Find all returned for the owner
    @GetMapping("/returned")
    public ResponseEntity<PageResponse<BorrowedBookResponse>> findAllReturnedBooks(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(bookService.findAllReturnedBooks(page, size, connectedUser));
    }

    // Update book shareable status
    @PatchMapping("/shareable/{book-id}")
    public ResponseEntity<Integer> bookShareableStatus(
            @PathVariable("book-id") Integer bookId,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(bookService.updateBookShareableStatus(bookId, connectedUser));
    }

    @PatchMapping("/archived/{book-id}")
    public ResponseEntity<Integer> updateBookArchiveStatus(
            @PathVariable("book-id") Integer bookId,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(bookService.updateBookArchiveStatus(bookId, connectedUser));
    }

    // Goal: A user wants to borrow a book from another user
    // If successful, create a BookTransactionHistory record to primary checks
    @PostMapping("/borrow/{book-id}")
    public ResponseEntity<Integer> borrowBook(
            @PathVariable("book-id") Integer bookId,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(bookService.borrowBook(bookId, connectedUser));
    }

    // Goal: As a user (borrower) I want to return a book I borrowed
    @PatchMapping("/return-book/{book-id}")
    public ResponseEntity<Integer> returnBorrowedBook(
            @PathVariable("book-id") Integer bookId,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(bookService.returnBorrowedBook(bookId, connectedUser));
    }

    // Goal: As an owner of the returned book(by borrower), I want to approve it
    @PatchMapping("/return-approve/{book-id}")
    public ResponseEntity<Integer> approvedReturnedBook(
            @PathVariable("book-id") Integer bookId,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(bookService.approveReturnedBook(bookId, connectedUser));
    }

    @PostMapping(value = "/book-cover/{book-id}", consumes = "multipart/form-data")
    public ResponseEntity<?> uploadBookCover(
            @RequestPart("file") MultipartFile file,
            @PathVariable("book-id") Integer bookId,
            Authentication connectedUser
//            @Parameter()
    ) {
        bookService.uploadBookCoverPicture(file, connectedUser, bookId);
        return ResponseEntity.accepted().build();
    }








}
