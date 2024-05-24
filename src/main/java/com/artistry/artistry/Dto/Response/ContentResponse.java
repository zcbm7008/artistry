package com.artistry.artistry.Dto.Response;

import com.artistry.artistry.Domain.portfolio.Content;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ContentResponse {
    private String url;
    private String comment;

    public static ContentResponse from(Content content){return new ContentResponse(content.getUrl(), content.getComment());}

}
