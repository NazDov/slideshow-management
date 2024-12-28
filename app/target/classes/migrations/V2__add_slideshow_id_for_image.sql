ALTER TABLE image
    ADD COLUMN slideshow_id BIGINT DEFAULT NULL;

-- rollaback
-- ALTER TABLE image DROP COLUMN slideshow_id
