CREATE DATABASE IF NOT EXISTS posipanion;

CREATE USER IF NOT EXISTS 'posipanion'@'localhost' IDENTIFIED BY 'posipanion';
GRANT ALL PRIVILEGES ON posipanion.* TO 'posipanion'@'localhost';
FlUSH PRIVILEGES;