package com.jkb.cmi.comment.dto.response;

import com.jkb.cmi.comment.entity.Comment;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentResponse {
    private Long id;
    private String username;
    private String content;
    private LocalDateTime createdDate;

    public static List<CommentResponse> tolist(List<Comment> comments) {
        return comments.stream()
                .map(c ->
                        new CommentResponse(c.getId(), c.getUser().getUsername(), c.getContent(), c.getCreatedDate()))
                .toList();
    }
}
