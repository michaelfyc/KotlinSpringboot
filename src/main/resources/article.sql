CREATE TABLE `article`
(
    `article_id` int         not null auto_increment,
    `title`      varchar(50) not null default '',
    `content`    varchar(50) not null default '',
    `author_id`  int         not null,
    `is_visible` tinyint(1)  not null default 1,
    primary key (article_id)
);