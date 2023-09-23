DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS categories CASCADE;
DROP TABLE IF EXISTS locations CASCADE;
DROP TABLE IF EXISTS events CASCADE;
DROP TABLE IF EXISTS requests CASCADE;
DROP TABLE IF EXISTS compilations CASCADE;
DROP TABLE IF EXISTS compilation_events CASCADE;
DROP TABLE IF EXISTS event_likes CASCADE;

CREATE TABLE IF NOT EXISTS users (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(250) NOT NULL,
    email VARCHAR(254) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS categories (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS compilations (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    pinned BOOLEAN,
    title  VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS locations
(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    location_lat DOUBLE PRECISION,
    location_lon DOUBLE PRECISION
);

CREATE TABLE IF NOT EXISTS events (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    annotation TEXT NOT NULL,
    category_id BIGINT NOT NULL,
    created_on TIMESTAMP WITHOUT TIME ZONE,
    confirmed_requests BIGINT,
    description TEXT NOT NULL,
    location_id BIGINT NOT NULL,
    initiator_id BIGINT NOT NULL,
    event_date TIMESTAMP NOT NULL,
    paid BOOLEAN DEFAULT false,
    participant_limit INT DEFAULT 0,
    request_moderation BOOLEAN DEFAULT true,
    published_on TIMESTAMP WITHOUT TIME ZONE,
    title VARCHAR(120) NOT NULL,
    state VARCHAR(255) NOT NULL,
    views INTEGER,

    CONSTRAINT fk_events_to_users FOREIGN KEY(initiator_id) REFERENCES users(id),
    CONSTRAINT fk_events_to_locations FOREIGN KEY(location_id) REFERENCES locations(id),
    CONSTRAINT fk_events_to_categories FOREIGN KEY(category_id) REFERENCES categories(id)
);

CREATE TABLE IF NOT EXISTS requests
(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    event_id BIGINT,
    created_on TIMESTAMP WITHOUT TIME ZONE,
    requester_id BIGINT,
    status VARCHAR(255),

    CONSTRAINT fk_requests_to_events FOREIGN KEY(event_id) REFERENCES events(id),
    CONSTRAINT fk_requests_to_users FOREIGN KEY(requester_id) REFERENCES users(id),
    CONSTRAINT unique_event_requester_pair UNIQUE (event_id, requester_id)
);

CREATE TABLE IF NOT EXISTS compilation_events (
    compilation_id BIGINT,
    event_id BIGINT,

    CONSTRAINT pk_compilation_events UNIQUE (compilation_id, event_id),
    CONSTRAINT fk_compilation_events_to_compilations FOREIGN KEY (compilation_id) REFERENCES compilations(id),
    CONSTRAINT fk_compilation_events_to_events FOREIGN KEY (event_id) REFERENCES events(id)
);
