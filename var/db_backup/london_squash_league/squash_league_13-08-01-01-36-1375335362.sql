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
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Division`
--

LOCK TABLES `Division` WRITE;
/*!40000 ALTER TABLE `Division` DISABLE KEYS */;
INSERT INTO `Division` VALUES (1,0,1,2),(2,0,2,2),(3,0,1,1),(4,0,2,1);
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
) ENGINE=InnoDB AUTO_INCREMENT=87 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Matches`
--

LOCK TABLES `Matches` WRITE;
/*!40000 ALTER TABLE `Matches` DISABLE KEYS */;
INSERT INTO `Matches` VALUES (1,0,NULL,NULL,1,24,27),(2,0,NULL,NULL,1,24,15),(3,0,NULL,NULL,1,24,14),(4,0,NULL,NULL,1,24,26),(5,0,NULL,NULL,1,24,25),(6,0,NULL,NULL,1,24,1),(7,0,NULL,NULL,1,24,4),(8,0,NULL,NULL,1,27,15),(9,0,NULL,NULL,1,27,14),(10,0,NULL,NULL,1,27,26),(11,0,NULL,NULL,1,27,25),(12,0,NULL,NULL,1,27,1),(13,0,NULL,NULL,1,27,4),(14,0,NULL,NULL,1,15,14),(15,0,NULL,NULL,1,15,26),(16,0,NULL,NULL,1,15,25),(17,0,NULL,NULL,1,15,1),(18,0,NULL,NULL,1,15,4),(19,0,NULL,NULL,1,14,26),(20,0,NULL,NULL,1,14,25),(21,0,NULL,NULL,1,14,1),(22,0,NULL,NULL,1,14,4),(23,0,NULL,NULL,1,26,25),(24,0,NULL,NULL,1,26,1),(25,0,NULL,NULL,1,26,4),(26,0,NULL,NULL,1,25,1),(27,0,NULL,NULL,1,25,4),(28,0,NULL,NULL,1,1,4),(29,0,NULL,NULL,2,3,28),(30,0,NULL,NULL,2,3,22),(31,0,NULL,NULL,2,3,11),(32,0,NULL,NULL,2,3,9),(33,0,NULL,NULL,2,3,16),(34,0,NULL,NULL,2,3,8),(35,0,NULL,NULL,2,3,5),(36,0,NULL,NULL,2,28,22),(37,0,NULL,NULL,2,28,11),(38,0,NULL,NULL,2,28,9),(39,0,NULL,NULL,2,28,16),(40,0,NULL,NULL,2,28,8),(41,0,NULL,NULL,2,28,5),(42,0,NULL,NULL,2,22,11),(43,0,NULL,NULL,2,22,9),(44,0,NULL,NULL,2,22,16),(45,0,NULL,NULL,2,22,8),(46,0,NULL,NULL,2,22,5),(47,0,NULL,NULL,2,11,9),(48,0,NULL,NULL,2,11,16),(49,0,NULL,NULL,2,11,8),(50,0,NULL,NULL,2,11,5),(51,0,NULL,NULL,2,9,16),(52,0,NULL,NULL,2,9,8),(53,0,NULL,NULL,2,9,5),(54,0,NULL,NULL,2,16,8),(55,0,NULL,NULL,2,16,5),(56,0,NULL,NULL,2,8,5),(57,0,NULL,NULL,3,18,6),(58,0,NULL,NULL,3,18,23),(59,0,NULL,NULL,3,18,12),(60,0,NULL,NULL,3,18,20),(61,0,NULL,NULL,3,18,2),(62,0,NULL,NULL,3,6,23),(63,0,NULL,NULL,3,6,12),(64,0,NULL,NULL,3,6,20),(65,0,NULL,NULL,3,6,2),(66,0,NULL,NULL,3,23,12),(67,0,NULL,NULL,3,23,20),(68,0,NULL,NULL,3,23,2),(69,0,NULL,NULL,3,12,20),(70,0,NULL,NULL,3,12,2),(71,0,NULL,NULL,3,20,2),(72,0,NULL,NULL,4,17,19),(73,0,NULL,NULL,4,17,21),(74,0,NULL,NULL,4,17,10),(75,0,NULL,NULL,4,17,7),(76,0,NULL,NULL,4,17,13),(77,0,NULL,NULL,4,19,21),(78,0,NULL,NULL,4,19,10),(79,0,NULL,NULL,4,19,7),(80,0,NULL,NULL,4,19,13),(81,0,NULL,NULL,4,21,10),(82,0,NULL,NULL,4,21,7),(83,0,NULL,NULL,4,21,13),(84,0,NULL,NULL,4,10,7),(85,0,NULL,NULL,4,10,13),(86,0,NULL,NULL,4,7,13);
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
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Player`
--

LOCK TABLES `Player` WRITE;
/*!40000 ALTER TABLE `Player` DISABLE KEYS */;
INSERT INTO `Player` VALUES (1,1,0,1,2,1),(2,1,0,3,1,2),(3,1,0,2,2,3),(4,1,0,1,2,4),(5,1,0,2,2,5),(6,1,0,3,1,8),(7,1,0,4,1,7),(8,1,0,2,2,7),(9,1,0,2,2,9),(10,1,0,4,1,9),(11,1,0,2,2,6),(12,1,0,3,1,10),(13,1,0,4,1,11),(14,1,0,1,2,12),(15,1,0,1,2,14),(16,1,0,2,2,15),(17,1,0,4,1,16),(18,1,0,3,1,17),(19,1,0,4,1,18),(20,1,0,3,1,19),(21,1,0,4,1,21),(22,1,0,2,2,21),(23,1,0,3,1,20),(24,1,0,1,2,22),(25,1,0,1,2,23),(26,1,0,1,2,24),(27,1,0,1,2,25),(28,1,0,2,2,26);
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
INSERT INTO `Round` VALUES (1,1,'2013-09-06 04:00:00','2013-07-30 04:00:00',1,NULL),(2,1,'2013-09-06 04:00:00','2013-07-30 04:00:00',2,NULL);
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
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `User`
--

LOCK TABLES `User` WRITE;
/*!40000 ALTER TABLE `User` DISABLE KEYS */;
INSERT INTO `User` VALUES (1,12,'jamesdbloom@gmail.com',0,'07515 900 569',1,'James D Bloom','','0f72a1756e47aa4d4068a356d85f4f7d2da59d7045487b9f20d45fb6f73b303d3bd394bd483fe8093381fa8cd82ccfb356174133833d3a6e3f6d5a03532fbfcf66477b4658aa8ed6'),(2,3,'nmalde@bechtel.com',0,'07733260764',1,'Neil Malde','','4fd3398c8e14d6c735d962d30937816651f489b56bc76345fc2c0f7929333cfaf00e66775c1944fba4c5872aa239cf7b4ce7287f0a33e020e8034e0bd52a308c2d59aa416edf7dc1'),(3,1,'m.hilton@m3c.co.uk',0,'07826948397',1,'Matt Hilton','','1c19e496fab0cb35fbb58416f6585f3c0d7f014bf380c3d7bd3680475335562bee9aa6eeb167c8a9660e56e2851b295b6f0700dfd9ee0abb77fa7ac8574ba7d678cb9ee96d182f6e'),(4,3,'jk@nowmedical.co.uk',3,'07960585315',2,'John KEEN','6c253350-f822-11e2-b522-000000000000','b8311a93922db8d8cd960f05941906edc684abba99db740a84628698eb50e09024cf04707dfbc2da0819c80eb6c986762c5dd9e20c99823dd07abca816d71beede934502ff1d2850'),(5,1,'samy.iralour@gmail.com',0,'07587141276',1,'samy','','bf09b6071ac2a36cbe4088f77f98c7b161d31e03b459c33935ee8f0c3aca956b68be70a2a7a94f7c4ff77d1aaff7400c39ac2bb8435d5cf2d7e975b0d51f1b171ed656148f9142bb'),(6,1,'swantonray@gmail.com',0,'07983530293',2,'Ray Swanton','','8532a7e68c1c1d9381b67900a60dd862148b19236f5b821c7834b2c3ebfff6493ec7cffaacc1fc75ee4f01dc288af1f20b4a975772216e0d3b738f1c825906e1552582788d700977'),(7,2,'swasey.rupert@gmail.com',0,'07906017618',2,'Rupert','','d049465ebd00c4588eebd3b671d2b60a2fb4e2a94ba633c68f9e194a3cef7e40578d0e63aca592033105f9a7f7c06a74aaeefd17c7b3e8e26ca55b8371c6193e93e868ee3f0eb8e2'),(8,3,'alistair.cossins@disney.com',0,'07787 260005',2,'Alistair Cossins','e7630c50-f826-11e2-b522-000000000000','f52572963c34518f7973b89beed6f195d39273e5def14303abcf35d4faceaef285fa2cef2c450c0b50f8c4bf81f6137453d63333798ef5d0d0acc0e86e191c6b46c26a7b42ea01b9'),(9,1,'rlpile@gmail.com',0,'07947 643747',2,'Richard Pile','','ca3d327cbc17323f2e2edc89920c47d17d70732c621c45948faa616aa7a4d55b8ab908e2ca37c1f1f08686fdf2ff979f21ee0ca51939525b4c411e1770d8e8c5f7ed9017b2697e0d'),(10,1,'jardine.finn@lbhf.gov.uk',0,'07903864846',2,'Jardine Finn','','6f82f4177347b9808c35716c2050be2a017d92f455bc63a2b08ef89e37b00c2c6f7fa750a987f3540e7f5ec468dc73161991e49399affab1089dbc070ec8b65286253918d43ee637'),(11,1,'scott.hands@vodafone.com',0,'07822859394',2,'Scott Hands','','4cd0697304c11fc2eac9fe30b33e37186a34332acd5dac94759a326fc3bcf2cf50e5b8a5f98b368e39a9d5b2ab3ba477640a9bbf061374856846156d8cdfd97c94528df582916f8c'),(12,1,'benoit.obadia@gmail.com',0,'07943934373',2,'Benoit','','665aa30fb696562d748e5e5fcd634cf1b10db6827c7277d334ba33224d7b612819e2a2acb49970a071fd2be4bf0402a9d89443653a2bda7564f2a648997b4ab25324b269856d08c1'),(13,1,'timvannispen@gmail.com',0,'07557949594',1,'Tim van Nispen','','5aa2274c5f6bf7291cd5e7f7beb4eb2e0432ded70004ced3a7702c22b81094740192faf02c53b67a383dbaa848df6162c7059265c335e59a73a279782eab43d9a3c550dce4ed2b3d'),(14,1,'benbhall1@gmail.com',0,'07768440727',2,'Ben Hall','','e33f487114256d4dfc9d0519d7a04d3266f53442a847e22e82dafe0399b05f61ab9d0664680de026b862ba313435a10fdfe2836335f32a9081f594d8b32b214b0b02ec6775d25bab'),(15,1,'roy.strachan@gmx.com',0,'07428068637',1,'Roy Strachan','','170f468ab6ec4cec8d1bbef91c914806449e73cfbe7498093ba383ec6ec499fc3ffbe15603392df9b2edc2cd4545ed19106286e3865908d01a31322136ec2693399a0bfc2ce4dc7d'),(16,1,'cin6304173@aol.com',0,'07787375914',2,'colin ingram','','86dfd4dc3b84355ff1cd35b9ba630d3868b489224a3675c993929aa8fd2a4225eabcf5d8a53c1370d04717915f56675b56dc9010f52bdb82764590787cb8a10580305fe0e7e15fd9'),(17,1,'j.townshend@mmu.ac.uk',0,'07950996387',1,' jules townshend','','ca4dcea39c657183c7273c654d27d23a74cd48007b50f39652d7ff692d9951c85c6f2cabc77533bf31aadb041f785f4372b328308eb84237b3e55e04623510694968478bf4b7ee7c'),(18,1,'philipmharrison2@gmail.com',0,'07730 822637',2,'Phil Harrison','','50c071bfccab9088633d5fafae3fc36a2f43cb20eac6dc982ece4287df65e18ddb2aa94d83cc56eb19f619db713f678e1328b1afbc1bce15946775547bc749881bd3899e6c91bd08'),(19,1,'lkshoe@yahoo.com',0,'07850 859 265',2,'Larry Shoemaker','','1e29c4f6569a394e364b3781960507f3a6289fd077d65a5abae29878e3cadf12cd10125ece5222df903054d0033e84536e58a8b987750b0da2b1a65cb56b8f457da6a4561fcb88c5'),(20,1,'bretstallwood@hotmail.com',0,'07791513942',1,'Bret','','019e90270783b9ad4a94fd82f35359e995ddcabfcc67bff5cdd0f6083f2495aaff25a3471588cd65dda06c94ec9520e19d1c2cf5c32b03628d87ccb85ed8d6dd4d12ae99a393b9e2'),(21,1,'james.rachael@gmail.com',0,'07760669232',1,'Rachael','','37f0031dffa2faacb64448545147f6b5fbc039ed328be47133ad423dfd653fa8f558604303b281adfbc3ff2c010356b120299bb9b2ad826ca8a845fdab046db18ee2c01a2a130a36'),(22,1,'andrea.caldera@gmail.com',0,'07850404421',2,'Andrea Caldera','','15399ddc3ddb1c4a563b57a2c25c5150536342b79d570767e7658d7af626cc6e9ee430ad44184e5b6ecdf431afdc0516bf1bcc06df7b212a8b3dd96b597ccbdfed05d7ff2cc038e1'),(23,1,'david@beguier.com',0,'07770694009',1,'David B-B','','35b24996fcf1656031c3762c0686290262d19100c5458d40f66716bfdd6c7f931a8888e8e2edb309e1640baaefb35431de3912a3a64c4e428f5214c05dcd42f748aeb4beee0948fe'),(24,1,'morley_daniel@yahoo.co.uk',0,'07793037582',1,'Dan Morley','','b9ab637060b6b5cec735f0f85e61a8ff700bf481bc5e60b683c0f264e23e78c3ffcbcff21af848abcdf2e82b7028f29df8ba6e11d3bcaf3bcf028153ccea9ea04ac977519118a2e4'),(25,1,'benjamin.j.cochrane@gmail.com',0,'07590505519',2,'Ben Cochrane','','d525b80be594524f41c38dc0185494789dbec3cbd5d1426513505839d96e924a53c3f602e284c314bf9cb5de33c9d4441940be384f6c913a09829c5079b5f9bdfaffab495adc275a'),(26,1,'merlin.fair08@imperial.ac.uk',0,'07596220972',1,'Merlin','','624fd3a85aea5797a1c114ea9e7dd901e544797838af45cca98b0ed3aa024fd0ff5b43cfc365cff7d88f90ab603fa367a1fe432d986a935acda38d4c36c58784a9e37eb519cb3b52');
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
INSERT INTO `User_Role` VALUES (1,1),(2,2),(3,2),(4,2),(5,2),(6,2),(7,2),(8,2),(9,2),(10,2),(11,2),(12,2),(13,2),(14,2),(15,2),(16,2),(17,2),(18,2),(19,2),(20,2),(21,2),(22,2),(23,2),(24,2),(25,2),(26,2);
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

-- Dump completed on 2013-08-01  1:36:02
