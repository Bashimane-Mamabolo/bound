package com.bash.boundbackend.modules.book.repository;

import com.bash.boundbackend.modules.book.entity.BookTransactionHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface BookTransactionHistoryRepository extends JpaRepository<BookTransactionHistory, Integer> {

    @Query("""
            SELECT history
            FROM BookTransactionHistory history
            WHERE history.user.id = :userId
            """)
    Page<BookTransactionHistory> findAllBorrowedBooks(Pageable pageable, @Param("userId") Integer userId);

    @Query("""
            SELECT history
            FROM BookTransactionHistory history
            WHERE history.book.owner.id = :userId
            """)
    Page<BookTransactionHistory> findAllReturnedBooks(Pageable pageable, @Param("userId") Integer userId);



    // A user cannot borrow the same book twice until they return it.
    // Another user cannot borrow a book that’s currently borrowed by anyone.
    @Query("""
            SELECT (COUNT(*) > 0) AS isBorrowed
            FROM BookTransactionHistory bookTransactionHistory
            Where bookTransactionHistory.book.id = :bookId
            AND bookTransactionHistory.bookOwnerReturnApproved = false
            """)
    boolean isAlreadyBorrowed(@Param("bookId") Integer bookId);


    @Query("""
            SELECT bookTransactionHistory
            FROM BookTransactionHistory bookTransactionHistory
            Where bookTransactionHistory.book.id = :bookId
            AND bookTransactionHistory.user.id = :userId
            AND bookTransactionHistory.bookReturned = false
            AND bookTransactionHistory.bookOwnerReturnApproved = false
            """)
    Optional<BookTransactionHistory> findBookByUserIdAndBookId(@Param("bookId") Integer bookId, @Param("userId") Integer userId);

    @Query("""
            SELECT bookTransactionHistory
            FROM BookTransactionHistory bookTransactionHistory
            Where bookTransactionHistory.book.id = :bookId
            AND bookTransactionHistory.book.owner.id = :userId
            AND bookTransactionHistory.bookReturned = true
            AND bookTransactionHistory.bookOwnerReturnApproved = false
            """)
    Optional<BookTransactionHistory> findBookByUserIdAndOwnerId(@Param("bookId") Integer bookId, @Param("userId") Integer userId);
}
