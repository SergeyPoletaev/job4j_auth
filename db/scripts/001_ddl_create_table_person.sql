create table if not exists person
(
    id       serial primary key not null,
    login    varchar unique     not null,
    password varchar            not null
);