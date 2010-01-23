-- Generated by SQL Maestro for MySQL. Release date 14.04.2009
-- 17.01.2010 15:37:57
-- ----------------------------------
-- Alias: qmessenger at 127.0.0.1
-- Database name: qmessenger
-- Host: 127.0.0.1
-- Port number: 3306
-- User name: root
-- Server: 5.1.41-community
-- Session ID: 36
-- Character set: utf8
-- Collation: utf8_general_ci


DROP DATABASE IF EXISTS `qMessenger`;

CREATE DATABASE `qMessenger`
  CHARACTER SET `utf8`
  COLLATE `utf8_general_ci`;

USE `qMessenger`;

/* Tables */
DROP TABLE IF EXISTS `tnu_tree`;

CREATE TABLE `tnu_tree` (
  ID      int NOT NULL,
  `Name`  varchar(30) CHARACTER SET `utf8` COLLATE `utf8_general_ci` NOT NULL,
  PRIMARY KEY (ID)
) ENGINE = InnoDB;

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `UserID`    bigint(50) AUTO_INCREMENT NOT NULL,
  `UserIP`    varchar(50) CHARACTER SET `utf8` COLLATE `utf8_general_ci` NOT NULL,
  `TreeID`    int NOT NULL,
  `UserName`  varchar(50) CHARACTER SET `utf8` COLLATE `utf8_general_ci`,
  PRIMARY KEY (`UserID`)
) ENGINE = InnoDB;

DROP TABLE IF EXISTS `userfiles`;

CREATE TABLE `userfiles` (
  ID            bigint AUTO_INCREMENT NOT NULL,
  `Path`        text NOT NULL,
  `dateAdded`   datetime,
  `isDeliver`   bit NOT NULL DEFAULT 0,
  `fromTreeID`  int NOT NULL,
  `toTreeID`    int NOT NULL,
  `FileName`    text NOT NULL,
  `checkSum`    varchar(20) NOT NULL,
  PRIMARY KEY (ID)
) ENGINE = InnoDB;

DROP TABLE IF EXISTS `usermessages`;

CREATE TABLE `usermessages` (
  ID                bigint(50) AUTO_INCREMENT NOT NULL,
  `UserID`          bigint(50) NOT NULL,
  `message`         text NOT NULL,
  `isDeliver`       bit DEFAULT 0,
  `FromUserTreeID`  int NOT NULL,
  `dateAdded`       datetime,
  PRIMARY KEY (ID)
) ENGINE = InnoDB;

/* Procedures */
DROP PROCEDURE IF EXISTS `AddUser`;

DELIMITER |

CREATE DEFINER = 'root'@'localhost' PROCEDURE `AddUser` 
(
  IN  `UserIp`  varchar(50),
  IN  `treeid`  integer
)
BEGIN

if( (select COUNT(`user`.`UserIP`) from user WHERE user.`UserIP` = UserIp) = 0)
then 

  SET autocommit=0;
  start TRANSACTION;
  insert into user (user.UserIP, user.TreeID ) 
  				   values(UserIp, treeid);
  commit;
  end if;

END|

DELIMITER ;

DROP PROCEDURE IF EXISTS `CheckTreeID`;

DELIMITER |

CREATE DEFINER = 'root'@'localhost' PROCEDURE `CheckTreeID` 
(
  IN  `id`    int,
  IN  `name`  varchar(50)
)
BEGIN
  declare nname varchar(50);
  select tnu_tree.Name into nname from tnu_tree
  where tnu_tree.ID = id;
  
  
END|

DELIMITER ;

DROP PROCEDURE IF EXISTS `getFilePathByID`;

DELIMITER |

CREATE DEFINER = 'root'@'localhost' PROCEDURE `getFilePathByID` 
(
  IN `ids` bigint
)
BEGIN
   select Path,FileName from userfiles where id = ids;

END|

DELIMITER ;

DROP PROCEDURE IF EXISTS `GetFilesFor`;

DELIMITER |

CREATE DEFINER = 'root'@'localhost' PROCEDURE `GetFilesFor` 
(
  IN `TreeID` integer
)
BEGIN
  select * from userfiles where userfiles.toTreeID = TreeID
  and isDeliver = 0;
END|

DELIMITER ;

DROP PROCEDURE IF EXISTS `GetMessagesForUser`;

DELIMITER |

CREATE DEFINER = 'root'@'localhost' PROCEDURE `GetMessagesForUser` 
(
  IN  `UserIP`  varchar(50),
  IN  `TreeID`  integer
)
BEGIN
 declare usrid bigint;

 if( UserIp is not null and LENGTH(UserIp) > 0 ) then
    select usermessages.message,
    		 usermessages.FromUserTreeID, 
             tnu_tree.Name as TreeName
       from usermessages inner join user 
    		on user.UserID = usermessages.UserID
       	    and user.UserIP = UserIp
        	and usermessages.isDeliver = 0
            inner join tnu_tree on tnu_tree.ID = usermessages.FromUserTreeID;
       
       update usermessages inner join user on user.UserID = usermessages.UserID
       set usermessages.isDeliver = 1
       where user.userIp = UserIp ;
       
 elseif( TreeID is not null ) then
    select usermessages.message,
       usermessages.FromUserTreeID,
       tnu_tree.Name  
    	from usermessages inner join user 
    		on user.UserID = usermessages.UserID
        	and user.TreeID = TreeID
        	and usermessages.isDeliver = 0
            inner join tnu_tree on tnu_tree.ID = usermessages.FromUserTreeID;
               
       update usermessages inner join user on user.UserID = usermessages.UserID
       set usermessages.isDeliver = 1
       where user.TreeID = TreeID ;
 end if;
 

END|

DELIMITER ;

DROP PROCEDURE IF EXISTS `getUserTreeName`;

DELIMITER |

CREATE DEFINER = 'root'@'localhost' PROCEDURE `getUserTreeName` 
(
  IN `userip` varchar(50)
)
BEGIN

   select `tnu_tree`.`Name` as TreeName, `tnu_tree`.`ID` as TreeID 
   from user inner join `tnu_tree`
   		on `tnu_tree`.`ID` = user.`TreeID`
   		and `user`.`UserIP` = userip;

END|

DELIMITER ;

DROP PROCEDURE IF EXISTS `RemoveFileByID`;

DELIMITER |

CREATE DEFINER = 'root'@'localhost' PROCEDURE `RemoveFileByID` 
(
  IN `FileID` bigint
)
BEGIN
  declare fpath text; 
  update userfiles 
  set isDeliver = 1
  where id = FileId;
  
  /*
  finding remaing files
  if count > 0 returnin value than correspond
  server delete this file
  */
  select Path into fpath from userfiles 
  where Id = Fileid;
  
  select count(id) from userfiles
  where isdeliver = 0 and Path = fpath;
 
  
  
END|

DELIMITER ;

DROP PROCEDURE IF EXISTS `SendFile`;

DELIMITER |

CREATE DEFINER = 'root'@'localhost' PROCEDURE `SendFile` 
(
  IN  `filepath`    text,
  IN  `fname`       text,
  IN  `fromtreeid`  integer,
  IN  `totreeid`    integer,
  IN  `checkSum`    varchar(20)
)
BEGIN
   insert into userfiles (Path, dateAdded, isDeliver, 
                         userfiles.FileName, userfiles.fromTreeID, userfiles.toTreeID, `userfiles`.`checkSum`)
   values(filepath, NOW(),0, fname, fromtreeid, totreeid, checkSum);
END|

DELIMITER ;

DROP PROCEDURE IF EXISTS `SendMessageToUser`;

DELIMITER |

CREATE DEFINER = 'root'@'localhost' PROCEDURE `SendMessageToUser` 
(
  IN  `TreeID`   integer,
  IN  `txtMess`  text
)
BEGIN

 declare userid_ bigint; 
 if(TreeID is not null and 
 	 txtMess is not null and 
      length(txtMess) > 0) then
   select User.UserID into userid_ from user
   where user.TreeID = TreeID;
   if(userid_ is not null) then 
     SET autocommit=0;
 	 start TRANSACTION;
  	 insert into usermessages (userid, message, isDeliver, dateAdded, FromUserTreeID)
    					values(userid_, txtMess, 0, NOW(), FromUserTreeID_in);
      commit; 
   end if;
  end if;


END|

DELIMITER ;

DROP PROCEDURE IF EXISTS `SetIsDeliverFor`;

DELIMITER |

CREATE DEFINER = 'root'@'localhost' PROCEDURE `SetIsDeliverFor` 
(
  IN `TreeID` integer
)
BEGIN
  update userfiles
  set userfiles.isDeliver = true
  where userfiles.toTreeID = TreeID;

END|

DELIMITER ;

/* Functions */
DROP FUNCTION IF EXISTS `AddNewNode`;

DELIMITER |

CREATE DEFINER = 'root'@'localhost' FUNCTION `AddNewNode` 
(
  `id`    integer,
  `name`  varchar(50)
)
RETURNS varchar(50) charset latin1
  DETERMINISTIC
BEGIN
  declare nname varchar(50);
  declare iid int;
  set nname = null;
  select tnu_tree.Name into nname from tnu_tree
  where tnu_tree.ID = id;
  IF(nname is null) then
  	 insert into tnu_tree values(id,name);
     return '';
  elseif( nname != name ) then 
     update tnu_tree set `tnu_tree`.`Name` = name
     where `tnu_tree`.`ID` = id;
     return '';  
  end if; 
  return nname;

END|

DELIMITER ;

DROP FUNCTION IF EXISTS `FindUser`;

DELIMITER |

CREATE DEFINER = 'root'@'localhost' FUNCTION `FindUser` 
(
  `Ip` varchar(50)
)
RETURNS tinyint(4)
BEGIN
 return (select COUNT(`user`.`UserIP`) 
 		from user WHERE user.`UserIP` = Ip);
END|

DELIMITER ;

/* Indexes */
CREATE UNIQUE INDEX ID
  ON `tnu_tree`
  (ID);

CREATE UNIQUE INDEX `TreeID`
  ON `user`
  (`TreeID`);

CREATE UNIQUE INDEX `UserID`
  ON `user`
  (`UserID`);

CREATE UNIQUE INDEX `UserIP`
  ON `user`
  (`UserIP`);

CREATE UNIQUE INDEX `UserIP_2`
  ON `user`
  (`UserIP`);

CREATE INDEX `fromTreeID`
  ON `userfiles`
  (`fromTreeID`);

CREATE INDEX `Path`
  ON `userfiles`
  (`Path`(1));

CREATE INDEX `toTreeID`
  ON `userfiles`
  (`toTreeID`);

CREATE UNIQUE INDEX ID
  ON `usermessages`
  (ID);

CREATE INDEX `UserID`
  ON `usermessages`
  (`UserID`);
