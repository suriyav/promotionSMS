CREATE DATABASE IF NOT EXISTS `catatp` CHARACTER SET utf8 COLLATE utf8_unicode_ci;

USE `catatp`;

CREATE TABLE IF NOT EXISTS `Packages`
(
    `PACKAGE_ID`     int(11) PRIMARY KEY,
    `Description`    varchar(255),
    `DATA_PRO`       NUMERIC(8),
    `VOI_ONNET_PRO`  NUMERIC(8),
    `VOI_OFFNET_PRO` NUMERIC(8),
    `IDD_PRO`        NUMERIC(8),
    `SMS_Message`    varchar(255),
    `created_at`     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at`     TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX (PACKAGE_ID)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci;