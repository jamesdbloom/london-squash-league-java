-- MySQL dump 10.13  Distrib 5.6.10, for osx10.8 (x86_64)
--
-- Host: localhost    Database: db143442_squash
-- ------------------------------------------------------
-- Server version	5.6.10

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Club`
--

DROP TABLE IF EXISTS `Club`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Club` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` int(11) DEFAULT NULL,
  `address` varchar(50) NOT NULL,
  `name` varchar(25) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Club`
--

LOCK TABLES `Club` WRITE;
/*!40000 ALTER TABLE `Club` DISABLE KEYS */;
INSERT INTO `Club` VALUES (1,0,'address 0','club name 0'),(2,0,'address 1','club name 1');
/*!40000 ALTER TABLE `Club` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Division`
--

DROP TABLE IF EXISTS `Division`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Division` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` int(11) DEFAULT NULL,
  `name` varchar(25) NOT NULL,
  `league_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK199794CD32CB8DA5` (`league_id`),
  CONSTRAINT `FK199794CD32CB8DA5` FOREIGN KEY (`league_id`) REFERENCES `League` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Division`
--

LOCK TABLES `Division` WRITE;
/*!40000 ALTER TABLE `Division` DISABLE KEYS */;
INSERT INTO `Division` VALUES (1,0,'Division name 0-0-0',1),(2,0,'Division name 1-0-0',1),(3,0,'Division name 0-1-0',2),(4,0,'Division name 1-1-0',2),(5,0,'Division name 0-0-1',3),(6,0,'Division name 1-0-1',3),(7,0,'Division name 0-1-1',4),(8,0,'Division name 1-1-1',4);
/*!40000 ALTER TABLE `Division` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `League`
--

DROP TABLE IF EXISTS `League`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `League` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` int(11) DEFAULT NULL,
  `name` varchar(25) NOT NULL,
  `club_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK876D3E4F5A00C305` (`club_id`),
  CONSTRAINT `FK876D3E4F5A00C305` FOREIGN KEY (`club_id`) REFERENCES `Club` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `League`
--

LOCK TABLES `League` WRITE;
/*!40000 ALTER TABLE `League` DISABLE KEYS */;
INSERT INTO `League` VALUES (1,0,'League name 0-0',1),(2,0,'League name 1-0',1),(3,0,'League name 0-1',2),(4,0,'League name 1-1',2);
/*!40000 ALTER TABLE `League` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Matches`
--

DROP TABLE IF EXISTS `Matches`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Matches` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` int(11) DEFAULT NULL,
  `score` varchar(255) DEFAULT NULL,
  `scoreEntered` datetime DEFAULT NULL,
  `playerOne_id` bigint(20) NOT NULL,
  `playerTwo_id` bigint(20) NOT NULL,
  `round_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK95572113101128E1` (`playerOne_id`),
  KEY `FK95572113191CC33B` (`playerTwo_id`),
  KEY `FK955721139C7BEB4F` (`round_id`),
  CONSTRAINT `FK955721139C7BEB4F` FOREIGN KEY (`round_id`) REFERENCES `Round` (`id`),
  CONSTRAINT `FK95572113101128E1` FOREIGN KEY (`playerOne_id`) REFERENCES `Player` (`id`),
  CONSTRAINT `FK95572113191CC33B` FOREIGN KEY (`playerTwo_id`) REFERENCES `Player` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=241 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Matches`
--

LOCK TABLES `Matches` WRITE;
/*!40000 ALTER TABLE `Matches` DISABLE KEYS */;
INSERT INTO `Matches` VALUES (1,0,NULL,NULL,1,2,1),(2,0,NULL,NULL,1,3,1),(3,0,NULL,NULL,1,4,1),(4,0,NULL,NULL,1,5,1),(5,0,NULL,NULL,1,6,1),(6,0,NULL,NULL,2,3,1),(7,0,NULL,NULL,2,4,1),(8,0,NULL,NULL,2,5,1),(9,0,NULL,NULL,2,6,1),(10,0,NULL,NULL,3,4,1),(11,0,NULL,NULL,3,5,1),(12,0,NULL,NULL,3,6,1),(13,0,NULL,NULL,4,5,1),(14,0,NULL,NULL,4,6,1),(15,0,NULL,NULL,5,6,1),(16,0,NULL,NULL,1,2,2),(17,0,NULL,NULL,1,3,2),(18,0,NULL,NULL,1,4,2),(19,0,NULL,NULL,1,5,2),(20,0,NULL,NULL,1,6,2),(21,0,NULL,NULL,2,3,2),(22,0,NULL,NULL,2,4,2),(23,0,NULL,NULL,2,5,2),(24,0,NULL,NULL,2,6,2),(25,0,NULL,NULL,3,4,2),(26,0,NULL,NULL,3,5,2),(27,0,NULL,NULL,3,6,2),(28,0,NULL,NULL,4,5,2),(29,0,NULL,NULL,4,6,2),(30,0,NULL,NULL,5,6,2),(31,0,NULL,NULL,7,8,3),(32,0,NULL,NULL,7,9,3),(33,0,NULL,NULL,7,10,3),(34,0,NULL,NULL,7,11,3),(35,0,NULL,NULL,7,12,3),(36,0,NULL,NULL,8,9,3),(37,0,NULL,NULL,8,10,3),(38,0,NULL,NULL,8,11,3),(39,0,NULL,NULL,8,12,3),(40,0,NULL,NULL,9,10,3),(41,0,NULL,NULL,9,11,3),(42,0,NULL,NULL,9,12,3),(43,0,NULL,NULL,10,11,3),(44,0,NULL,NULL,10,12,3),(45,0,NULL,NULL,11,12,3),(46,0,NULL,NULL,7,8,4),(47,0,NULL,NULL,7,9,4),(48,0,NULL,NULL,7,10,4),(49,0,NULL,NULL,7,11,4),(50,0,NULL,NULL,7,12,4),(51,0,NULL,NULL,8,9,4),(52,0,NULL,NULL,8,10,4),(53,0,NULL,NULL,8,11,4),(54,0,NULL,NULL,8,12,4),(55,0,NULL,NULL,9,10,4),(56,0,NULL,NULL,9,11,4),(57,0,NULL,NULL,9,12,4),(58,0,NULL,NULL,10,11,4),(59,0,NULL,NULL,10,12,4),(60,0,NULL,NULL,11,12,4),(61,0,NULL,NULL,13,14,5),(62,0,NULL,NULL,13,15,5),(63,0,NULL,NULL,13,16,5),(64,0,NULL,NULL,13,17,5),(65,0,NULL,NULL,13,18,5),(66,0,NULL,NULL,14,15,5),(67,0,NULL,NULL,14,16,5),(68,0,NULL,NULL,14,17,5),(69,0,NULL,NULL,14,18,5),(70,0,NULL,NULL,15,16,5),(71,0,NULL,NULL,15,17,5),(72,0,NULL,NULL,15,18,5),(73,0,NULL,NULL,16,17,5),(74,0,NULL,NULL,16,18,5),(75,0,NULL,NULL,17,18,5),(76,0,NULL,NULL,13,14,6),(77,0,NULL,NULL,13,15,6),(78,0,NULL,NULL,13,16,6),(79,0,NULL,NULL,13,17,6),(80,0,NULL,NULL,13,18,6),(81,0,NULL,NULL,14,15,6),(82,0,NULL,NULL,14,16,6),(83,0,NULL,NULL,14,17,6),(84,0,NULL,NULL,14,18,6),(85,0,NULL,NULL,15,16,6),(86,0,NULL,NULL,15,17,6),(87,0,NULL,NULL,15,18,6),(88,0,NULL,NULL,16,17,6),(89,0,NULL,NULL,16,18,6),(90,0,NULL,NULL,17,18,6),(91,0,NULL,NULL,19,20,7),(92,0,NULL,NULL,19,21,7),(93,0,NULL,NULL,19,22,7),(94,0,NULL,NULL,19,23,7),(95,0,NULL,NULL,19,24,7),(96,0,NULL,NULL,20,21,7),(97,0,NULL,NULL,20,22,7),(98,0,NULL,NULL,20,23,7),(99,0,NULL,NULL,20,24,7),(100,0,NULL,NULL,21,22,7),(101,0,NULL,NULL,21,23,7),(102,0,NULL,NULL,21,24,7),(103,0,NULL,NULL,22,23,7),(104,0,NULL,NULL,22,24,7),(105,0,NULL,NULL,23,24,7),(106,0,NULL,NULL,19,20,8),(107,0,NULL,NULL,19,21,8),(108,0,NULL,NULL,19,22,8),(109,0,NULL,NULL,19,23,8),(110,0,NULL,NULL,19,24,8),(111,0,NULL,NULL,20,21,8),(112,0,NULL,NULL,20,22,8),(113,0,NULL,NULL,20,23,8),(114,0,NULL,NULL,20,24,8),(115,0,NULL,NULL,21,22,8),(116,0,NULL,NULL,21,23,8),(117,0,NULL,NULL,21,24,8),(118,0,NULL,NULL,22,23,8),(119,0,NULL,NULL,22,24,8),(120,0,NULL,NULL,23,24,8),(121,0,NULL,NULL,25,26,9),(122,0,NULL,NULL,25,27,9),(123,0,NULL,NULL,25,28,9),(124,0,NULL,NULL,25,29,9),(125,0,NULL,NULL,25,30,9),(126,0,NULL,NULL,26,27,9),(127,0,NULL,NULL,26,28,9),(128,0,NULL,NULL,26,29,9),(129,0,NULL,NULL,26,30,9),(130,0,NULL,NULL,27,28,9),(131,0,NULL,NULL,27,29,9),(132,0,NULL,NULL,27,30,9),(133,0,NULL,NULL,28,29,9),(134,0,NULL,NULL,28,30,9),(135,0,NULL,NULL,29,30,9),(136,0,NULL,NULL,25,26,10),(137,0,NULL,NULL,25,27,10),(138,0,NULL,NULL,25,28,10),(139,0,NULL,NULL,25,29,10),(140,0,NULL,NULL,25,30,10),(141,0,NULL,NULL,26,27,10),(142,0,NULL,NULL,26,28,10),(143,0,NULL,NULL,26,29,10),(144,0,NULL,NULL,26,30,10),(145,0,NULL,NULL,27,28,10),(146,0,NULL,NULL,27,29,10),(147,0,NULL,NULL,27,30,10),(148,0,NULL,NULL,28,29,10),(149,0,NULL,NULL,28,30,10),(150,0,NULL,NULL,29,30,10),(151,0,NULL,NULL,31,32,11),(152,0,NULL,NULL,31,33,11),(153,0,NULL,NULL,31,34,11),(154,0,NULL,NULL,31,35,11),(155,0,NULL,NULL,31,36,11),(156,0,NULL,NULL,32,33,11),(157,0,NULL,NULL,32,34,11),(158,0,NULL,NULL,32,35,11),(159,0,NULL,NULL,32,36,11),(160,0,NULL,NULL,33,34,11),(161,0,NULL,NULL,33,35,11),(162,0,NULL,NULL,33,36,11),(163,0,NULL,NULL,34,35,11),(164,0,NULL,NULL,34,36,11),(165,0,NULL,NULL,35,36,11),(166,0,NULL,NULL,31,32,12),(167,0,NULL,NULL,31,33,12),(168,0,NULL,NULL,31,34,12),(169,0,NULL,NULL,31,35,12),(170,0,NULL,NULL,31,36,12),(171,0,NULL,NULL,32,33,12),(172,0,NULL,NULL,32,34,12),(173,0,NULL,NULL,32,35,12),(174,0,NULL,NULL,32,36,12),(175,0,NULL,NULL,33,34,12),(176,0,NULL,NULL,33,35,12),(177,0,NULL,NULL,33,36,12),(178,0,NULL,NULL,34,35,12),(179,0,NULL,NULL,34,36,12),(180,0,NULL,NULL,35,36,12),(181,0,NULL,NULL,37,38,13),(182,0,NULL,NULL,37,39,13),(183,0,NULL,NULL,37,40,13),(184,0,NULL,NULL,37,41,13),(185,0,NULL,NULL,37,42,13),(186,0,NULL,NULL,38,39,13),(187,0,NULL,NULL,38,40,13),(188,0,NULL,NULL,38,41,13),(189,0,NULL,NULL,38,42,13),(190,0,NULL,NULL,39,40,13),(191,0,NULL,NULL,39,41,13),(192,0,NULL,NULL,39,42,13),(193,0,NULL,NULL,40,41,13),(194,0,NULL,NULL,40,42,13),(195,0,NULL,NULL,41,42,13),(196,0,NULL,NULL,37,38,14),(197,0,NULL,NULL,37,39,14),(198,0,NULL,NULL,37,40,14),(199,0,NULL,NULL,37,41,14),(200,0,NULL,NULL,37,42,14),(201,0,NULL,NULL,38,39,14),(202,0,NULL,NULL,38,40,14),(203,0,NULL,NULL,38,41,14),(204,0,NULL,NULL,38,42,14),(205,0,NULL,NULL,39,40,14),(206,0,NULL,NULL,39,41,14),(207,0,NULL,NULL,39,42,14),(208,0,NULL,NULL,40,41,14),(209,0,NULL,NULL,40,42,14),(210,0,NULL,NULL,41,42,14),(211,0,NULL,NULL,43,44,15),(212,0,NULL,NULL,43,45,15),(213,0,NULL,NULL,43,46,15),(214,0,NULL,NULL,43,47,15),(215,0,NULL,NULL,43,48,15),(216,0,NULL,NULL,44,45,15),(217,0,NULL,NULL,44,46,15),(218,0,NULL,NULL,44,47,15),(219,0,NULL,NULL,44,48,15),(220,0,NULL,NULL,45,46,15),(221,0,NULL,NULL,45,47,15),(222,0,NULL,NULL,45,48,15),(223,0,NULL,NULL,46,47,15),(224,0,NULL,NULL,46,48,15),(225,0,NULL,NULL,47,48,15),(226,0,NULL,NULL,43,44,16),(227,0,NULL,NULL,43,45,16),(228,0,NULL,NULL,43,46,16),(229,0,NULL,NULL,43,47,16),(230,0,NULL,NULL,43,48,16),(231,0,NULL,NULL,44,45,16),(232,0,NULL,NULL,44,46,16),(233,0,NULL,NULL,44,47,16),(234,0,NULL,NULL,44,48,16),(235,0,NULL,NULL,45,46,16),(236,0,NULL,NULL,45,47,16),(237,0,NULL,NULL,45,48,16),(238,0,NULL,NULL,46,47,16),(239,0,NULL,NULL,46,48,16),(240,0,NULL,NULL,47,48,16);
/*!40000 ALTER TABLE `Matches` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Player`
--

DROP TABLE IF EXISTS `Player`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Player` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` int(11) DEFAULT NULL,
  `status` int(11) NOT NULL,
  `currentDivision_id` bigint(20) DEFAULT NULL,
  `league_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK8EA3870195ECFB0C` (`currentDivision_id`),
  KEY `FK8EA3870132CB8DA5` (`league_id`),
  KEY `FK8EA38701F1882E0F` (`user_id`),
  CONSTRAINT `FK8EA38701F1882E0F` FOREIGN KEY (`user_id`) REFERENCES `User` (`id`),
  CONSTRAINT `FK8EA3870132CB8DA5` FOREIGN KEY (`league_id`) REFERENCES `League` (`id`),
  CONSTRAINT `FK8EA3870195ECFB0C` FOREIGN KEY (`currentDivision_id`) REFERENCES `Division` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Player`
--

LOCK TABLES `Player` WRITE;
/*!40000 ALTER TABLE `Player` DISABLE KEYS */;
INSERT INTO `Player` VALUES (1,0,0,1,1,2),(2,0,0,1,1,3),(3,0,0,1,1,4),(4,0,0,1,1,5),(5,0,0,1,1,6),(6,0,0,1,1,1),(7,0,0,2,1,7),(8,0,0,2,1,8),(9,0,0,2,1,9),(10,0,0,2,1,10),(11,0,0,2,1,11),(12,0,0,2,1,1),(13,0,0,3,2,12),(14,0,0,3,2,13),(15,0,0,3,2,14),(16,0,0,3,2,15),(17,0,0,3,2,16),(18,0,0,3,2,1),(19,0,0,4,2,17),(20,0,0,4,2,18),(21,0,0,4,2,19),(22,0,0,4,2,20),(23,0,0,4,2,21),(24,0,0,4,2,1),(25,0,0,5,3,22),(26,0,0,5,3,23),(27,0,0,5,3,24),(28,0,0,5,3,25),(29,0,0,5,3,26),(30,0,0,5,3,1),(31,0,0,6,3,27),(32,0,0,6,3,28),(33,0,0,6,3,29),(34,0,0,6,3,30),(35,0,0,6,3,31),(36,0,0,6,3,1),(37,0,0,7,4,32),(38,0,0,7,4,33),(39,0,0,7,4,34),(40,0,0,7,4,35),(41,0,0,7,4,36),(42,0,0,7,4,1),(43,0,0,8,4,37),(44,0,0,8,4,38),(45,0,0,8,4,39),(46,0,0,8,4,40),(47,0,0,8,4,41),(48,0,0,8,4,1);
/*!40000 ALTER TABLE `Player` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Role`
--

DROP TABLE IF EXISTS `Role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` int(11) DEFAULT NULL,
  `description` varchar(50) NOT NULL,
  `name` varchar(25) NOT NULL,
  `club_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uc_Role_1` (`name`),
  KEY `FK26F4965A00C305` (`club_id`),
  CONSTRAINT `FK26F4965A00C305` FOREIGN KEY (`club_id`) REFERENCES `Club` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Role`
--

LOCK TABLES `Role` WRITE;
/*!40000 ALTER TABLE `Role` DISABLE KEYS */;
INSERT INTO `Role` VALUES (1,0,'Administrator Role','ROLE_ADMIN',NULL),(2,0,'Authentication User Role','ROLE_USER',NULL);
/*!40000 ALTER TABLE `Role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Round`
--

DROP TABLE IF EXISTS `Round`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Round` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` int(11) DEFAULT NULL,
  `endDate` datetime NOT NULL,
  `startDate` datetime NOT NULL,
  `division_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK4B7C16E27E9AE25` (`division_id`),
  CONSTRAINT `FK4B7C16E27E9AE25` FOREIGN KEY (`division_id`) REFERENCES `Division` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Round`
--

LOCK TABLES `Round` WRITE;
/*!40000 ALTER TABLE `Round` DISABLE KEYS */;
INSERT INTO `Round` VALUES (1,0,'2013-06-05 05:14:43','2013-06-01 05:14:43',1),(2,0,'2013-06-06 05:14:43','2013-06-02 05:14:43',1),(3,0,'2013-06-06 05:14:44','2013-06-02 05:14:44',2),(4,0,'2013-06-07 05:14:44','2013-06-03 05:14:44',2),(5,0,'2013-06-06 05:14:44','2013-06-02 05:14:44',3),(6,0,'2013-06-07 05:14:44','2013-06-03 05:14:44',3),(7,0,'2013-06-07 05:14:44','2013-06-03 05:14:44',4),(8,0,'2013-06-08 05:14:44','2013-06-04 05:14:44',4),(9,0,'2013-06-06 05:14:44','2013-06-02 05:14:44',5),(10,0,'2013-06-07 05:14:44','2013-06-03 05:14:44',5),(11,0,'2013-06-07 05:14:44','2013-06-03 05:14:44',6),(12,0,'2013-06-08 05:14:44','2013-06-04 05:14:44',6),(13,0,'2013-06-07 05:14:44','2013-06-03 05:14:44',7),(14,0,'2013-06-08 05:14:44','2013-06-04 05:14:44',7),(15,0,'2013-06-08 05:14:44','2013-06-04 05:14:44',8),(16,0,'2013-06-09 05:14:45','2013-06-05 05:14:45',8);
/*!40000 ALTER TABLE `Round` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `User`
--

DROP TABLE IF EXISTS `User`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `User` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` int(11) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `mobile` varchar(255) DEFAULT NULL,
  `mobilePrivacy` int(11) NOT NULL,
  `name` varchar(50) NOT NULL,
  `oneTimeToken` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uc_User_1` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `User`
--

LOCK TABLES `User` WRITE;
/*!40000 ALTER TABLE `User` DISABLE KEYS */;
INSERT INTO `User` VALUES (1,1,'jamesdbloom@gmail.com','07515 900 569',1,'James D Bloom','','da455eb9f960a178adf1d4da3d78b42da0046d3216e5170fa3ae7ddf168ac9f30af0cef66172015b0178c7079d97432fc1c544fc2a403f679069de65fcfa6c66812dcba0a40efedb'),(2,0,'0_0_0_0@email.com',NULL,0,'0_0_0_0 name',NULL,NULL),(3,0,'1_0_0_0@email.com',NULL,0,'1_0_0_0 name',NULL,NULL),(4,0,'2_0_0_0@email.com',NULL,0,'2_0_0_0 name',NULL,NULL),(5,0,'3_0_0_0@email.com',NULL,0,'3_0_0_0 name',NULL,NULL),(6,0,'4_0_0_0@email.com',NULL,0,'4_0_0_0 name',NULL,NULL),(7,0,'0_1_0_0@email.com',NULL,0,'0_1_0_0 name',NULL,NULL),(8,0,'1_1_0_0@email.com',NULL,0,'1_1_0_0 name',NULL,NULL),(9,0,'2_1_0_0@email.com',NULL,0,'2_1_0_0 name',NULL,NULL),(10,0,'3_1_0_0@email.com',NULL,0,'3_1_0_0 name',NULL,NULL),(11,0,'4_1_0_0@email.com',NULL,0,'4_1_0_0 name',NULL,NULL),(12,0,'0_0_1_0@email.com',NULL,0,'0_0_1_0 name',NULL,NULL),(13,0,'1_0_1_0@email.com',NULL,0,'1_0_1_0 name',NULL,NULL),(14,0,'2_0_1_0@email.com',NULL,0,'2_0_1_0 name',NULL,NULL),(15,0,'3_0_1_0@email.com',NULL,0,'3_0_1_0 name',NULL,NULL),(16,0,'4_0_1_0@email.com',NULL,0,'4_0_1_0 name',NULL,NULL),(17,0,'0_1_1_0@email.com',NULL,0,'0_1_1_0 name',NULL,NULL),(18,0,'1_1_1_0@email.com',NULL,0,'1_1_1_0 name',NULL,NULL),(19,0,'2_1_1_0@email.com',NULL,0,'2_1_1_0 name',NULL,NULL),(20,0,'3_1_1_0@email.com',NULL,0,'3_1_1_0 name',NULL,NULL),(21,0,'4_1_1_0@email.com',NULL,0,'4_1_1_0 name',NULL,NULL),(22,0,'0_0_0_1@email.com',NULL,0,'0_0_0_1 name',NULL,NULL),(23,0,'1_0_0_1@email.com',NULL,0,'1_0_0_1 name',NULL,NULL),(24,0,'2_0_0_1@email.com',NULL,0,'2_0_0_1 name',NULL,NULL),(25,0,'3_0_0_1@email.com',NULL,0,'3_0_0_1 name',NULL,NULL),(26,0,'4_0_0_1@email.com',NULL,0,'4_0_0_1 name',NULL,NULL),(27,0,'0_1_0_1@email.com',NULL,0,'0_1_0_1 name',NULL,NULL),(28,0,'1_1_0_1@email.com',NULL,0,'1_1_0_1 name',NULL,NULL),(29,0,'2_1_0_1@email.com',NULL,0,'2_1_0_1 name',NULL,NULL),(30,0,'3_1_0_1@email.com',NULL,0,'3_1_0_1 name',NULL,NULL),(31,0,'4_1_0_1@email.com',NULL,0,'4_1_0_1 name',NULL,NULL),(32,0,'0_0_1_1@email.com',NULL,0,'0_0_1_1 name',NULL,NULL),(33,0,'1_0_1_1@email.com',NULL,0,'1_0_1_1 name',NULL,NULL),(34,0,'2_0_1_1@email.com',NULL,0,'2_0_1_1 name',NULL,NULL),(35,0,'3_0_1_1@email.com',NULL,0,'3_0_1_1 name',NULL,NULL),(36,0,'4_0_1_1@email.com',NULL,0,'4_0_1_1 name',NULL,NULL),(37,0,'0_1_1_1@email.com',NULL,0,'0_1_1_1 name',NULL,NULL),(38,0,'1_1_1_1@email.com',NULL,0,'1_1_1_1 name',NULL,NULL),(39,0,'2_1_1_1@email.com',NULL,0,'2_1_1_1 name',NULL,NULL),(40,0,'3_1_1_1@email.com',NULL,0,'3_1_1_1 name',NULL,NULL),(41,0,'4_1_1_1@email.com',NULL,0,'4_1_1_1 name',NULL,NULL);
/*!40000 ALTER TABLE `User` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `User_Role`
--

DROP TABLE IF EXISTS `User_Role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `User_Role` (
  `User_id` bigint(20) NOT NULL,
  `roles_id` bigint(20) NOT NULL,
  KEY `FK8B9F886AEA75E288` (`roles_id`),
  KEY `FK8B9F886AF1882E0F` (`User_id`),
  CONSTRAINT `FK8B9F886AF1882E0F` FOREIGN KEY (`User_id`) REFERENCES `User` (`id`),
  CONSTRAINT `FK8B9F886AEA75E288` FOREIGN KEY (`roles_id`) REFERENCES `Role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `User_Role`
--

LOCK TABLES `User_Role` WRITE;
/*!40000 ALTER TABLE `User_Role` DISABLE KEYS */;
INSERT INTO `User_Role` VALUES (1,1),(2,2),(3,2),(4,2),(5,2),(6,2),(7,2),(8,2),(9,2),(10,2),(11,2),(12,2),(13,2),(14,2),(15,2),(16,2),(17,2),(18,2),(19,2),(20,2),(21,2),(22,2),(23,2),(24,2),(25,2),(26,2),(27,2),(28,2),(29,2),(30,2),(31,2),(32,2),(33,2),(34,2),(35,2),(36,2),(37,2),(38,2),(39,2),(40,2),(41,2);
/*!40000 ALTER TABLE `User_Role` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-05-31  6:19:24
