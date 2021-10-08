create table `reply`(
  id int not null,
  uid int not null,
  article_id int not null,
  content varchar(1000) not null default '',
  reply_at datetime(6) not null default '1000-01-01 00:00:00.00000',
  rid int not null default 4,
  key `article_reply`(article_id,uid,content)
);