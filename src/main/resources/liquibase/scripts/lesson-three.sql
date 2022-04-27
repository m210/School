-- liquibase formatted sql

--changeset m210:1
CREATE INDEX student_name_index ON student (name);

--changeset m210:2
CREATE INDEX faculty_nameAndColor_index ON faculty(name, color);