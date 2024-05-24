package com.artistry.artistry.Dto.Request;

import com.artistry.artistry.Domain.tag.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TagCreateRequest {

    @NonNull
    private String name;

    public Tag toEntity() {
        return new Tag(name);
    }
}
