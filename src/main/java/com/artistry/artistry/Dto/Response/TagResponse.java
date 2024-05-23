package com.artistry.artistry.Dto.Response;

import com.artistry.artistry.Domain.tag.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TagResponse {
    private Long id;
    private String name;
    public static TagResponse from(Tag tag){
        return new TagResponse(
                tag.getId(),
                tag.getName()
        );
    }
}
