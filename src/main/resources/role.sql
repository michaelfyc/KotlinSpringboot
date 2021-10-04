CREATE TABLE `role`(
    `rid` int not null,
    `role_name` varchar(20) not null,
    primary key(rid)
);

INSERT INTO `role`(`rid`,`role_name`) VALUES (1,'root'),(2,'admin'),(3,'user'),(4,'guest');