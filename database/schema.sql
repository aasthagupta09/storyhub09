-- StoryHub schema (MySQL). Hibernate also auto-creates these tables (ddl-auto=update).
CREATE DATABASE IF NOT EXISTS storyhub;
USE storyhub;

CREATE TABLE users (
  id            BIGINT AUTO_INCREMENT PRIMARY KEY,
  username      VARCHAR(40)  NOT NULL UNIQUE,
  email         VARCHAR(100) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  bio           VARCHAR(500),
  created_at    DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE stories (
  id         BIGINT AUTO_INCREMENT PRIMARY KEY,
  title      VARCHAR(150) NOT NULL,
  category   VARCHAR(30)  NOT NULL,
  content    LONGTEXT     NOT NULL,
  author_id  BIGINT       NOT NULL,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (author_id) REFERENCES users(id)
);

CREATE TABLE comments (
  id         BIGINT AUTO_INCREMENT PRIMARY KEY,
  story_id   BIGINT NOT NULL,
  user_id    BIGINT NOT NULL,
  text       VARCHAR(1000) NOT NULL,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (story_id) REFERENCES stories(id) ON DELETE CASCADE,
  FOREIGN KEY (user_id)  REFERENCES users(id)
);

CREATE TABLE likes (
  id       BIGINT AUTO_INCREMENT PRIMARY KEY,
  story_id BIGINT NOT NULL,
  user_id  BIGINT NOT NULL,
  UNIQUE (story_id, user_id),               -- one like per user per story
  FOREIGN KEY (story_id) REFERENCES stories(id) ON DELETE CASCADE,
  FOREIGN KEY (user_id)  REFERENCES users(id)
);

CREATE TABLE favorites (
  id       BIGINT AUTO_INCREMENT PRIMARY KEY,
  story_id BIGINT NOT NULL,
  user_id  BIGINT NOT NULL,
  UNIQUE (story_id, user_id),
  FOREIGN KEY (story_id) REFERENCES stories(id) ON DELETE CASCADE,
  FOREIGN KEY (user_id)  REFERENCES users(id)
);

CREATE TABLE ratings (
  id       BIGINT AUTO_INCREMENT PRIMARY KEY,
  story_id BIGINT NOT NULL,
  user_id  BIGINT NOT NULL,
  stars    INT NOT NULL CHECK (stars BETWEEN 1 AND 5),
  UNIQUE (story_id, user_id),               -- one rating per user per story
  FOREIGN KEY (story_id) REFERENCES stories(id) ON DELETE CASCADE,
  FOREIGN KEY (user_id)  REFERENCES users(id)
);
