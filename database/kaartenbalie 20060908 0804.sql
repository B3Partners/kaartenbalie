-- MySQL Administrator dump 1.4
--
-- ------------------------------------------------------
-- Server version	4.1.9-nt-max


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


--
-- Create schema kaartenbalie
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ kaartenbalie;
USE kaartenbalie;

--
-- Structure for table `kaartenbalie`.`roles`
--

DROP TABLE IF EXISTS `kaartenbalie`.`roles`;
CREATE TABLE  `kaartenbalie`.`roles` (
  `user_id` int(10) unsigned NOT NULL default '0',
  `role` varchar(45) NOT NULL default ''
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `kaartenbalie`.`roles`
--

/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `kaartenbalie`.`roles` (`user_id`,`role`) VALUES 
 (2,'beheerder'),
 (1,'gebruiker');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;


--
-- Structure for table `kaartenbalie`.`users`
--

DROP TABLE IF EXISTS `kaartenbalie`.`users`;
CREATE TABLE  `kaartenbalie`.`users` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `USERNAME` varchar(45) NOT NULL default '',
  `PASSWORD` varchar(45) NOT NULL default '',
  `FIRSTNAME` varchar(45) NOT NULL default '',
  `LASTNAME` varchar(45) NOT NULL default '',
  `EMAIL` varchar(45) NOT NULL default '',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `kaartenbalie`.`users`
--

/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `kaartenbalie`.`users` (`id`,`USERNAME`,`PASSWORD`,`FIRSTNAME`,`LASTNAME`,`EMAIL`) VALUES 
 (1,'roy','roy','Roy','Braam','roybraam@b3partners.nl'),
 (2,'nando','nando','Nando','de Goeij','nandodegoeij@b3partners.nl');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
