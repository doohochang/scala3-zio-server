CREATE TABLE IF NOT EXISTS articles (
    id serial PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    content text NOT NULL,
    author_name VARCHAR(255) NOT NULL,
    view_count int DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
