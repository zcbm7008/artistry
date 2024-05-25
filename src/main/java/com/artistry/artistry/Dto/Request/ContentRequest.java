package com.artistry.artistry.Dto.Request;

import com.artistry.artistry.Domain.portfolio.Content;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ContentRequest {
    @NotEmpty(message = "포트폴리오 url이 첨부되지 않았습니다.")
    private String url;
    private String comment;

    public Content toEntity(){
        Content content = new Content(url,comment);
        return content;
    }
}
