CREATE TABLE `article`
(
    `article_id` int         not null auto_increment,
    `title`      varchar(50) not null default '',
    `content`    varchar(50) not null default '',
    `author_id`  int         not null,
    `create_at`  datetime(6) not null default '1000-01-01 00:00:00.00000',
    /* default article is public to all roles */
    `rid`        int         not null default 4,
    primary key (article_id)
);