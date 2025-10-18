package com.bash.boundbackend.modules.book.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackResponse {

    private Double rating;
    private String comment;
    private boolean ownFeedback;  // highlight own feedback with a diff color

}
