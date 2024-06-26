package com.jkb.cmi.dto.response;

import com.jkb.cmi.entity.Comment;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SaveCommentResponse {
    private Long id;
    private LocalDateTime createdDate;

    public static SaveCommentResponse from(Comment comment) {
        return new SaveCommentResponse(comment.getId(), comment.getCreatedDate());
    }
}
