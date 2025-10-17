package com.bash.boundbackend.modules.book.repository;

import com.bash.boundbackend.modules.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Integer> {
}
