create table if not exists person_role
(
    id        serial primary key         not null,
    person_id int references person (id) not null,
    role_id   int references role (id)   not null
);