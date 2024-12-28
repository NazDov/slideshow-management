package com.github.nazdov.slideshow.image.dto;

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
@Schema(
        description = "Response for a search query, containing the search term (part of Image URL or name)" +
                " total results," +
                " and a list of matching images."
)
public class SearchResponse {

    @Schema(description = "The search query string (part of Image URL or name)", example = "name")
    private String query;

    @Schema(description = "The total number of results found.", example = "25")
    private int totalResults;

    @Schema(description = "List of images matching the search query.")
    private List<ImageResponse> images;
}
