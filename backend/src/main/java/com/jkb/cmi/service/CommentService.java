package com.jkb.cmi.service;

import com.jkb.cmi.dto.request.SaveCommentRequest;
import com.jkb.cmi.dto.request.UpdateCommentRequest;
import com.jkb.cmi.dto.response.CommentResponse;
import com.jkb.cmi.entity.Comment;
import com.jkb.cmi.entity.Currency;
import com.jkb.cmi.entity.User;
import com.jkb.cmi.repository.CommentRepository;
import com.jkb.cmi.repository.CurrencyRepository;
import com.jkb.cmi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final CurrencyRepository currencyRepository;

    public Long saveComment(SaveCommentRequest saveCommentRequest) {
        User user = userRepository.getByUsername(saveCommentRequest.getUsername());
        Currency currency = currencyRepository.getReferenceById(saveCommentRequest.getCurrencyId());
        Comment comment = Comment.builder()
                .user(user)
                .currency(currency)
                .content(saveCommentRequest.getContent())
                .build();
        Comment saveComment = commentRepository.save(comment);
        return saveComment.getId();
    }

    public List<CommentResponse> getComment(String market) {
        List<Comment> comments = commentRepository.getByCurrency_Market(market);
        return CommentResponse.tolist(comments);
    }

    public void updateComment(UpdateCommentRequest updateCommentRequest) {
        Comment comment = commentRepository.getReferenceById(updateCommentRequest.getId());
        comment.changeComment(updateCommentRequest.getContent());
    }

    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }
}
