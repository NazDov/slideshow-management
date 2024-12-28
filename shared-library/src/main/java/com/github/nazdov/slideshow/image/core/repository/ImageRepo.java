package com.github.nazdov.slideshow.image.core.repository;

import com.github.nazdov.slideshow.image.core.entity.Image;
import com.github.nazdov.slideshow.image.core.entity.ImageAndSlideshow;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;

@Repository
public interface ImageRepo extends R2dbcRepository<Image, Long> {
    @Query("SELECT * FROM image WHERE url IN (:urls) AND image.is_deleted=false")
    Flux<Image> findAllByUrls(@Param("urls") Collection<String> urls);

    @Query("SELECT DISTINCT * FROM image WHERE id = :imageId AND slideshow_id = :slideshowId AND image.is_deleted=false")
    Mono<Image> findByIdAndSlideshowId(@Param("slideshowId") Long slideshowId, @Param("imageId") Long imageId);

    @Query("""
                SELECT 
                    i.id AS image_id,
                    i.url AS image_url,
                    i.title AS image_title,
                    i.description AS image_description,
                    i.duration AS image_duration,
                    i.slideshow_id AS image_slideshow_id,
                    s.id AS slideshow_id,
                    s.name AS slideshow_name,
                    s.created_at AS slideshow_created_at
                FROM image i
                LEFT JOIN slideshow s
                ON i.slideshow_id = s.id
                WHERE i.title LIKE CONCAT('%', :query, '%')
                   OR i.description LIKE CONCAT('%', :query, '%')
                   OR i.url LIKE CONCAT('%', :query, '%')
                   AND i.is_deleted=false
            """)
    Flux<ImageAndSlideshow> searchByQuery(@Param("query") String query);
}
