package com.bash.boundbackend.modules.book.service;

import com.bash.boundbackend.common.utils.PageResponse;
import com.bash.boundbackend.modules.auth.entity.User;
import com.bash.boundbackend.modules.book.dto.BookMapper;
import com.bash.boundbackend.modules.book.dto.request.BookRequest;
import com.bash.boundbackend.modules.book.dto.response.BookResponse;
import com.bash.boundbackend.modules.book.entity.Book;
import com.bash.boundbackend.modules.book.repository.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookMapper bookMapper;
    private final BookRepository bookRepository;

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
}
