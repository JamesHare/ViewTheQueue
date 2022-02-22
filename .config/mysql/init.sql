DROP USER if exists 'viewthequeue';
CREATE USER 'viewthequeue'@'%' IDENTIFIED BY 'viewthequeue1';
GRANT ALL PRIVILEGES ON *.* TO 'viewthequeue'@'%' WITH GRANT OPTION;

DROP DATABASE IF EXISTS viewthequeue;
CREATE SCHEMA viewthequeue;
ALTER DEFAULT PRIVILEGES IN SCHEMA viewthequeue GRANT ALL PRIVILEGES ON TABLES TO viewthequeue;
GRANT ALL ON SCHEMA viewthequeue TO viewthequeue;
ALTER ROLE viewthequeue SET search_path TO viewthequeue;
grant viewthequeue to admin;
ALTER DEFAULT PRIVILEGES FOR ROLE viewthequeue GRANT INSERT, UPDATE, DELETE, TRUNCATE ON TABLES TO admin;