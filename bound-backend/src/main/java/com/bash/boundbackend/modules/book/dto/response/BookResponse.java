package com.bash.boundbackend.modules.book.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookResponse {
    private Integer id;
    private String title;
    private String authorName;
    private String isbn;
    private String synopsis;
    private String ownerName;
    private byte[] bookCover;
    private double averageBookRating;
    private boolean archived;
    private boolean shareable;

}
