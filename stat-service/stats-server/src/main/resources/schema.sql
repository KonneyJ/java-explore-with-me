DROP TABLE IF EXISTS statistics;

CREATE TABLE IF NOT EXISTS statistics (
    id int generated by default as identity primary key,
    app varchar(255),
    uri varchar(255) not null,
    ip varchar(255) not null,
    created timestamp without time zone
);
