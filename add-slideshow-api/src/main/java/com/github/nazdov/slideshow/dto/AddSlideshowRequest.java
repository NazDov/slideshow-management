package com.github.nazdov.slideshow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request payload for adding a new slideshow.")
public class AddSlideshowRequest {

    @Schema(
            description = "The name of the slideshow.",
            example = "Summer Vacation Slideshow"
    )
    private String name;

    @Schema(description = "List of image IDs to be associated with the slideshow." +
            " At least one ID should be provided.")
    private List<Long> imageIds;

    @Schema(description = "List of image URLs to be associated with the slideshow." +
            " At least one URL should be provided.",
            example = "[\"http://example.com/image1.jpg\"," +
                    " \"http://example.com/image2.jpg\"]",
            required = true)
    private List<String> imageUrls;
}

