package com.github.nazdov.slideshow.image.core.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table(name = "image")
@EqualsAndHashCode
@ToString
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Image {
    @Id
    private Long id;
    private String url;
    private String title;
    private String description;
    private Long duration;
    @Column("created_at")
    private LocalDateTime createdAt;
    @Column("is_deleted")
    private boolean isDeleted;
    @Column("slideshow_id")
    private Long slideshowId;

    public Image.ImageBuilder copyFrom(Image image) {
        return Image.builder()
                .id(image.getId())
                .url(image.getUrl())
                .title(image.getTitle())
                .description(image.getDescription())
                .createdAt(image.getCreatedAt())
                .duration(image.getDuration())
                .slideshowId(image.getSlideshowId())
                .isDeleted(image.isDeleted());
    }
}
