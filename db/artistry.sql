DROP Table IF EXISTS `member`;

CREATE TABLE `member` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `nickName` varchar(255) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

INSERT INTO `member` (`id`, `nickName`) VALUES (0,'composer1'),(1,'art1'),(2,'movieman1');

DROP Table IF EXISTS `tag`;

CREATE TABLE `tag` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `name` varchar(255) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

INSERT INTO `tag` (`id`,`name`) VALUES (0,'밴드'),(1,'재즈'),(2,'힙합'),(3,'트랩'),(4,'JPOP'),(5,'KPOP'),(6,'POP'),(7,'RNB'),(8,'하우스'),(9,'개러지'),(10,'퓨처베이스'),(11,'락');

DROP Table IF EXISTS `role`;

CREATE TABLE `role` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `roleName` varchar(255) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

INSERT INTO `role` (`id`,`roleName`) VALUES (0,'작곡가'),(1,'일러스트레이터'),(2,'편곡가'),(3,'영상편집'),(4,'조교'),(5,'드럼'),(6,'기타'),(7,'키보드'),(8,'베이스'),(9,'보컬'),(10,'작사가');

DROP TABLE IF EXISTS `team`;

CREATE TABLE `team`(
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(255) NOT NULL,
    `host_id` bigint(20) NOT NULL,
    `createdAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `fk_team_host` (`host_id`),
    CONSTRAINT `fk_team_host` FOREIGN KEY (`host_id`) REFERENCES `member` (`id`) ON DELETE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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