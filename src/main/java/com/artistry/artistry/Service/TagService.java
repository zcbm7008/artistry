package com.artistry.artistry.Service;

import com.artistry.artistry.Domain.Tag;
import com.artistry.artistry.Exceptions.TagNotFoundException;
import com.artistry.artistry.Repository.TagRepository;
import org.springframework.stereotype.Service;

@Service
public class TagService {

    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository){
        this.tagRepository = tagRepository;
    }

    public Tag findById(Long id){
        return tagRepository.findById(id)
                .orElseThrow(TagNotFoundException::new);
    }
}
