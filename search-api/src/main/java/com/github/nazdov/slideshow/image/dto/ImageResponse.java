package com.github.nazdov.slideshow.image.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageResponse {
    private Long id;
    private String url;
    private String title;
    private String description;
    private int duration;
    private SlideshowResponse slideshow;
}
