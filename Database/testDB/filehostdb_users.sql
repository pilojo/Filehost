-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: filehosttest
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
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `Username` varchar(32) DEFAULT NULL,
  `First_Name` varchar(32) NOT NULL,
  `Last_Name` varchar(150) NOT NULL,
  `Email` varchar(150) NOT NULL,
  `AccountType_ID` bigint(20) DEFAULT NULL,
  `RootFolder_ID` bigint(20) DEFAULT NULL,
  `Password` varchar(150) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ID_UNIQUE` (`ID`),
  UNIQUE KEY `Email_UNIQUE` (`Email`),
  UNIQUE KEY `Username_UNIQUE` (`Username`),
  KEY `fk_Users_AccountTypes_idx` (`AccountType_ID`),
  KEY `fk_Users_Folders1_idx` (`RootFolder_ID`),
  CONSTRAINT `fk_Users_AccountTypes` FOREIGN KEY (`AccountType_ID`) REFERENCES `accounttypes` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Users_Folders1` FOREIGN KEY (`RootFolder_ID`) REFERENCES `folders` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (2,'Fullmetal Alchemist','Edward','Elric','FMA@dogofmilitary.ca',1,NULL,'s3@rching4Phil'),(23,'rer','lel','rer','em@bleh.cum',4,6,'dontHackPlz'),(25,'a','a','b','a@a.a',4,8,'$2a$10$9sNR423SWxufvhpH5pJ.heuN795lCPjcD0GbTQMorzYNlD2Y.c3ou'),(26,'aaaa','aaaa','aaaaa','aaa@aaaa.aaa',4,9,'$2a$10$52AK6dG7IquRLmZq0ADocuTXNp3uSgNaDp.aKfbJhLmhjElI2dlZS'),(27,'b','b','b','b@b.b',4,10,'$2a$10$VuSIgWZTJsY3VoowpcgF4.vcctmYrFFVV6AutBoigwPTuArKn6JTC'),(28,'pilojo','John','Pilon','jp9723@hotmail.com',4,11,'$2a$10$INAIJwzuEOK2QhfQeAy1b.KtjPa/zyKsZ80yyOiVT10HrKKh.f90e');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-03-08  9:28:57
