CREATE TABLE `user`
(
    `uid`       int         not null auto_increment,
    `email`     varchar(50) not null,
    `username`  varchar(30) not null,
    `password`  varchar(32) not null,
    `is_locked` tinyint(1)  not null default 0,
    primary key (uid)
);
