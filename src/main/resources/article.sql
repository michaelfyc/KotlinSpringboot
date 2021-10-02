CREATE TABLE `article`
(
    `article_id` int         not null auto_increment,
    `title`      varchar(50) not null default '',
    `content`    varchar(50) not null default '',
    `author_id`  int         not null,
    /* default article is public to all roles */
    `rid` int not null default 4,
    primary key (article_id)
);