CREATE DATABASE  IF NOT EXISTS `std_loja` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `std_loja`;
-- MySQL dump 10.13  Distrib 5.5.16, for osx10.5 (i386)
--
-- Host: localhost    Database: std_loja
-- ------------------------------------------------------
-- Server version	5.5.19

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
-- Table structure for table `tipo_pagto_tabelas`
--

DROP TABLE IF EXISTS `tipo_pagto_tabelas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tipo_pagto_tabelas` (
  `tabela_financ_id` smallint(6) NOT NULL,
  `tipo_pagto_id` smallint(6) NOT NULL,
  PRIMARY KEY (`tabela_financ_id`,`tipo_pagto_id`),
  KEY `fk_tabela_financ_has_tipo_pagto_tipo_pagto1777632` (`tipo_pagto_id`),
  KEY `fk_tabela_financ_has_tipo_pagto_tabela_financ1119833` (`tabela_financ_id`),
  KEY `G4H132GJ4G1H2` (`tabela_financ_id`),
  KEY `2J523#452G3H4` (`tipo_pagto_id`),
  CONSTRAINT `2J523#452G3H4` FOREIGN KEY (`tipo_pagto_id`) REFERENCES `tipo_pagto` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `G4H132GJ4G1H2` FOREIGN KEY (`tabela_financ_id`) REFERENCES `tabela_financ` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tipo_pagto_tabelas`
--

LOCK TABLES `tipo_pagto_tabelas` WRITE;
/*!40000 ALTER TABLE `tipo_pagto_tabelas` DISABLE KEYS */;
INSERT INTO `tipo_pagto_tabelas` VALUES (1,1),(1,2),(1,3),(3,5),(1,11),(2,11),(3,11),(4,11),(11,11);
/*!40000 ALTER TABLE `tipo_pagto_tabelas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario_permissoes`
--

DROP TABLE IF EXISTS `usuario_permissoes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `usuario_permissoes` (
  `usuario_id` smallint(6) NOT NULL,
  `permissao` varchar(15) NOT NULL,
  PRIMARY KEY (`usuario_id`,`permissao`),
  KEY `us_perm_id_fk` (`usuario_id`),
  KEY `us_perm_nome_fk` (`permissao`),
  CONSTRAINT `us_perm_id_fk` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `us_perm_nome_fk` FOREIGN KEY (`permissao`) REFERENCES `permissao` (`nome`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario_permissoes`
--

LOCK TABLES `usuario_permissoes` WRITE;
/*!40000 ALTER TABLE `usuario_permissoes` DISABLE KEYS */;
INSERT INTO `usuario_permissoes` VALUES (1,'ADMIN'),(1,'DARDESC');
/*!40000 ALTER TABLE `usuario_permissoes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario`
--

DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `usuario` (
  `id` smallint(6) NOT NULL AUTO_INCREMENT,
  `nome` varchar(85) NOT NULL,
  `login` varchar(15) NOT NULL,
  `senha` varchar(45) NOT NULL,
  `ativo` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `nome_UNIQUE` (`nome`),
  UNIQUE KEY `login_UNIQUE` (`login`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario`
--

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` VALUES (1,'Administrador','admin','admin',1);
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `coeficiente`
--

DROP TABLE IF EXISTS `coeficiente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `coeficiente` (
  `tabela_financ_id` smallint(6) NOT NULL,
  `num_parcelas` smallint(6) NOT NULL,
  `coeficiente` float NOT NULL,
  PRIMARY KEY (`tabela_financ_id`,`num_parcelas`),
  KEY `fk_coeficiente_tabela_financ1891883` (`tabela_financ_id`),
  KEY `FD6F4566` (`tabela_financ_id`),
  CONSTRAINT `FD6F4566` FOREIGN KEY (`tabela_financ_id`) REFERENCES `tabela_financ` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `coeficiente`
--

LOCK TABLES `coeficiente` WRITE;
/*!40000 ALTER TABLE `coeficiente` DISABLE KEYS */;
INSERT INTO `coeficiente` VALUES (1,1,1),(2,1,1),(2,2,0.5),(2,3,0.3334),(2,4,0.25),(3,2,23),(3,3,2.54),(4,1,1.01),(4,3,8.768),(4,5,3),(11,1,1.002),(11,2,0.634543),(11,3,0.4313),(11,4,0.352),(11,5,0.200001),(11,6,0.19),(11,7,0.15),(11,8,0.14675);
/*!40000 ALTER TABLE `coeficiente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `produto`
--

DROP TABLE IF EXISTS `produto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `produto` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `descricao` varchar(40) DEFAULT NULL,
  `valor` decimal(7,2) DEFAULT NULL,
  `tipo` tinyint(4) NOT NULL DEFAULT '0',
  `fabricante` varchar(45) DEFAULT NULL,
  `modelo` varchar(45) DEFAULT NULL,
  `ano` varchar(15) DEFAULT NULL,
  `cor` varchar(30) DEFAULT NULL,
  `cilindradas` smallint(6) DEFAULT NULL,
  `placa` varchar(15) DEFAULT NULL,
  `chassi` varchar(20) DEFAULT NULL,
  `combustivel` tinyint(4) DEFAULT NULL,
  `obs` text,
  `qtd_estoque` int(11) DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `placa_UNIQUE` (`placa`),
  UNIQUE KEY `chassi_UNIQUE` (`chassi`),
  UNIQUE KEY `descricao_UNIQUE` (`descricao`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `produto`
--

LOCK TABLES `produto` WRITE;
/*!40000 ALTER TABLE `produto` DISABLE KEYS */;
INSERT INTO `produto` VALUES (1,'TESTE PRODUTO',200.00,1,'HONDA','biz','2006/2007','AZUL',150,'ASD-3476',NULL,0,NULL,0),(5,NULL,20000.00,1,'teste','hgfajsgfhasg','2010','qwyeruy',0,'QWE-5678',NULL,2,NULL,-1),(6,'DOCUMENTAÇÃO',1400.00,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,-1),(7,NULL,25000.00,1,'volks','fox','2006/2005','VERMELHA',1000,'FHF-1234',NULL,2,NULL,0),(8,NULL,10000.00,1,'','FIAT','2000','PRATA',1000,'FOX-3245','ADF4A65S4DF65',4,NULL,0),(9,NULL,2000.00,1,'jkhgjhgjh','jkfahgsdjh','3545','dgdgf',100,'GAS-7153','gd6556s54a',2,NULL,0),(10,'ADMINISTRAÇÃO',20.00,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0),(13,NULL,20000.00,1,'HONDA','CG','2007','BRANCA',150,'HGF-4123','5736542Y3FHG4',1,NULL,0),(17,NULL,0.00,1,'sfsfdsfds','asdfhgfhgf','4354','fdsfds',1000,'DGF-1234','fdshsxe45erf',5,NULL,1),(20,NULL,NULL,1,'HONDA','BIZ','2007','VERMELHA',100,'AAA-4321','GAJHDGFA6SDFA',1,NULL,0),(22,NULL,NULL,1,'HONDA','CG','2005','VERMELHA',150,'AAA-1111','23FHGFHJ13534',1,NULL,1),(30,NULL,NULL,1,'JKGDSAJHG','DGFDGFD','343564','SGFDSF',16,'AAA-1235','JDFDFGDGF',1,NULL,1),(31,NULL,0.00,1,'dsfdsf','fdsfd','2000','dgdsfd',100,'ARD-3435','hgfhasd454654',1,NULL,1),(33,'TAXAS EXTRA',0.00,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0),(35,NULL,0.00,1,'TESTE','TESTE','2000','VERMELHA',0,'AAA-1234','',1,NULL,1),(39,NULL,0.00,1,'TESTE','TESTE','2000','TESTE',0,'AAA-1238','AJHGFJAHSD',1,NULL,1),(40,NULL,0.00,1,'YWEQWIUYT','FGASFj','2007','verde',0,'AAA-1122',NULL,3,NULL,1),(41,NULL,12000.00,1,'FIAT','UNO','2000','BRANCA',1000,'ABC-1122',NULL,1,NULL,1),(42,NULL,5000.00,1,'HONDA','CG150','2006','VERMELHA',150,'QQQ1234','1234567890-QWERTYUI',1,NULL,0);
/*!40000 ALTER TABLE `produto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pedido`
--

DROP TABLE IF EXISTS `pedido`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pedido` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `data` datetime DEFAULT NULL,
  `cliente_id` int(11) NOT NULL,
  `valor_total` decimal(7,2) NOT NULL,
  `desconto` decimal(3,2) DEFAULT NULL,
  `usuario_id` smallint(6) NOT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `fk_pedido_cliente113451` (`cliente_id`),
  KEY `fk_pedido_usuario15351` (`usuario_id`),
  KEY `J2HG5J2H345` (`cliente_id`),
  KEY `G52H452` (`usuario_id`),
  CONSTRAINT `G52H452` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `J2HG5J2H345` FOREIGN KEY (`cliente_id`) REFERENCES `cliente` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=76 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pedido`
--

LOCK TABLES `pedido` WRITE;
/*!40000 ALTER TABLE `pedido` DISABLE KEYS */;
INSERT INTO `pedido` VALUES (2,'2010-01-01 00:00:00',1,0.00,0.00,1,0),(4,NULL,39,20.00,NULL,1,0),(6,NULL,41,1400.00,NULL,1,0),(7,NULL,47,5000.00,NULL,1,0),(8,NULL,41,200.00,NULL,1,0),(9,NULL,41,1400.00,NULL,1,0),(10,NULL,47,190.00,NULL,1,0),(11,NULL,38,20000.00,NULL,1,0),(12,NULL,39,10000.00,NULL,1,0),(13,NULL,37,21400.00,NULL,1,0),(14,'2012-07-09 16:48:07',26,1400.00,NULL,1,0),(15,'2012-07-09 17:11:37',38,10000.00,NULL,1,0),(17,'2012-07-09 17:23:59',38,10000.00,NULL,1,0),(18,'2012-07-09 17:41:41',38,3200.00,NULL,1,0),(21,'2012-07-09 18:00:53',41,21400.00,NULL,1,0),(22,'2012-07-09 18:09:35',39,20000.00,NULL,1,0),(23,'2012-07-09 18:21:26',38,200.00,NULL,1,0),(24,'2012-07-09 18:23:27',52,21400.00,NULL,1,0),(25,'2012-07-09 19:50:47',37,20000.00,NULL,1,0),(26,'2012-07-13 21:29:40',47,20000.00,NULL,1,0),(30,'2012-07-15 14:12:16',26,20000.00,NULL,1,0),(43,'2012-07-15 16:03:05',1,200.00,NULL,1,0),(44,'2012-07-15 16:08:04',1,20000.00,NULL,1,0),(45,'2012-07-15 16:14:02',1,1400.00,NULL,1,0),(46,'2012-07-15 16:16:46',1,200.00,NULL,1,0),(47,'2012-07-15 16:20:58',1,1400.00,NULL,1,0),(48,'2012-07-15 16:42:18',1,1400.00,NULL,1,0),(49,'2012-07-15 16:44:08',35,1400.00,NULL,1,0),(50,'2012-07-15 21:57:33',1,25000.00,NULL,1,0),(51,'2012-07-16 21:16:55',37,11400.00,NULL,1,0),(52,'2012-07-16 21:25:30',26,21400.00,NULL,1,0),(53,'2012-07-16 21:29:32',35,1400.00,NULL,1,0),(63,'2012-07-16 21:56:58',26,1400.00,NULL,1,0),(64,'2012-07-16 21:59:42',39,1400.00,NULL,1,0),(65,'2012-07-16 22:08:04',26,1400.00,NULL,1,0),(66,'2012-07-16 22:10:11',37,1400.00,NULL,1,0),(67,'2012-07-16 22:17:37',39,1400.00,NULL,1,0),(68,'2012-07-16 22:22:32',47,900.00,NULL,1,0),(69,'2012-07-16 22:28:47',37,1400.00,NULL,1,0),(70,'2012-07-16 23:05:14',38,2000.00,NULL,1,0),(71,'2012-07-18 00:45:12',50,1400.00,NULL,1,0),(72,'2012-07-18 18:36:33',38,20.00,NULL,1,0),(73,'2012-07-18 19:50:10',35,1400.00,NULL,1,0),(74,'2012-07-21 11:31:25',47,34.44,NULL,1,0),(75,'2012-07-21 15:03:14',53,5000.00,NULL,1,0);
/*!40000 ALTER TABLE `pedido` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `permissao`
--

DROP TABLE IF EXISTS `permissao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `permissao` (
  `nome` varchar(15) NOT NULL,
  PRIMARY KEY (`nome`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `permissao`
--

LOCK TABLES `permissao` WRITE;
/*!40000 ALTER TABLE `permissao` DISABLE KEYS */;
INSERT INTO `permissao` VALUES ('ADMIN'),('DARDESC'),('EDITCLIENTE');
/*!40000 ALTER TABLE `permissao` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pedido_pag`
--

DROP TABLE IF EXISTS `pedido_pag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pedido_pag` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `valor` decimal(7,2) NOT NULL,
  `num_parcelas` smallint(6) NOT NULL,
  `tipo_pagto_id` smallint(6) NOT NULL,
  `pedido_id` int(11) NOT NULL,
  `tabela_financ_id` smallint(6) NOT NULL,
  `data_venc` date NOT NULL,
  `frequencia` tinyint(4) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `fk_pedido_pag_tipo_pagto1512390` (`tipo_pagto_id`),
  KEY `fk_pedido_pag_pedido11235635` (`pedido_id`),
  KEY `fk_pedido_pag_tabela_financ1155553` (`tabela_financ_id`),
  KEY `23JH54` (`tipo_pagto_id`),
  KEY `JHK2345H5` (`pedido_id`),
  KEY `KGJ23H4532` (`tabela_financ_id`),
  CONSTRAINT `23JH54` FOREIGN KEY (`tipo_pagto_id`) REFERENCES `tipo_pagto` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `JHK2345H5` FOREIGN KEY (`pedido_id`) REFERENCES `pedido` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `KGJ23H4532` FOREIGN KEY (`tabela_financ_id`) REFERENCES `tabela_financ` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=75 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pedido_pag`
--

LOCK TABLES `pedido_pag` WRITE;
/*!40000 ALTER TABLE `pedido_pag` DISABLE KEYS */;
INSERT INTO `pedido_pag` VALUES (2,190.00,1,1,10,1,'2012-07-09',0),(3,20000.00,1,1,11,1,'2012-07-09',0),(4,10000.00,1,1,12,1,'2012-07-09',0),(5,21400.00,2,11,13,11,'2012-08-09',0),(6,100.00,1,1,14,1,'2012-07-09',0),(7,10000.00,2,11,15,11,'2012-08-09',0),(9,10000.00,4,11,17,11,'2012-08-09',0),(10,200.00,1,1,18,1,'2012-07-09',0),(11,3000.00,4,11,18,2,'2012-08-10',0),(14,1000.00,1,1,21,1,'2012-07-09',0),(15,20400.00,8,11,21,11,'2012-08-09',0),(16,20000.00,8,11,22,11,'2012-08-09',0),(17,200.00,2,11,23,11,'2012-08-09',0),(18,21400.00,3,5,24,3,'2012-08-09',0),(19,20000.00,3,5,25,3,'2012-12-01',0),(20,20000.00,5,11,26,11,'2012-08-13',0),(25,1000.00,1,1,30,1,'2012-07-15',0),(26,19000.00,7,11,30,11,'2013-01-01',0),(37,200.00,1,1,43,1,'2012-07-15',0),(38,20000.00,1,1,44,1,'2012-07-15',0),(39,1400.00,1,1,45,1,'2012-07-15',0),(40,200.00,1,1,46,1,'2012-07-15',0),(41,1400.00,1,1,47,1,'2012-07-15',0),(42,1400.00,1,1,48,1,'2012-07-15',0),(43,1400.00,1,1,49,1,'2012-07-15',0),(44,25000.00,1,1,50,1,'2012-07-15',0),(45,1000.00,1,1,51,1,'2012-07-01',0),(46,5000.00,6,11,51,11,'2012-08-16',0),(47,5400.00,3,5,51,3,'2012-08-16',0),(48,1000.00,1,1,52,1,'2012-07-16',0),(49,18000.00,8,11,52,11,'2012-08-16',0),(50,2400.00,1,2,52,1,'2012-07-16',0),(51,1400.00,4,11,53,11,'2012-08-16',0),(61,1400.00,4,11,63,11,'2012-08-16',0),(62,1400.00,4,11,64,11,'2012-08-16',0),(63,1400.00,4,11,65,11,'2012-08-16',0),(64,1400.00,4,11,66,11,'2012-08-16',0),(65,1400.00,4,11,67,11,'2012-08-16',0),(66,900.00,2,5,68,3,'2012-08-16',0),(67,1400.00,8,11,69,11,'2012-08-16',0),(68,2000.00,8,11,70,11,'2012-08-16',0),(69,1400.00,2,11,71,11,'2012-08-18',0),(70,20.00,1,1,72,1,'2012-07-18',0),(71,1400.00,1,1,73,1,'2012-07-01',0),(72,34.44,1,1,74,1,'2012-07-18',0),(73,500.00,1,1,75,1,'2012-07-21',0),(74,4500.00,6,11,75,11,'2012-08-21',0);
/*!40000 ALTER TABLE `pedido_pag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `boleto`
--

DROP TABLE IF EXISTS `boleto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `boleto` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `vencimento` date NOT NULL,
  `valor` decimal(7,2) NOT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '0',
  `cliente_id` int(11) NOT NULL,
  `pedido_pag_id` int(11) NOT NULL,
  `parcela_n` smallint(6) DEFAULT NULL,
  `dataPag` date DEFAULT NULL,
  `valorPago` decimal(7,2) DEFAULT NULL,
  `valorFaltante` decimal(7,0) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_boleto_cliente1110119532` (`cliente_id`),
  KEY `fk_boleto_pedido_pag132195` (`pedido_pag_id`),
  KEY `XC234523` (`cliente_id`),
  KEY `BN262436` (`pedido_pag_id`),
  CONSTRAINT `BN262436` FOREIGN KEY (`pedido_pag_id`) REFERENCES `pedido_pag` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `XC234523` FOREIGN KEY (`cliente_id`) REFERENCES `cliente` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=138 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `boleto`
--

LOCK TABLES `boleto` WRITE;
/*!40000 ALTER TABLE `boleto` DISABLE KEYS */;
INSERT INTO `boleto` VALUES (1,'2012-08-09',0.00,0,38,7,NULL,NULL,NULL,NULL),(2,'2012-09-09',0.00,0,38,7,NULL,NULL,NULL,NULL),(3,'2012-09-09',3520.00,0,38,9,NULL,NULL,NULL,NULL),(4,'2011-08-09',3520.00,0,38,9,NULL,NULL,NULL,NULL),(5,'2012-10-09',3520.00,0,38,9,NULL,NULL,NULL,NULL),(6,'2012-11-09',3520.00,0,38,9,NULL,NULL,NULL,NULL),(7,'2012-10-10',750.00,0,38,11,NULL,NULL,NULL,NULL),(8,'2012-08-10',750.00,0,38,11,NULL,NULL,NULL,NULL),(9,'2012-09-10',750.00,0,38,11,NULL,NULL,NULL,NULL),(10,'2012-11-10',750.00,0,38,11,NULL,NULL,NULL,NULL),(11,'2012-08-09',2993.70,0,41,15,NULL,NULL,NULL,NULL),(12,'2012-11-09',2993.70,0,41,15,NULL,NULL,NULL,NULL),(13,'2013-01-09',2993.70,0,41,15,NULL,NULL,NULL,NULL),(14,'2012-10-09',2993.70,0,41,15,NULL,NULL,NULL,NULL),(15,'2013-03-09',2993.70,0,41,15,NULL,NULL,NULL,NULL),(16,'2012-09-09',2993.70,0,41,15,NULL,NULL,NULL,NULL),(17,'2012-12-09',2993.70,0,41,15,NULL,NULL,NULL,NULL),(18,'2013-02-09',2993.70,0,41,15,NULL,NULL,NULL,NULL),(19,'2012-11-09',2935.00,0,39,16,NULL,NULL,NULL,NULL),(20,'2012-12-09',2935.00,0,39,16,NULL,NULL,NULL,NULL),(21,'2012-08-09',2935.00,0,39,16,NULL,NULL,NULL,NULL),(22,'2013-02-09',2935.00,0,39,16,NULL,NULL,NULL,NULL),(23,'2013-01-09',2935.00,0,39,16,NULL,NULL,NULL,NULL),(24,'2013-03-09',2935.00,0,39,16,NULL,NULL,NULL,NULL),(25,'2012-09-09',2935.00,0,39,16,NULL,NULL,NULL,NULL),(26,'2012-10-09',2935.00,0,39,16,NULL,NULL,NULL,NULL),(27,'2012-09-09',126.90,0,38,17,NULL,NULL,NULL,NULL),(28,'2012-08-09',126.90,0,38,17,NULL,NULL,NULL,NULL),(29,'2012-09-09',54355.99,0,52,18,NULL,NULL,NULL,NULL),(30,'2012-10-09',54355.99,0,52,18,NULL,NULL,NULL,NULL),(31,'2012-08-09',54355.99,0,52,18,NULL,NULL,NULL,NULL),(32,'2013-02-01',50799.99,0,37,19,NULL,NULL,NULL,NULL),(33,'2012-12-01',50799.99,0,37,19,NULL,NULL,NULL,NULL),(34,'2013-01-01',50799.99,0,37,19,NULL,NULL,NULL,NULL),(35,'2012-08-13',4000.02,0,47,20,NULL,NULL,NULL,NULL),(36,'2012-09-13',4000.02,0,47,20,NULL,NULL,NULL,NULL),(37,'2012-10-13',4000.02,0,47,20,NULL,NULL,NULL,NULL),(38,'2012-11-13',4000.02,0,47,20,NULL,NULL,NULL,NULL),(39,'2012-12-13',4000.02,0,47,20,NULL,NULL,NULL,NULL),(48,'2013-01-01',2850.00,0,26,26,1,NULL,NULL,NULL),(49,'2013-02-01',2850.00,0,26,26,2,NULL,NULL,NULL),(50,'2013-03-01',2850.00,0,26,26,3,NULL,NULL,NULL),(51,'2013-04-01',2850.00,0,26,26,4,NULL,NULL,NULL),(52,'2013-05-01',2850.00,0,26,26,5,NULL,NULL,NULL),(53,'2013-06-01',2850.00,0,26,26,6,NULL,NULL,NULL),(54,'2013-07-01',2850.00,0,26,26,7,NULL,NULL,NULL),(55,'2012-08-16',949.99,0,37,46,1,NULL,NULL,NULL),(56,'2012-09-16',949.99,0,37,46,2,NULL,NULL,NULL),(57,'2012-10-16',949.99,0,37,46,3,NULL,NULL,NULL),(58,'2012-11-16',949.99,0,37,46,4,NULL,NULL,NULL),(59,'2012-12-16',949.99,0,37,46,5,NULL,NULL,NULL),(60,'2013-01-16',949.99,0,37,46,6,NULL,NULL,NULL),(61,'2012-08-16',13715.99,0,37,47,1,NULL,NULL,NULL),(62,'2012-09-16',13715.99,0,37,47,2,NULL,NULL,NULL),(63,'2012-10-16',13715.99,0,37,47,3,NULL,NULL,NULL),(64,'2012-08-16',2641.50,0,26,49,1,NULL,NULL,NULL),(65,'2012-09-16',2641.50,0,26,49,2,NULL,NULL,NULL),(66,'2012-10-16',2641.50,0,26,49,3,NULL,NULL,NULL),(67,'2012-11-16',2641.50,0,26,49,4,NULL,NULL,NULL),(68,'2012-12-16',2641.50,0,26,49,5,NULL,NULL,NULL),(69,'2013-01-16',2641.50,0,26,49,6,NULL,NULL,NULL),(70,'2013-02-16',2641.50,0,26,49,7,NULL,NULL,NULL),(71,'2013-03-16',2641.50,0,26,49,8,NULL,NULL,NULL),(72,'2012-08-16',492.79,0,35,51,1,NULL,NULL,NULL),(73,'2012-09-16',492.79,0,35,51,2,NULL,NULL,NULL),(74,'2012-10-16',492.79,0,35,51,3,NULL,NULL,NULL),(75,'2012-11-16',492.79,0,35,51,4,NULL,NULL,NULL),(92,'2012-11-16',492.79,0,26,61,4,NULL,NULL,NULL),(93,'2012-09-16',492.79,0,26,61,2,NULL,NULL,NULL),(94,'2012-08-16',492.79,0,26,61,1,NULL,NULL,NULL),(95,'2012-10-16',492.79,0,26,61,3,NULL,NULL,NULL),(96,'2012-10-16',492.79,0,39,62,3,NULL,NULL,NULL),(97,'2012-08-16',492.79,0,39,62,1,NULL,NULL,NULL),(98,'2012-11-16',492.79,0,39,62,4,NULL,NULL,NULL),(99,'2012-09-16',492.79,0,39,62,2,NULL,NULL,NULL),(100,'2012-08-16',492.79,0,26,63,1,NULL,NULL,NULL),(101,'2012-09-16',492.79,0,26,63,2,NULL,NULL,NULL),(102,'2012-10-16',492.79,0,26,63,3,NULL,NULL,NULL),(103,'2012-11-16',492.79,0,26,63,4,NULL,NULL,NULL),(104,'2012-08-16',492.79,0,37,64,1,NULL,NULL,NULL),(105,'2012-11-16',492.79,0,37,64,4,NULL,NULL,NULL),(106,'2012-10-16',492.79,0,37,64,3,NULL,NULL,NULL),(107,'2012-09-16',492.79,0,37,64,2,NULL,NULL,NULL),(108,'2012-09-16',492.79,0,39,65,2,NULL,NULL,NULL),(109,'2012-11-16',492.79,0,39,65,4,NULL,NULL,NULL),(110,'2012-08-16',492.79,0,39,65,1,NULL,NULL,NULL),(111,'2012-10-16',492.79,0,39,65,3,NULL,NULL,NULL),(112,'2012-09-16',20700.00,0,47,66,2,NULL,NULL,NULL),(113,'2012-08-16',20700.00,0,47,66,1,NULL,NULL,NULL),(114,'2012-12-16',205.45,0,37,67,5,NULL,NULL,NULL),(115,'2013-01-16',205.45,0,37,67,6,NULL,NULL,NULL),(116,'2013-02-16',205.45,0,37,67,7,NULL,NULL,NULL),(117,'2012-10-16',205.45,0,37,67,3,NULL,NULL,NULL),(118,'2012-08-16',205.45,0,37,67,1,NULL,NULL,NULL),(119,'2013-03-16',205.45,0,37,67,8,NULL,NULL,NULL),(120,'2012-09-16',205.45,0,37,67,2,NULL,NULL,NULL),(121,'2012-11-16',205.45,0,37,67,4,NULL,NULL,NULL),(122,'2012-08-16',293.50,0,38,68,1,NULL,NULL,NULL),(123,'2012-09-16',293.50,0,38,68,2,NULL,NULL,NULL),(124,'2012-10-16',293.50,0,38,68,3,NULL,NULL,NULL),(125,'2012-11-16',293.50,0,38,68,4,NULL,NULL,NULL),(126,'2012-12-16',293.50,0,38,68,5,NULL,NULL,NULL),(127,'2013-01-16',293.50,0,38,68,6,NULL,NULL,NULL),(128,'2013-02-16',293.50,0,38,68,7,NULL,NULL,NULL),(129,'2013-03-16',293.50,0,38,68,8,NULL,NULL,NULL),(130,'2012-08-18',888.36,0,50,69,1,NULL,NULL,NULL),(131,'2012-09-18',888.36,0,50,69,2,NULL,NULL,NULL),(132,'2012-08-21',854.99,0,53,74,1,NULL,NULL,NULL),(133,'2012-09-21',854.99,0,53,74,2,NULL,NULL,NULL),(134,'2012-10-21',854.99,0,53,74,3,NULL,NULL,NULL),(135,'2012-11-21',854.99,0,53,74,4,NULL,NULL,NULL),(136,'2012-12-21',854.99,0,53,74,5,NULL,NULL,NULL),(137,'2013-01-21',854.99,0,53,74,6,NULL,NULL,NULL);
/*!40000 ALTER TABLE `boleto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tipo_pagto`
--

DROP TABLE IF EXISTS `tipo_pagto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tipo_pagto` (
  `id` smallint(6) NOT NULL AUTO_INCREMENT,
  `descricao` varchar(45) NOT NULL,
  `gera_boleto` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `descricao_UNIQUE1122123` (`descricao`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tipo_pagto`
--

LOCK TABLES `tipo_pagto` WRITE;
/*!40000 ALTER TABLE `tipo_pagto` DISABLE KEYS */;
INSERT INTO `tipo_pagto` VALUES (1,'A VISTA',0),(2,'CARTÃO DÉBITO',0),(3,'CARTÃO CRÉDITO',0),(5,'PROMISSÓRIA',1),(11,'CHEQUE',1);
/*!40000 ALTER TABLE `tipo_pagto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tabela_financ`
--

DROP TABLE IF EXISTS `tabela_financ`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tabela_financ` (
  `id` smallint(6) NOT NULL AUTO_INCREMENT,
  `descricao` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `descricao_UNIQUE` (`descricao`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tabela_financ`
--

LOCK TABLES `tabela_financ` WRITE;
/*!40000 ALTER TABLE `tabela_financ` DISABLE KEYS */;
INSERT INTO `tabela_financ` VALUES (1,'PADRAO'),(11,'POR CHEQUE'),(2,'sem juros'),(3,'tabela 03 ALTERADA'),(4,'TABELA 04');
/*!40000 ALTER TABLE `tabela_financ` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pagto_recebido`
--

DROP TABLE IF EXISTS `pagto_recebido`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pagto_recebido` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `data` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `valor` decimal(7,2) NOT NULL,
  `receb_usuario_id` smallint(6) DEFAULT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '0',
  `data_informada` date NOT NULL,
  `valor_devido` decimal(7,2) DEFAULT NULL,
  `multa` decimal(7,2) DEFAULT NULL,
  `juros` decimal(7,2) DEFAULT NULL,
  `pedido_pag_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_pagto_recebido_usuario90351` (`receb_usuario_id`),
  KEY `JH6GJH456` (`receb_usuario_id`),
  KEY `pedido_pag_fk` (`pedido_pag_id`),
  CONSTRAINT `pedido_pag_fk` FOREIGN KEY (`pedido_pag_id`) REFERENCES `pedido_pag` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `JH6GJH456` FOREIGN KEY (`receb_usuario_id`) REFERENCES `usuario` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pagto_recebido`
--

LOCK TABLES `pagto_recebido` WRITE;
/*!40000 ALTER TABLE `pagto_recebido` DISABLE KEYS */;
/*!40000 ALTER TABLE `pagto_recebido` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cliente`
--

DROP TABLE IF EXISTS `cliente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cliente` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nome` varchar(100) NOT NULL,
  `tel_com` varchar(15) DEFAULT NULL,
  `tel_res` varchar(15) DEFAULT NULL,
  `tel_cel` varchar(15) DEFAULT NULL,
  `rg` varchar(20) DEFAULT NULL,
  `cpf` varchar(20) DEFAULT NULL,
  `end_rua` varchar(65) DEFAULT NULL,
  `end_num` varchar(10) DEFAULT NULL,
  `end_compl` varchar(15) DEFAULT NULL,
  `end_bairro` varchar(45) DEFAULT NULL,
  `end_municipio` varchar(45) DEFAULT NULL,
  `end_cep` varchar(15) DEFAULT NULL,
  `obs` text,
  PRIMARY KEY (`id`),
  UNIQUE KEY `nome_UNIQUE` (`nome`),
  UNIQUE KEY `rg_UNIQUE` (`rg`),
  UNIQUE KEY `cpf_UNIQUE` (`cpf`)
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cliente`
--

LOCK TABLES `cliente` WRITE;
/*!40000 ALTER TABLE `cliente` DISABLE KEYS */;
INSERT INTO `cliente` VALUES (1,'TESTE','','','',NULL,NULL,'','','','','',NULL,NULL),(26,'EDUARDO Q DE ALMEIDA','','','','34354565434','4564765464','','','','','',NULL,NULL),(35,'ERUYTWUEYTR GHSDGFKJH',NULL,'','',NULL,'2547623584',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(37,'ALBERTO',NULL,'765657865','54354354','3757865','43564354','RUA JSGFJHGSDF JDSGJH','22',NULL,NULL,NULL,NULL,NULL),(38,'EDUARDO QUEIROZ DE ALMEIDA','','123456','654321','657657361','75327615243','RUA OLHO DAGUA DO BORGES','184','apto 42 BL','VILA SILVIA','SAO PAULO',NULL,NULL),(39,'TESTE NOVO',NULL,'654654','353465467','754343','343543537','','','',NULL,NULL,NULL,NULL),(41,'NOVO CLIENTE JASGJFHA',NULL,'',NULL,'354365435456','',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(43,'TESTE DE NOVO',NULL,'',NULL,'8465765354354','987597857687657',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(44,'RTUYTORQWEUY YTUYTUO',NULL,'',NULL,'798789-96855465','65456354354',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(47,'joão ferreira',NULL,'',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(50,'EDUARDO ALMEIDA 2',NULL,'','',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(52,'EDUARDO QUEIROZ ALMEIDA','546546','5612476512765','5456547','451826531276','12345689876','RUA OLHO DAGUA DO BORGES','184','AP42 BL01','VILA SILVIA','SÃO PAULO',NULL,NULL),(53,'REGINALDO DA SILVA XAVIER','','243254325','153435754654','123456789088','1234567890987','RUA JOAQUIM CARLOS','1200','','vila MARIA','SÃO PAULO',NULL,NULL);
/*!40000 ALTER TABLE `cliente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pedido_produtos`
--

DROP TABLE IF EXISTS `pedido_produtos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pedido_produtos` (
  `produto_id` int(11) NOT NULL,
  `pedido_id` int(11) NOT NULL,
  `valor` decimal(7,2) NOT NULL,
  `qtd` smallint(6) NOT NULL DEFAULT '1',
  `desconto` double DEFAULT '0',
  PRIMARY KEY (`produto_id`,`pedido_id`),
  KEY `fk_produto_has_pedido_pedido11321` (`pedido_id`),
  KEY `fk_produto_has_pedido_produto1231235` (`produto_id`),
  KEY `76ASD5F` (`produto_id`),
  KEY `B2345J` (`pedido_id`),
  CONSTRAINT `76ASD5F` FOREIGN KEY (`produto_id`) REFERENCES `produto` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `B2345J` FOREIGN KEY (`pedido_id`) REFERENCES `pedido` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pedido_produtos`
--

LOCK TABLES `pedido_produtos` WRITE;
/*!40000 ALTER TABLE `pedido_produtos` DISABLE KEYS */;
INSERT INTO `pedido_produtos` VALUES (1,2,200.00,1,0),(1,10,200.00,1,5),(1,23,200.00,1,NULL),(1,43,200.00,1,NULL),(1,46,200.00,1,NULL),(5,26,20000.00,1,NULL),(5,44,20000.00,1,NULL),(5,52,20000.00,1,NULL),(6,13,1400.00,1,NULL),(6,14,1400.00,1,0),(6,18,1400.00,1,NULL),(6,21,1400.00,1,NULL),(6,24,1400.00,1,NULL),(6,45,1400.00,1,NULL),(6,47,1400.00,1,NULL),(6,48,1400.00,1,NULL),(6,49,1400.00,1,NULL),(6,51,1400.00,1,NULL),(6,52,1400.00,1,NULL),(6,53,1400.00,1,NULL),(6,63,1400.00,1,NULL),(6,64,1400.00,1,NULL),(6,65,1400.00,1,NULL),(6,66,1400.00,1,NULL),(6,67,1400.00,1,NULL),(6,69,1400.00,1,NULL),(6,71,1400.00,1,NULL),(6,73,1400.00,1,NULL),(7,50,25000.00,1,NULL),(8,12,10000.00,1,NULL),(8,15,10000.00,1,NULL),(8,17,10000.00,1,NULL),(8,51,10000.00,1,NULL),(9,18,2000.00,1,10),(9,70,2000.00,1,NULL),(10,72,20.00,1,NULL),(13,11,20000.00,1,NULL),(13,13,20000.00,1,NULL),(13,21,20000.00,1,NULL),(13,22,20000.00,1,NULL),(13,24,20000.00,1,NULL),(13,25,20000.00,1,NULL),(13,30,20000.00,1,NULL),(20,74,34.44,1,0),(33,68,1000.00,1,10),(42,75,5000.00,1,NULL);
/*!40000 ALTER TABLE `pedido_produtos` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2012-07-24  0:59:21
