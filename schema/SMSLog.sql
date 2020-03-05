CREATE DATABASE IF NOT EXISTS `catatp` CHARACTER SET utf8 COLLATE utf8_unicode_ci;

USE `catatp`;

CREATE TABLE IF NOT EXISTS `SMSLog`
(
    `ID`           int PRIMARY KEY AUTO_INCREMENT,
    `MDN`          int(11),
    `SMS_Message`  VARCHAR(255),
    `Sender`       CHAR(20),
    `SendDateTime` DATETIME,
    `Status`       CHAR(20),
    `created_at`   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX (MDN)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci;