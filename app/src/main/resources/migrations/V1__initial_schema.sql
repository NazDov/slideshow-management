-- Image Table
CREATE TABLE IF NOT EXISTS image (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       url VARCHAR(200) NOT NULL UNIQUE,
                       title VARCHAR(50) DEFAULT NULL,
                       description VARCHAR(100) DEFAULT NULL,
                       duration BIGINT NOT NULL,
                       is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
