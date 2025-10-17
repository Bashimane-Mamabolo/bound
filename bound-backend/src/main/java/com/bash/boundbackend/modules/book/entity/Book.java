package com.bash.boundbackend.modules.book.entity;

import com.bash.boundbackend.modules.auth.entity.User;
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
    @OneToMany(mappedBy = "book")
    private List<BookTransactionHistory> bookTransactionHistoryList;

    // Calculate average book rating field
    @Transient
    public double getBookRating(){
        if (feedbacks == null){
            return  0.0;
        }
        var averageBookRating = this.feedbacks.stream()
                .mapToDouble(Feedback::getRating)
                .average()
                .orElse(0.0);
        // round it to the nearest whole number e.g 4.5 -> 5.0
        return Math.round(averageBookRating*100.0)/100.0;
    }




}
