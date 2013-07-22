-- MySQL dump 10.13  Distrib 5.5.30, for Linux (x86_64)
--
-- Host: localhost    Database: squash_league
-- ------------------------------------------------------
-- Server version	5.5.30

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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Club`
--

LOCK TABLES `Club` WRITE;
/*!40000 ALTER TABLE `Club` DISABLE KEYS */;
INSERT INTO `Club` VALUES (1,0,'Chalk Hill Road, Hammersmith, London, W6 8DW','Hammersmith GLL');
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
  `name` int(11) NOT NULL,
  `round_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK199794CD9C7BEB4F` (`round_id`),
  CONSTRAINT `FK199794CD9C7BEB4F` FOREIGN KEY (`round_id`) REFERENCES `Round` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Division`
--

LOCK TABLES `Division` WRITE;
/*!40000 ALTER TABLE `Division` DISABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `League`
--

LOCK TABLES `League` WRITE;
/*!40000 ALTER TABLE `League` DISABLE KEYS */;
INSERT INTO `League` VALUES (1,0,'Lunchtime',1),(2,0,'Evening',1);
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
  `division_id` bigint(20) NOT NULL,
  `playerOne_id` bigint(20) NOT NULL,
  `playerTwo_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK9557211327E9AE25` (`division_id`),
  KEY `FK95572113101128E1` (`playerOne_id`),
  KEY `FK95572113191CC33B` (`playerTwo_id`),
  CONSTRAINT `FK95572113191CC33B` FOREIGN KEY (`playerTwo_id`) REFERENCES `Player` (`id`),
  CONSTRAINT `FK95572113101128E1` FOREIGN KEY (`playerOne_id`) REFERENCES `Player` (`id`),
  CONSTRAINT `FK9557211327E9AE25` FOREIGN KEY (`division_id`) REFERENCES `Division` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Matches`
--

LOCK TABLES `Matches` WRITE;
/*!40000 ALTER TABLE `Matches` DISABLE KEYS */;
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
  UNIQUE KEY `UK_Player_1` (`user_id`,`league_id`),
  UNIQUE KEY `UK_Player_2` (`user_id`,`currentDivision_id`),
  KEY `FK8EA3870195ECFB0C` (`currentDivision_id`),
  KEY `FK8EA3870132CB8DA5` (`league_id`),
  KEY `FK8EA38701F1882E0F` (`user_id`),
  CONSTRAINT `FK8EA38701F1882E0F` FOREIGN KEY (`user_id`) REFERENCES `User` (`id`),
  CONSTRAINT `FK8EA3870132CB8DA5` FOREIGN KEY (`league_id`) REFERENCES `League` (`id`),
  CONSTRAINT `FK8EA3870195ECFB0C` FOREIGN KEY (`currentDivision_id`) REFERENCES `Division` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Player`
--

LOCK TABLES `Player` WRITE;
/*!40000 ALTER TABLE `Player` DISABLE KEYS */;
INSERT INTO `Player` VALUES (1,0,0,NULL,2,1),(2,0,0,NULL,1,2),(3,0,0,NULL,2,3),(4,0,0,NULL,2,4),(5,0,0,NULL,2,5),(6,0,0,NULL,1,8),(7,0,0,NULL,1,7),(8,0,0,NULL,2,7),(9,0,0,NULL,2,9),(10,0,0,NULL,1,9),(11,0,0,NULL,2,6),(12,0,0,NULL,1,10),(13,0,0,NULL,1,11),(14,0,0,NULL,2,12),(15,0,0,NULL,2,14),(16,0,0,NULL,2,15),(17,0,0,NULL,1,16),(18,0,0,NULL,1,17),(19,0,0,NULL,1,18);
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
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
  `league_id` bigint(20) NOT NULL,
  `previousRound_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK4B7C16E32CB8DA5` (`league_id`),
  KEY `FK4B7C16E543CBF46` (`previousRound_id`),
  CONSTRAINT `FK4B7C16E543CBF46` FOREIGN KEY (`previousRound_id`) REFERENCES `Round` (`id`),
  CONSTRAINT `FK4B7C16E32CB8DA5` FOREIGN KEY (`league_id`) REFERENCES `League` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Round`
--

LOCK TABLES `Round` WRITE;
/*!40000 ALTER TABLE `Round` DISABLE KEYS */;
INSERT INTO `Round` VALUES (1,0,'2013-08-30 04:00:00','2013-07-30 04:00:00',1,NULL),(2,0,'2013-08-30 04:00:00','2013-07-30 04:00:00',2,NULL);
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
  `loginFailures` int(11) NOT NULL,
  `mobile` varchar(255) DEFAULT NULL,
  `mobilePrivacy` int(11) NOT NULL,
  `name` varchar(50) NOT NULL,
  `oneTimeToken` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uc_User_1` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `User`
--

LOCK TABLES `User` WRITE;
/*!40000 ALTER TABLE `User` DISABLE KEYS */;
INSERT INTO `User` VALUES (1,8,'jamesdbloom@gmail.com',0,'07515 900 569',1,'James D Bloom','','3d8ec859343e4f8db209287657340a224cb49ac77118619eb5ee8d745efd725c65913207b4c4dadfdbdccd59e0576befd8dba46e3db9d5821c5967b5744562980746961bf3b7f109'),(2,3,'nmalde@bechtel.com',0,'07733260764',1,'Neil Malde','','4fd3398c8e14d6c735d962d30937816651f489b56bc76345fc2c0f7929333cfaf00e66775c1944fba4c5872aa239cf7b4ce7287f0a33e020e8034e0bd52a308c2d59aa416edf7dc1'),(3,1,'m.hilton@m3c.co.uk',0,'07826948397',1,'Matt Hilton','','1c19e496fab0cb35fbb58416f6585f3c0d7f014bf380c3d7bd3680475335562bee9aa6eeb167c8a9660e56e2851b295b6f0700dfd9ee0abb77fa7ac8574ba7d678cb9ee96d182f6e'),(4,1,'jk@nowmedical.co.uk',0,'07960585315',2,'John KEEN','','6def6cb0a6f2c4dbf96f2e30f913148f67ee88223cffcd064f936968427523778e45be9e6329dc1c5c5d8a840570861bf0267c1e3237cea4fdb4752ddc619e1e98f56e4ea1786beb'),(5,1,'samy.iralour@gmail.com',0,'07587141276',1,'samy','','bf09b6071ac2a36cbe4088f77f98c7b161d31e03b459c33935ee8f0c3aca956b68be70a2a7a94f7c4ff77d1aaff7400c39ac2bb8435d5cf2d7e975b0d51f1b171ed656148f9142bb'),(6,1,'swantonray@gmail.com',0,'07983530293',2,'Ray Swanton','','8532a7e68c1c1d9381b67900a60dd862148b19236f5b821c7834b2c3ebfff6493ec7cffaacc1fc75ee4f01dc288af1f20b4a975772216e0d3b738f1c825906e1552582788d700977'),(7,1,'swasey.rupert@gmail.com',0,'07906017618',2,'Rupert Swasey','','d049465ebd00c4588eebd3b671d2b60a2fb4e2a94ba633c68f9e194a3cef7e40578d0e63aca592033105f9a7f7c06a74aaeefd17c7b3e8e26ca55b8371c6193e93e868ee3f0eb8e2'),(8,1,'alistair.cossins@disney.com',0,'07787 260005',2,'Alistair Cossins','','f52572963c34518f7973b89beed6f195d39273e5def14303abcf35d4faceaef285fa2cef2c450c0b50f8c4bf81f6137453d63333798ef5d0d0acc0e86e191c6b46c26a7b42ea01b9'),(9,1,'rlpile@gmail.com',0,'07947 643747',2,'Richard Pile','','ca3d327cbc17323f2e2edc89920c47d17d70732c621c45948faa616aa7a4d55b8ab908e2ca37c1f1f08686fdf2ff979f21ee0ca51939525b4c411e1770d8e8c5f7ed9017b2697e0d'),(10,1,'jardine.finn@lbhf.gov.uk',0,'07903864846',2,'Jardine Finn','','6f82f4177347b9808c35716c2050be2a017d92f455bc63a2b08ef89e37b00c2c6f7fa750a987f3540e7f5ec468dc73161991e49399affab1089dbc070ec8b65286253918d43ee637'),(11,1,'scott.hands@vodafone.com',0,'07822859394',2,'Scott Hands','','4cd0697304c11fc2eac9fe30b33e37186a34332acd5dac94759a326fc3bcf2cf50e5b8a5f98b368e39a9d5b2ab3ba477640a9bbf061374856846156d8cdfd97c94528df582916f8c'),(12,1,'benoit.obadia@gmail.com',0,'07943934373',2,'Benoit','','665aa30fb696562d748e5e5fcd634cf1b10db6827c7277d334ba33224d7b612819e2a2acb49970a071fd2be4bf0402a9d89443653a2bda7564f2a648997b4ab25324b269856d08c1'),(13,1,'timvannispen@gmail.com',0,'07557949594',1,'Tim van Nispen','','5aa2274c5f6bf7291cd5e7f7beb4eb2e0432ded70004ced3a7702c22b81094740192faf02c53b67a383dbaa848df6162c7059265c335e59a73a279782eab43d9a3c550dce4ed2b3d'),(14,1,'benbhall1@gmail.com',0,'07768440727',2,'Ben Hall','','e33f487114256d4dfc9d0519d7a04d3266f53442a847e22e82dafe0399b05f61ab9d0664680de026b862ba313435a10fdfe2836335f32a9081f594d8b32b214b0b02ec6775d25bab'),(15,1,'roy.strachan@gmx.com',0,'07428068637',1,'Roy Strachan','','170f468ab6ec4cec8d1bbef91c914806449e73cfbe7498093ba383ec6ec499fc3ffbe15603392df9b2edc2cd4545ed19106286e3865908d01a31322136ec2693399a0bfc2ce4dc7d'),(16,1,'cin6304173@aol.com',0,'07787375914',2,'colin ingram','','86dfd4dc3b84355ff1cd35b9ba630d3868b489224a3675c993929aa8fd2a4225eabcf5d8a53c1370d04717915f56675b56dc9010f52bdb82764590787cb8a10580305fe0e7e15fd9'),(17,1,'j.townshend@mmu.ac.uk',0,'07950996387',1,' jules townshend','','ca4dcea39c657183c7273c654d27d23a74cd48007b50f39652d7ff692d9951c85c6f2cabc77533bf31aadb041f785f4372b328308eb84237b3e55e04623510694968478bf4b7ee7c'),(18,1,'philipmharrison2@gmail.com',0,'07730 822637',2,'Phil Harrison','','50c071bfccab9088633d5fafae3fc36a2f43cb20eac6dc982ece4287df65e18ddb2aa94d83cc56eb19f619db713f678e1328b1afbc1bce15946775547bc749881bd3899e6c91bd08');
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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `User_Role`
--

LOCK TABLES `User_Role` WRITE;
/*!40000 ALTER TABLE `User_Role` DISABLE KEYS */;
INSERT INTO `User_Role` VALUES (1,1),(2,2),(3,2),(4,2),(5,2),(6,2),(7,2),(8,2),(9,2),(10,2),(11,2),(12,2),(13,2),(14,2),(15,2),(16,2),(17,2),(18,2);
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

-- Dump completed on 2013-07-22 13:32:54
