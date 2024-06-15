package com.artistry.artistry.Dto.Response;

import com.artistry.artistry.Domain.common.AbstractLink;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LinkResponse {
    private String url;
    private String comment;

    public static LinkResponse from(AbstractLink abstractLink){return new LinkResponse(abstractLink.getUrl(), abstractLink.getComment());}

}
