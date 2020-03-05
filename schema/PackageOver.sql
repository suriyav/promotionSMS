CREATE TABLE IF NOT EXISTS `PackageOver`
(
    `ID`                int auto_increment primary key,
    `MONTHS`            varchar(6)                              null,
    `MDN`               int(11)                                 null,
    `PACKAGE_ID`        char(8)                                 null,
    `VOI_ONNET_PRO`     decimal(8)                              null,
    `VOI_OFFNET_PRO`    decimal(8)                              null,
    `IDD_PRO`           decimal(8)                              null,
    `DATA_PRO`          decimal(8)                              null,
    `PACKAGE_RECOMMEND` char(8)                                 null,
    `FLG_Send_SMS`      tinyint                                 null,
    `created_at`        timestamp default current_timestamp()   null,
    `updated_at`        timestamp default '0000-00-00 00:00:00' null on update current_timestamp(),
    INDEX (`MONTHS`, `MDN`, `PACKAGE_ID`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci;