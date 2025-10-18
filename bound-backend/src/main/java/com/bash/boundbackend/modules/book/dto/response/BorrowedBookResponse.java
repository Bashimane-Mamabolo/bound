package com.bash.boundbackend.modules.book.dto.response;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BorrowedBookResponse {

    private Integer id;
    private String title;
    private String authorName;
    private String isbn;
    private double averageBookRating;
    private boolean bookReturned;
    private boolean bookOwnerReturnApproved;
}
