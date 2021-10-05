CREATE TABLE `user`
(
    `uid`       int         not null auto_increment,
    `email`     varchar(50) not null,
    `username`  varchar(30) not null,
    `password`  varchar(32) not null,
    `is_locked` tinyint(1)  not null default 0,
    `lock_to` datetime not null default '1000-01-01 00:00:00',
    primary key (uid)
);
