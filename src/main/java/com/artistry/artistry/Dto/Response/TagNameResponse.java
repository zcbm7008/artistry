package com.artistry.artistry.Dto.Response;

import com.artistry.artistry.Domain.tag.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TagNameResponse {
    private Long id;
    private String name;
    public static TagNameResponse from(final Tag tag){return new TagNameResponse(tag.getId(), tag.getName());}
}
