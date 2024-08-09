--  SQLINES DEMO *** 0_09_00
--  SQLINES DEMO *** 0_09_00_initial_db_create.sql
--  SQLINES DEMO *** 0_09_00

CREATE TABLE BENUTZER (
    ID INT AUTO_INCREMENT PRIMARY KEY, -- Sequence id, auto-incremented
    EMAIL VARCHAR(255) NOT NULL UNIQUE, -- Email with unique constraint
    PASSWORD VARCHAR(255) NOT NULL -- Password field
);