package com.bash.boundbackend.modules.book.controller;

import com.bash.boundbackend.common.utils.PageResponse;
import com.bash.boundbackend.modules.book.dto.request.BookRequest;
import com.bash.boundbackend.modules.book.dto.response.BookResponse;
import com.bash.boundbackend.modules.book.entity.Book;
import com.bash.boundbackend.modules.book.service.BookService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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



}
