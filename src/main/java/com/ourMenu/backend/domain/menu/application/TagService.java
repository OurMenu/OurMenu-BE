package com.ourMenu.backend.domain.menu.application;

import com.ourMenu.backend.domain.menu.dao.TagRepository;
import com.ourMenu.backend.domain.menu.domain.Tag;
import com.ourMenu.backend.domain.menu.dto.request.TagRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    public Tag createTag(TagRequestDto tagInfo) {
        return Tag.builder()
                .name(tagInfo.getTagTitle())
                .isCustom(tagInfo.getIsCustom())
                .build();
    }

    @Transactional
    public Optional<Tag> findByName(String tagName){
        return tagRepository.findByName(tagName);
    }
}