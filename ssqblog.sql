/*
SQLyog Ultimate v10.00 Beta1
MySQL - 8.0.27 : Database - ssqblog
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`ssqblog` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `ssqblog`;

/*Table structure for table `ssq_blog` */

DROP TABLE IF EXISTS `ssq_blog`;

CREATE TABLE `ssq_blog` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `description` varchar(255) NOT NULL,
  `status` tinyint(1) NOT NULL,
  `created` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP,
  `tags` varchar(255) DEFAULT NULL,
  `filename` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `ssq_blog` */

insert  into `ssq_blog`(`id`,`title`,`description`,`status`,`created`,`tags`,`filename`) values (23,'Spring Security整合Jwt','该篇文章是用于描述如何通过Spring Security 整合JWT实现授权与认证功能的。',1,'2022-02-02 12:36:00',NULL,'Spirng Security整合Jwt.md'),(24,'后端实现Markdown文件转html文件','该篇文章是关于如何在springboot框架下，将Markdown文件转换成Html文件的，以及如何提取markdown中的front-matter。',1,'2022-02-02 12:36:06',NULL,'后端实现markdown文件转html文件.md');

/*Table structure for table `ssq_tag` */

DROP TABLE IF EXISTS `ssq_tag`;

CREATE TABLE `ssq_tag` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(30) NOT NULL,
  `number` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `ssq_tag` */

insert  into `ssq_tag`(`id`,`name`,`number`) values (23,'Spring',2),(24,'SpirngSecurity',1);

/*Table structure for table `ssq_tag_blog` */

DROP TABLE IF EXISTS `ssq_tag_blog`;

CREATE TABLE `ssq_tag_blog` (
  `id` int NOT NULL AUTO_INCREMENT,
  `tag_id` int NOT NULL,
  `blog_id` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `ssq_tag_blog` */

insert  into `ssq_tag_blog`(`id`,`tag_id`,`blog_id`) values (29,23,23),(30,24,23),(31,23,24);

/*Table structure for table `ssq_user` */

DROP TABLE IF EXISTS `ssq_user`;

CREATE TABLE `ssq_user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(64) DEFAULT NULL,
  `avater` varchar(64) DEFAULT NULL,
  `email` varchar(64) DEFAULT NULL,
  `password` varchar(64) DEFAULT NULL,
  `status` tinyint NOT NULL,
  `created` datetime DEFAULT NULL,
  `last_login` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `ssq_user` */

insert  into `ssq_user`(`id`,`username`,`avater`,`email`,`password`,`status`,`created`,`last_login`) values (1,'SSQ',NULL,NULL,'123456',1,NULL,NULL);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
