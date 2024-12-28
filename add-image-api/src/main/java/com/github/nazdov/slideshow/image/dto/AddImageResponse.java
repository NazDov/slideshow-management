package com.github.nazdov.slideshow.image.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Builder
public class AddImageResponse {

    @Schema(description = "Unique ID of the image", example = "123")
    private Long id;

    @Schema(description = "Timestamp when the image was created", example = "2024-01-01T00:00:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
}

