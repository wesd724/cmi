package com.jkb.cmi.comment.entity;

import com.jkb.cmi.common.entity.BaseEntity;
import com.jkb.cmi.currency.entity.Currency;
import com.jkb.cmi.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "comment")
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_id")
    private Currency currency;

    @Column(nullable = false)
    private String content;

    @Builder
    public Comment(User user, Currency currency, String content) {
        this.user = user;
        this.currency = currency;
        this.content = content;
    }

    public void changeComment(String content) {
        this.content = content;
    }
}
