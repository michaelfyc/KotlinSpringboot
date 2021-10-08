CREATE TABLE `reply_relation`(
    id int not null auto_increment,
    reply_id int not null,
    reply_to int not null,
    primary key(id),
    key reply_relation (reply_id, reply_to)
)