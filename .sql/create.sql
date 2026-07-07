CREATE TABLE authors (

    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    name VARCHAR(100) NOT NULL,

    surname VARCHAR(100) NOT NULL,

    email VARCHAR(150) NOT NULL UNIQUE

);

CREATE TABLE posts (

    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    title VARCHAR(255) NOT NULL,

    body TEXT,

    author_id BIGINT NOT NULL,

    CONSTRAINT fk_post_author
        FOREIGN KEY(author_id)
        REFERENCES authors(id)
        ON DELETE CASCADE
);

CREATE TABLE comments (

    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    username VARCHAR(100) NOT NULL,

    body TEXT,

    post_id BIGINT NOT NULL,

    CONSTRAINT fk_comment_post
        FOREIGN KEY(post_id)
        REFERENCES posts(id)
        ON DELETE CASCADE
);