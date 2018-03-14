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
-- Table structure for table `tomcat_sessions`
--

DROP TABLE IF EXISTS `tomcat_sessions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tomcat_sessions` (
  `session_id` varchar(100) NOT NULL,
  `valid_session` char(1) NOT NULL,
  `max_inactive` int(11) NOT NULL,
  `last_access` bigint(20) NOT NULL,
  `app_name` varchar(255) DEFAULT NULL,
  `session_data` mediumblob,
  PRIMARY KEY (`session_id`),
  KEY `kapp_name` (`app_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tomcat_sessions`
--

LOCK TABLES `tomcat_sessions` WRITE;
/*!40000 ALTER TABLE `tomcat_sessions` DISABLE KEYS */;
INSERT INTO `tomcat_sessions` VALUES ('056FDE113A04A5D1330F7E6A8132F750','1',1800,1520458112005,'/Catalina/localhost/','¨\Ì\0sr\0java.lang.Long;ã\‰êÃè#\ﬂ\0J\0valuexr\0java.lang.NumberÜ¨ïî\‡ã\0\0xp\0\0b]úsq\0~\0\0\0\0b]úsr\0java.lang.Integer‚†§˜Åá8\0I\0valuexq\0~\0\0\0sr\0java.lang.Boolean\Õ rÄ’ú˙\Ó\0Z\0valuexp\0sq\0~\0sq\0~\0\0\0\0b]út\0 056FDE113A04A5D1330F7E6A8132F750sq\0~\0\0\0\0\0'),('9544A63005BBB65F498F7FBB346FA5E7','1',1800,1520458522539,'/Catalina/localhost/','¨\Ì\0sr\0java.lang.Long;ã\‰êÃè#\ﬂ\0J\0valuexr\0java.lang.NumberÜ¨ïî\‡ã\0\0xp\0\0bbÚ\Ÿsq\0~\0\0\0\0bcﬂ´sr\0java.lang.Integer‚†§˜Åá8\0I\0valuexq\0~\0\0\0sr\0java.lang.Boolean\Õ rÄ’ú˙\Ó\0Z\0valuexp\0sq\0~\0sq\0~\0\0\0\0bcﬂ´t\0 9544A63005BBB65F498F7FBB346FA5E7sq\0~\0\0\0\0\0'),('E0C0BD593154A97F79E2A81C3998314B','1',1800,1520458300313,'/Catalina/localhost/','¨\Ì\0sr\0java.lang.Long;ã\‰êÃè#\ﬂ\0J\0valuexr\0java.lang.NumberÜ¨ïî\‡ã\0\0xp\0\0b]úsq\0~\0\0\0\0b`{ôsr\0java.lang.Integer‚†§˜Åá8\0I\0valuexq\0~\0\0\0sr\0java.lang.Boolean\Õ rÄ’ú˙\Ó\0Z\0valuexp\0sq\0~\0sq\0~\0\0\0\0b`{ôt\0 E0C0BD593154A97F79E2A81C3998314Bsq\0~\0\0\0\0t\0emailt\0b@b.b');
/*!40000 ALTER TABLE `tomcat_sessions` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-03-13 14:20:26
