-- V1__init
CREATE TABLE contacts (
                          id SERIAL NOT NULL PRIMARY KEY,
                          first_name VARCHAR(255) NOT NULL,
                          last_name VARCHAR(255) NOT NULL,
                          create_on timestamp with time zone NOT NULL DEFAULT now(),
                          email VARCHAR(255) NOT NULL,
                          phone VARCHAR(255) NOT NULL
);