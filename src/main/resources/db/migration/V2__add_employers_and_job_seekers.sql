-- V2__add_employers_and_job_seekers.sql

CREATE TABLE employers (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           company_name VARCHAR(255) NOT NULL,
                           company_description TEXT,
                           website VARCHAR(255),
                           user_id BIGINT UNIQUE,
                           FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE job_seekers (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             resume_url VARCHAR(255),
                             skills TEXT,
                             experience_level VARCHAR(100),
                             user_id BIGINT UNIQUE,
                             FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
