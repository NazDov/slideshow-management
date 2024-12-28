package com.github.nazdov.slideshow.image.core.repository;

import com.github.nazdov.slideshow.image.core.entity.ImageAndSlideshow;
import com.github.nazdov.slideshow.image.core.entity.Slideshow;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface SlideshowRepo extends R2dbcRepository<Slideshow, Long> {

    @Query("""
                SELECT 
                    i.id AS image_id,
                    i.url AS image_url,
                    i.title AS image_title,
                    i.description AS image_description,
                    i.duration AS image_duration,
                    i.slideshow_id AS image_slideshow_id,
                    i.created_at AS image_created_at,
                    s.id AS slideshow_id,
                    s.name AS slideshow_name,
                    s.created_at AS slideshow_created_at
                FROM slideshow s
                LEFT JOIN image i
                ON i.slideshow_id = s.id
                WHERE i.slideshow_id = :id AND s.is_deleted=false
                ORDER BY i.created_at ASC
            """)
    Flux<ImageAndSlideshow> findOrderedAsc(@Param("id") Long id);

    @Query("""
                SELECT 
                    i.id AS image_id,
                    i.url AS image_url,
                    i.title AS image_title,
                    i.description AS image_description,
                    i.duration AS image_duration,
                    i.slideshow_id AS image_slideshow_id,
                    i.created_at AS image_created_at,
                    s.id AS slideshow_id,
                    s.name AS slideshow_name,
                    s.created_at AS slideshow_created_at
                FROM slideshow s
                LEFT JOIN image i
                ON i.slideshow_id = s.id
                WHERE i.slideshow_id = :id AND s.is_deleted=false
                ORDER BY i.created_at DESC
            """)
    Flux<ImageAndSlideshow> findOrderedDesc(@Param("id") Long id);
}
