package com.jkb.cmi.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @OneToOne
    @JoinColumn(name = "cash_asset_id")
    private CashAsset cashAsset;

    @Builder
    public User(String username, String password, CashAsset cashAsset) {
        this.username = username;
        this.password = password;
        this.cashAsset = cashAsset;
    }
}
