CREATE TABLE IF NOT EXISTS articles (
    id serial PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    content text NOT NULL,
    author_name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
