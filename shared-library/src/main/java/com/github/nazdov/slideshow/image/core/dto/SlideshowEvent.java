package com.github.nazdov.slideshow.image.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SlideshowEvent {
    private String topic;
    private Integer partition;
    private Long timestamp;
    private String key;
    private String value;
}
