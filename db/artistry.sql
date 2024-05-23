USE artistrydb;
SET foreign_key_checks = 0;
-- Drop and recreate the member table
DROP TABLE IF EXISTS `member`;

CREATE TABLE `member` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `nickName` VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `member` (`nickName`) VALUES ('composer1'), ('art1'), ('movieman1'),('작곡가123');

-- Drop and recreate the tag table
DROP TABLE IF EXISTS `tag`;

CREATE TABLE `tag` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `tag` (`id`, `name`) VALUES (1,'밴드'), (2,'재즈'), (3,'힙합'), (4,'트랩'), (5,'JPOP'), (6,'KPOP'), (7,'POP'), (8,'RNB'), (9, '하우스'), (10,'개러지'), (11,'퓨처베이스'), (12,'락');

-- Drop and recreate the role table
DROP TABLE IF EXISTS `role`;

CREATE TABLE `role` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `roleName` VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `role` (`id`,`roleName`) VALUES (1,'작곡가'), (2,'일러스트레이터'), (3,'편곡가'), (4,'영상편집'), (5,'조교'), (6,'드럼'), (7,'기타'), (8,'키보드'), (9,'베이스'), (10,'보컬'), (11,'작사가');

-- Drop and recreate the team table
DROP TABLE IF EXISTS `team`;

CREATE TABLE `team` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(255) NOT NULL,
    `host_id` BIGINT(20) NOT NULL,
    `createdAt` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `fk_team_host` (`host_id`),
    CONSTRAINT `fk_team_host` FOREIGN KEY (`host_id`) REFERENCES `member` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `team` (`name`,`host_id`) VALUES ('작곡가 구합니다','2'),('프로젝트 팀원 구합니다','3');

-- Drop and recreate the team_role table
DROP TABLE IF EXISTS `team_role`;

CREATE TABLE `team_role` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `team_id` BIGINT(20) NOT NULL,
    `role_id` BIGINT(20) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `fk_team_role_team` (`team_id`),
    KEY `fk_team_role_role` (`role_id`),
    CONSTRAINT `fk_team_role_team` FOREIGN KEY (`team_id`) REFERENCES `team` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_team_role_role` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Drop and recreate the team_tag table
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

SET foreign_key_checks = 1;