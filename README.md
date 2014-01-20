System Environment
====================
  
    1.jdk1.7.0_45
  
    2.netbean 7.3.1
    
    3.mysql 5.1.23
    

Database you need to create
=====================

    sql:
        CREATE DATABASE javafx_update;
        CREATE TABLE `versions` (`id` int(11) NOT NULL auto_increment,
                                 `version` varchar(255) default NULL,
                                 `datetime` varchar(255) default NULL,
                                 `path` varchar(255) default NULL,
                                  PRIMARY KEY  (`id`));
        INSERT INTO `versions` VALUES ('2', '1.0', '2013-12-16 17:10:49', '');
        INSERT INTO `versions` VALUES ('3', '1.1', '2013-12-16 17:11:17', 'update/1.1.zip');
    
