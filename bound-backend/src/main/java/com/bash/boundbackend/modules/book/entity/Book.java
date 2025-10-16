package com.bash.boundbackend.modules.book.entity;

import com.bash.boundbackend.entity.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Book extends AuditableEntity{


    private String title;
    private String authorName;
    private String isbn;
    private String synopsis;
    private String bookCover;
    private boolean archived;
    private boolean shareable;

    // Many books can belong to one user
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    // A book can have a many feedbacks
    @OneToMany(mappedBy = "book")
    private List<Feedback> feedbacks;

    // One book many transactions
    @OneToMany
    private List<BookTransactionHistory> bookTransactionHistoryList;




}
