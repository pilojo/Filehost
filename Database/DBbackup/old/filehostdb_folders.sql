-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: filehostdb
-- ------------------------------------------------------
-- Server version	5.7.21-log

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
-- Table structure for table `folders`
--

DROP TABLE IF EXISTS `folders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `folders` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `Name` varchar(32) NOT NULL,
  `Parent_Path` text NOT NULL,
  `Owner_ID` bigint(20) NOT NULL,
  `Permission_ID` bigint(20) NOT NULL,
  `ParentFolder_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ID_UNIQUE` (`ID`),
  KEY `fk_Folders_Users1_idx` (`Owner_ID`),
  KEY `fk_Folders_Permissions1_idx` (`Permission_ID`),
  KEY `fk_parent_folder_id_idx` (`ParentFolder_ID`),
  CONSTRAINT `fk_Folders_Permissions1` FOREIGN KEY (`Permission_ID`) REFERENCES `permissions` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_parent_folder_id` FOREIGN KEY (`ParentFolder_ID`) REFERENCES `folders` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_id` FOREIGN KEY (`Owner_ID`) REFERENCES `users` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `folders`
--

LOCK TABLES `folders` WRITE;
/*!40000 ALTER TABLE `folders` DISABLE KEYS */;
INSERT INTO `folders` VALUES (6,'pilojo','/',3,1,NULL),(7,'TEST','/pilojo/',3,1,6),(8,'AAA','/pilojo/',3,1,6),(9,'BBB','/pilojo/',3,1,6),(10,'CCC','/pilojo/',3,1,6),(11,'DDD','/pilojo/',3,1,6),(12,'EEE','/pilojo/',3,1,6),(13,'FFF','/pilojo/',3,1,6),(14,'GGG','/pilojo/',3,1,6),(15,'HHH','/pilojo/',3,1,6),(16,'III','/pilojo/',3,1,6),(17,'JJJ','/pilojo/',3,1,6),(18,'KKK','/pilojo/',3,1,6),(19,'LLL','/pilojo/',3,1,6),(20,'MMM','/pilojo/',3,1,6),(21,'NNN','/pilojo/',3,1,6),(22,'OOO','/pilojo/',3,1,6),(23,'PPP','/pilojo/',3,1,6),(24,'QQQ','/pilojo/',3,1,6),(25,'RRR','/pilojo/',3,1,6),(26,'SSS','/pilojo/',3,1,6),(27,'TTT','/pilojo/',3,1,6),(28,'UUU','/pilojo/',3,1,6),(29,'VVV','/pilojo/',3,1,6),(30,'WWW','/pilojo/',3,1,6),(31,'YYY','/pilojo/',3,1,6),(32,'ZZZ','/pilojo/',3,1,6),(33,'ABC','/pilojo/',3,1,6),(34,'ABCD','/pilojo/',3,1,6),(35,'ABCDE','/pilojo/',3,1,6),(36,'ABCDEF','/pilojo/',3,1,6),(37,'ABCDEFG','/pilojo/',3,1,6),(38,'ABCDEFGH','/pilojo/',3,1,6),(39,'ABCDEFGHI','/pilojo/',3,1,6),(40,'ABCDEFGHIJ','/pilojo/',3,1,6),(41,'A','/pilojo/',3,1,6),(42,'B','/pilojo/',3,1,6),(43,'asdf','/pilojo/DDD/',3,1,11),(44,'asdffff','/pilojo/DDD/asdf/',3,1,43);
/*!40000 ALTER TABLE `folders` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-03-13 14:20:25
