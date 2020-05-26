DROP TABLE IF EXISTS employees;

CREATE TABLE employees (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  first_name VARCHAR(250) NOT NULL,
  last_name VARCHAR(250) NOT NULL,
  email_address VARCHAR(250) DEFAULT NULL
);

INSERT INTO employees (first_name, last_name, email_address) VALUES
  ('Aliko', 'Dangote', 'ad@test.com'),
  ('Bill', 'Gates', 'bg@test.com');