package com.ourMenu.backend.domain.menu.application;

import com.ourMenu.backend.domain.menu.domain.Tag;
import com.ourMenu.backend.domain.menu.dto.request.TagRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TagService {

    public Tag createTag(TagRequestDto tagInfo) {
        return Tag.builder()
                .name(tagInfo.getTagTitle())
                .isCustom(tagInfo.getIsCustom())
                .build();
    }
}