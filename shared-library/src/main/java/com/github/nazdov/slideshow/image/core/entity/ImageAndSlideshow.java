package com.github.nazdov.slideshow.image.core.entity;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageAndSlideshow {
    private Long imageId;
    private String imageUrl;
    private String imageTitle;
    private String imageDescription;
    private Integer imageDuration;
    private Long imageSlideshowId;
    private LocalDateTime imageCreatedAt;
    private Long slideshowId;
    private String slideshowName;
    private LocalDateTime slideshowCreatedAt;
}
