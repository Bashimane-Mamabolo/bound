package com.bash.boundbackend.modules.book.repository;

import com.bash.boundbackend.modules.book.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

    // Write our own query using @QUERY

    //    SELECT * FROM books
    //    WHERE archived = false AND shareable = true AND owner_id != 5
    //    ORDER BY created_at DESC
    //    LIMIT 10 OFFSET 0;

    @Query("""
            SELECT book
            FROM Book book
            WHERE book.archived = false
            AND book.shareable = true
            AND book.owner.id != :userId

            """)
    Page<Book> findAllDisplayableBooks(Pageable pageable, Integer userId);
}
