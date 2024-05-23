package com.artistry.artistry.Service;

import com.artistry.artistry.Domain.Tag;
import com.artistry.artistry.Dto.Request.TagRequest;
import com.artistry.artistry.Dto.Response.TagResponse;
import com.artistry.artistry.Exceptions.TagNotFoundException;
import com.artistry.artistry.Repository.TagRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    public List<TagResponse> findAll() {
        List <Tag> tags = tagRepository.findAll();
        return tags.stream()
                .map(TagResponse::from)
                .collect(Collectors.toList());
    }

    public List<Tag> findAllById(final List<TagRequest> tagRequests){
        return tagRequests.stream()
                .map(tagRequest -> findById(tagRequest.getId()))
                .collect(Collectors.toList());
    }
}
