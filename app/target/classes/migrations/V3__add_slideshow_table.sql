-- Slideshow Table
CREATE TABLE IF NOT EXISTS slideshow (
                                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                         name VARCHAR(50) DEFAULT NULL,
                                         is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
                                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
