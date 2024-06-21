USE artistrydb;
SET foreign_key_checks = 0;

DROP TABLE IF EXISTS `tag`;

CREATE TABLE `tag` (
    `id` BIGINT(255) NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`)
);

INSERT INTO `tag` (`id`, `name`) VALUES
(1,'밴드'), (2,'재즈'), (3,'힙합'), (4,'트랩'), (5,'JPOP'), (6,'KPOP'),
(7,'POP'), (8,'RNB'), (9,'하우스'), (10,'개러지'), (11,'퓨처베이스'),
(12,'락'), (13, '프로지향'), (14,'아마추어'), (15,'공모전'), (16,'보카로');

DROP TABLE IF EXISTS `team_tag`;

CREATE TABLE `team_tag` (
    `team_id` BIGINT(20) NOT NULL,
    `tag_id` BIGINT(20) NOT NULL,
    PRIMARY KEY (`team_id`, `tag_id`),
    KEY `fk_team_tag_team` (`team_id`),
    KEY `fk_team_tag_tag` (`tag_id`),
    CONSTRAINT `fk_team_tag_team` FOREIGN KEY (`team_id`) REFERENCES `team` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_team_tag_tag` FOREIGN KEY (`tag_id`) REFERENCES `tag` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `team_tag` (`team_id`, `tag_id`) VALUES
(1, 3), (1, 4),
(2, 5), (2, 6),
(3, 3), (3, 7),
(4, 4), (4, 7),
(5, 7), (5, 11);

DROP TABLE IF EXISTS `role`;

CREATE TABLE `role` (
    `id` BIGINT(255) NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`)
);

INSERT INTO `role` (`id`,`name`) VALUES
(1,'작곡가'), (2,'일러스트레이터'), (3,'편곡가'), (4,'영상편집'), (5,'조교'),
(6,'드럼'), (7,'기타'), (8,'키보드'), (9,'베이스'), (10,'보컬'), (11,'작사가'),
(12,'마스터링'), (13,'프로듀서');

DROP TABLE IF EXISTS `application`;

CREATE TABLE `application` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `portfolio_id` BIGINT(255) NOT NULL,
    `team_role_id` BIGINT(255) NOT NULL,
    `status` VARCHAR(255) NOT NULL CHECK (status IN ('PENDING', 'APPROVED', 'REJECTED')),
    `type` VARCHAR(255) NOT NULL CHECK (type IN ('APPLICATION', 'INVITATION')),
    PRIMARY KEY (`id`),
    KEY `fk_application_portfolio` (`portfolio_id`),
    KEY `fk_application_team_role` (`team_role_id`),
    CONSTRAINT `fk_application_portfolio` FOREIGN KEY (`portfolio_id`) REFERENCES `portfolio` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_application_team_role` FOREIGN KEY (`team_role_id`) REFERENCES `teamRole` (`id`) ON DELETE CASCADE
);

INSERT INTO `application` (`portfolio_id`, `team_role_id`, `status`, `type`) VALUES
(1, 1, 'PENDING', 'APPLICATION'),
(2, 2, 'APPROVED', 'INVITATION'),
(3, 3, 'REJECTED', 'APPLICATION'),
(4, 4, 'PENDING', 'INVITATION'),
(5, 5, 'APPROVED', 'APPLICATION');

DROP TABLE IF EXISTS `portfolio`;

CREATE TABLE `portfolio` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `member_id` BIGINT(255) NOT NULL,
    `role_id` BIGINT(255) NOT NULL,
    `title` VARCHAR(255) NOT NULL,
    `view_count` BIGINT(255) NOT NULL,
    `like_count` BIGINT(255) NOT NULL,
    `access` VARCHAR(255) CHECK (access IN ('PRIVATE', 'PUBLIC')),
    PRIMARY KEY (`id`),
    KEY `fk_portfolio_member` (`member_id`),
    KEY `fk_portfolio_role` (`role_id`),
    CONSTRAINT `fk_portfolio_member` FOREIGN KEY (`member_id`) REFERENCES `member` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_portfolio_role` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `portfolio` (`member_id`, `role_id`, `title`, `view_count`, `like_count`, `access`) VALUES
(1, 1, 'Trap Beat 1', 100, 10, 'PUBLIC'),
(2, 2, 'Cool Illustration', 200, 30, 'PUBLIC'),
(3, 3, 'Jazz Arrangement', 150, 20, 'PRIVATE'),
(4, 4, 'Video Editing', 250, 40, 'PUBLIC'),
(5, 5, 'Assistant Work', 50, 5, 'PRIVATE');

DROP TABLE IF EXISTS `portfolio_contents`;

CREATE TABLE `portfolio_contents` (
    `portfolio_id` BIGINT(20) NOT NULL,
    `url` VARCHAR(255),
    `comment` VARCHAR(255),
    `type` VARCHAR(255) CHECK (type IN ('WEB', 'IMAGE', 'AUDIO', 'VIDEO', 'TEXT', 'UNKNOWN')),
    KEY `fk_portfolio_contents_portfolio` (`portfolio_id`),
    CONSTRAINT `fk_portfolio_contents_portfolio` FOREIGN KEY (`portfolio_id`) REFERENCES `portfolio` (`id`) ON DELETE CASCADE
);

INSERT INTO `portfolio_contents` (`portfolio_id`, `url`, `comment`, `type`) VALUES
(1, 'http://example.com/audio1.mp3', 'Trap beat example', 'AUDIO'),
(2, 'http://example.com/image1.jpg', 'Illustration example', 'IMAGE'),
(3, 'http://example.com/text1.txt', 'Jazz arrangement notes', 'TEXT'),
(4, 'http://example.com/video1.mp4', 'Edited video example', 'VIDEO'),
(5, 'http://example.com/web1.html', 'Assistant project details', 'WEB');

DROP TABLE IF EXISTS `member`;

CREATE TABLE `member` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `nickName` VARCHAR(20) NOT NULL,
    `email` VARCHAR(255) NOT NULL,
    `url` VARCHAR(255),
    `bio` VARCHAR(1000),
    `deleted` TINYINT(1) NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `member` (`nickName`,`email`,`bio`) VALUES
('zerisound','zerisound22@gmail.com','making trap music'),
('premalone','notpost@gmail.com','wassup'),
('illuster','illuster@gmail.com',"hi! i'm making cool illustrations"),
('kawaiineverdie4','kawaii@gmail.com',"hi! i like jpop"),
('movieisreal','movieisreal@gmail.com',"tool : AfterEffect"),
('더샤이팬','theshyfan@gmail.com',"더샤이팬임 ㅇㅇ"),
('musicianX','musicx@gmail.com',"X-style music"),
('artistY','arty@gmail.com',"Creating modern art"),
('drummerZ','drumz@gmail.com',"Drumming for life"),
('vocalistA','vocala@gmail.com',"Singing is my passion");

DROP TABLE IF EXISTS `member_links`;

CREATE TABLE `member_links` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `member_id` BIGINT(20) NOT NULL,
    `comment` VARCHAR(255) NOT NULL,
    `url` VARCHAR(255) NOT NULL,
    `type` VARCHAR(255) CHECK (type IN ('WEB', 'IMAGE', 'AUDIO', 'VIDEO', 'TEXT', 'UNKNOWN')),
    PRIMARY KEY (`id`),
    KEY `fk_member_links_member` (`member_id`),
    CONSTRAINT `fk_member_links_member` FOREIGN KEY (`member_id`) REFERENCES `member` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `member_links` (`member_id`,`comment`,`url`,`type`) VALUES
(1, 'instragram', 'https://www.instagram.com/zerisound/','WEB'),
(1, 'youtube', 'https://www.youtube.com/@zerisound','WEB'),
(2, 'personal blog', 'https://premalone-blog.com','WEB'),
(3, 'portfolio', 'https://illuster-portfolio.com','WEB'),
(4, 'jpop fanpage', 'https://kawaii-jpop.com','WEB');

DROP TABLE IF EXISTS `team`;

CREATE TABLE `team` (
    `id` BIGINT(255) NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(255) NOT NULL,
    `host_id` BIGINT(255) NOT NULL,
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `team_status` VARCHAR(255) CHECK (team_status IN ('RECRUITING', 'CANCELED', 'FINISHED')),
    `deleted` TINYINT(1) NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`),
    KEY `fk_team_host` (`host_id`),
    CONSTRAINT `fk_team_host` FOREIGN KEY (`host_id`) REFERENCES `member` (`id`) ON DELETE CASCADE
);

INSERT INTO `team` (`name`,`host_id`,`team_status`) VALUES
('i need a songwriter!',2,'RECRUITING'),
('find a team to making jpop!',3,'RECRUITING'),
('제 비트에 랩해주실 래퍼분 찾습니다',5,'RECRUITING'),
('anyone wants to make trap music?',1,'CANCELED'),
("let's make postmalone style together",2,'FINISHED'),
('Rock Band Formation',6,'RECRUITING'),
('Jazz Ensemble',7,'RECRUITING'),
('Future Bass Project',8,'RECRUITING'),
('RNB Collaboration',9,'RECRUITING'),
('Pop Music Group',10,'RECRUITING');

DROP TABLE IF EXISTS `teamRole`;

CREATE TABLE `teamRole` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `role_id` BIGINT(20) NOT NULL,
    `team_id` BIGINT(20) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `fk_teamRole_team` (`team_id`),
    KEY `fk_teamRole_role` (`role_id`),
    CONSTRAINT `fk_teamRole_team` FOREIGN KEY (`team_id`) REFERENCES `team` (`id`),
    CONSTRAINT `fk_teamRole_role` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `teamRole` (`role_id`, `team_id`) VALUES
(1, 1), (2, 1), (3, 1),
(4, 2), (5, 2), (6, 2),
(7, 3), (8, 3), (9, 3),
(10, 4), (11, 4), (12, 4),
(13, 5),
(6, 6), (7, 6), (9, 6), (10, 6),
(2, 7), (3, 7), (5, 7),
(1, 8), (4, 8), (11, 8),
(8, 9), (10, 9),
(7, 10), (8, 10), (12, 10);

SET foreign_key_checks = 1;