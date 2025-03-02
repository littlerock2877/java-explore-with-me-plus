CREATE TABLE IF NOT EXISTS users (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(250) NOT NULL,
    email VARCHAR(254) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS categories (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS locations
(
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    lat FLOAT NOT NULL,
    lon FLOAT NOT NULL
);

CREATE TABLE IF NOT EXISTS events
(
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    annotation VARCHAR(2000),
    category_id INTEGER REFERENCES categories(id) ON DELETE CASCADE,
    confirmed_requests BIGINT,
    description VARCHAR(7000),
    event_date TIMESTAMP WITHOUT TIME ZONE,
    created_on TIMESTAMP WITHOUT TIME ZONE,
    initiator_id INTEGER REFERENCES users(id) ON DELETE CASCADE,
    location_id INTEGER REFERENCES locations(id) ON DELETE CASCADE,
    paid BOOLEAN,
    participant_limit BIGINT,
    published_on TIMESTAMP WITHOUT TIME ZONE,
    request_moderation BOOLEAN,
    state VARCHAR(120),
    title VARCHAR(120),
    views BIGINT
);

CREATE TABLE IF NOT EXISTS compilations
(
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    pinned BOOLEAN DEFAULT false,
    title varchar(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS compilation_events
(
    compilation_id INTEGER REFERENCES compilations(id) ON DELETE CASCADE NOT NULL,
    event_id INTEGER REFERENCES events(id) ON DELETE CASCADE NOT NULL,
    PRIMARY KEY (compilation_id, event_id)
);

CREATE TABLE IF NOT EXISTS requests
(
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    created TIMESTAMP WITHOUT TIME ZONE,
    event INTEGER REFERENCES events(id) ON DELETE CASCADE NOT NULL,
    requester INTEGER REFERENCES users(id) ON DELETE CASCADE NOT NULL,
    status varchar(25) NOT NULL
);