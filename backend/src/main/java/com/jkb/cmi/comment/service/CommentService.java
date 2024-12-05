package com.jkb.cmi.comment.service;

import com.jkb.cmi.comment.dto.request.SaveCommentRequest;
import com.jkb.cmi.comment.dto.request.UpdateCommentRequest;
import com.jkb.cmi.comment.dto.response.CommentResponse;
import com.jkb.cmi.comment.dto.response.SaveCommentResponse;
import com.jkb.cmi.comment.entity.Comment;
import com.jkb.cmi.currency.entity.Currency;
import com.jkb.cmi.user.entity.User;
import com.jkb.cmi.comment.repository.CommentRepository;
import com.jkb.cmi.currency.repository.CurrencyRepository;
import com.jkb.cmi.user.repository.UserRepository;
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

    public SaveCommentResponse saveComment(SaveCommentRequest saveCommentRequest) {
        User user = userRepository.getByUsername(saveCommentRequest.getUsername());
        Currency currency = currencyRepository.getReferenceById(saveCommentRequest.getCurrencyId());
        Comment comment = Comment.builder()
                .user(user)
                .currency(currency)
                .content(saveCommentRequest.getContent())
                .build();
        Comment saveComment = commentRepository.save(comment);
        return SaveCommentResponse.from(saveComment);
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
