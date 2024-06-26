package com.artistry.artistry.Service;

import com.artistry.artistry.Domain.tag.Tag;
import com.artistry.artistry.Dto.Request.TagCreateRequest;
import com.artistry.artistry.Dto.Request.TagRequest;
import com.artistry.artistry.Dto.Request.TagUpdateRequest;
import com.artistry.artistry.Dto.Response.TagNameResponse;
import com.artistry.artistry.Dto.Response.TagResponse;
import com.artistry.artistry.Exceptions.TagNotFoundException;
import com.artistry.artistry.Repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagService {

    @Autowired
    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository){
        this.tagRepository = tagRepository;
    }

    public Tag findEntityById(Long id){
        return tagRepository.findById(id)
                .orElseThrow(() -> new TagNotFoundException(String.format("[%d]는 존재하지 않는 태그 ID입니다.", id)));
    }

    public TagResponse findById(Long id){
        return TagResponse.from(findEntityById(id));
    }

    public List<TagResponse> findAll() {
        List <Tag> tags = tagRepository.findAll();

        return tags.stream()
                .map(TagResponse::from)
                .collect(Collectors.toList());
    }

    public List<TagResponse> findAllById(final List<TagRequest> tagRequests){
        return tagRequests.stream()
                .map(tagRequest -> findById(tagRequest.getId()))
                .collect(Collectors.toList());
    }

    public List<Tag> findAllEntityById(final List<TagRequest> tagRequests){
        return tagRequests.stream()
                .map(tagRequest -> findEntityById(tagRequest.getId()))
                .collect(Collectors.toList());
    }

    public List <TagNameResponse> findTagNames(final String name){
        List <Tag> tags = tagRepository.findAllByName(name);

        return tags.stream().map(TagNameResponse::from).collect(Collectors.toList());
    }

    public TagResponse createTag(final TagCreateRequest request){
        Tag tag = tagRepository.save(request.toEntity());

        return TagResponse.from(tag);
    }

    @Transactional
    public TagResponse updateTag(final Long tagId, final TagUpdateRequest request){
        Tag tag = findEntityById(tagId);
        tag.update(request.getName());

        return TagResponse.from(findEntityById(tagId));
    }

    @Transactional
    public void delete(final Long tagId){
        Tag tag = findEntityById(tagId);
        tagRepository.delete(tag);
    }
}
