package com.github.nazdov.slideshow.image.core.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table(name = "slideshow")
@EqualsAndHashCode
@ToString
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Slideshow {
    @Id
    private Long id;
    private String name;
    @Column("created_at")
    private LocalDateTime createdAt;
    @Column("is_deleted")
    private boolean isDeleted;

    public Slideshow.SlideshowBuilder copyFrom(Slideshow image) {
        return Slideshow.builder()
                .id(image.getId())
                .name(image.getName())
                .createdAt(image.getCreatedAt())
                .isDeleted(image.isDeleted());
    }
}
