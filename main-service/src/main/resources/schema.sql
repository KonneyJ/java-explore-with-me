DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS category CASCADE;
DROP TABLE IF EXISTS location CASCADE;
DROP TABLE IF EXISTS events CASCADE;
DROP TABLE IF EXISTS requests CASCADE;
DROP TABLE IF EXISTS compilations CASCADE;
DROP TABLE IF EXISTS compilation_events CASCADE;
DROP TABLE IF EXISTS likes CASCADE;

CREATE TABLE IF NOT EXISTS users (
    user_id int generated by default as identity primary key,
    email varchar(255) unique not null,
    user_name varchar(255) not null
);

CREATE TABLE IF NOT EXISTS category (
    category_id int generated by default as identity primary key,
    category_name varchar(50) unique not null
);

CREATE TABLE IF NOT EXISTS location (
    location_id int generated by default as identity primary key,
    lat float not null,
    lon float not null
);

CREATE TABLE IF NOT EXISTS events (
    event_id int generated by default as identity primary key,
    annotation varchar(2000) not null,
    category_id int not null REFERENCES category (category_id) on delete cascade,
    confirmed_requests int,
    created_date timestamp without time zone,
    description varchar(7000),
    event_date timestamp without time zone not null,
    user_id int not null REFERENCES users (user_id) on delete cascade,
    location_id int not null REFERENCES location (location_id) on delete cascade,
    paid boolean default false not null,
    participant_limit int default 0,
    published_date timestamp without time zone,
    request_moderation boolean default true not null,
    state varchar(20) not null,
    title varchar(120) not null
);

CREATE TABLE IF NOT EXISTS requests(
    request_id int generated by default as identity primary key,
    created timestamp without time zone,
    event_id int not null REFERENCES events (event_id) on delete cascade,
    requester_id int not null REFERENCES users (user_id) on delete cascade,
    status varchar(20)
);

CREATE TABLE IF NOT EXISTS compilations(
    compilation_id int generated by default as identity primary key,
    pinned boolean not null default false,
    title varchar(50) not null
);

CREATE TABLE IF NOT EXISTS compilation_events(
    compilation_id int not null REFERENCES compilations (compilation_id) ON update CASCADE,
    event_id int not null REFERENCES events (event_id) ON update CASCADE,
    primary key(compilation_id, event_id)
);

CREATE TABLE IF NOT EXISTS likes(
    like_id int generated by default as identity primary key,
    liked boolean not null,
    user_id int not null REFERENCES users (user_id) on delete cascade,
    event_id int not null REFERENCES events (event_id) on delete cascade,
    request_id int not null REFERENCES requests (request_id) on delete cascade,
    created_date timestamp without time zone
);