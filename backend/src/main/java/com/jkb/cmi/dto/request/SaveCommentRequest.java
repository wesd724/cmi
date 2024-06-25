package com.jkb.cmi.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SaveCommentRequest {
    private String username;
    private Long currencyId;
    private String content;
}
