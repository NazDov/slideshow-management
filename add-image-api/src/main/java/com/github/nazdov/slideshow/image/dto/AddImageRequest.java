package com.github.nazdov.slideshow.image.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddImageRequest {

    @Schema(description = "URL of the image", example = "https://example.com/image.jpg")
    private String url;

    @Schema(description = "Title of the image", example = "My Image")
    private String title;

    @Schema(description = "Description of the image", example = "A beautiful image of a sunset.")
    private String description;

    @Schema(description = "Duration the image will be played for, in seconds", example = "300")
    private Long duration;
}
