package com.artistry.artistry.Domain.member;

import com.artistry.artistry.Domain.common.AbstractLink;
import com.artistry.artistry.Domain.common.ContentsType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Embeddable
public class MemberLink extends AbstractLink {
    private static final int MIN_COMMENT_LENGTH = 1;
    private static final int MAX_COMMENT_LENGTH = 10;

    public MemberLink(String url, String comment){
        this(url, comment, ContentsType.WEB);
    }

    public MemberLink(String url, String comment, ContentsType type){
        super(url, comment, type, MIN_COMMENT_LENGTH, MAX_COMMENT_LENGTH);
    }

}
