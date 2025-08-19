-- Create schema
CREATE SCHEMA IF NOT EXISTS event_db AUTHORIZATION postgres;

-- Create roles table in event_db schema
CREATE TABLE IF NOT EXISTS event_db.roles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(20) UNIQUE NOT NULL
);

-- Create users table in event_db schema
CREATE TABLE IF NOT EXISTS event_db.users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

-- Create join table in event_db schema
CREATE TABLE IF NOT EXISTS event_db.user_roles (
    user_id BIGINT NOT NULL,
    role_id INTEGER NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES event_db.users (id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES event_db.roles (id) ON DELETE CASCADE
);

-- Insert initial roles if they don't exist
INSERT INTO event_db.roles(name) VALUES('ROLE_USER') ON CONFLICT (name) DO NOTHING;
INSERT INTO event_db.roles(name) VALUES('ROLE_ORGANIZER') ON CONFLICT (name) DO NOTHING; -- Added ORGANIZER role
INSERT INTO event_db.roles(name) VALUES('ROLE_ADMIN') ON CONFLICT (name) DO NOTHING;