CREATE TABLE car (
    id INTEGER PRIMARY KEY,
    label VARCHAR(10) NOT NULL,
    model VARCHAR(10) NOT NULL,
    costs REAL CHECK (costs > 0)
);

CREATE TABLE dude (
     id INTEGER PRIMARY KEY,
     name TEXT NOT NULL,
     license BOOLEAN,
     car_id INTEGER REFERENCES car(id)
);