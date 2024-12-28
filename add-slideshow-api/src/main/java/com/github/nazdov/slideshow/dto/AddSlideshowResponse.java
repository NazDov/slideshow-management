package com.github.nazdov.slideshow.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@Schema(description = "Response payload for adding a new slideshow.")
public class AddSlideshowResponse {

    @Schema(description = "The ID of the newly created slideshow.",
            example = "123")
    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "The timestamp when the slideshow was created.",
            example = "2024-12-28 14:30:00")
    private LocalDateTime createdAt;
}
