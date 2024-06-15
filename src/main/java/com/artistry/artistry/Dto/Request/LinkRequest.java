package com.artistry.artistry.Dto.Request;

import com.artistry.artistry.Domain.member.MemberLink;
import com.artistry.artistry.Domain.portfolio.Content;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LinkRequest {
    @NotEmpty(message = "url이 첨부되지 않았습니다.")
    private String url;
    private String comment;

    public Content toContent(){
        return new Content(url,comment);
    }

    public MemberLink toMemberLink(){
        return new MemberLink(url,comment);
    }
}
