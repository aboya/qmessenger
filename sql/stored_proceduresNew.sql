-- Generated by SQL Maestro for MySQL. Release date 14.04.2009
-- 20.04.2010 20:51:10
-- ----------------------------------
-- Alias: qmessenger at 127.0.0.1
-- Database name: qmessenger
-- Host: 127.0.0.1
-- Port number: 3306
-- User name: root
-- Server: 5.1.41-community
-- Session ID: 1
-- Character set: utf8
-- Collation: utf8_general_ci


CREATE DATABASE `qmessenger`
  CHARACTER SET `utf8`
  COLLATE `utf8_general_ci`;

USE `qmessenger`;

/* Tables */
CREATE TABLE `tnu_tree` (
  ID      int NOT NULL,
  `Name`  varchar(30) CHARACTER SET `utf8` COLLATE `utf8_general_ci` NOT NULL,
  PRIMARY KEY (ID)
) ENGINE = InnoDB;

CREATE TABLE `user` (
  `UserID`      bigint(50) AUTO_INCREMENT NOT NULL PRIMARY KEY,
  `UserIP`      varchar(50) CHARACTER SET `utf8` COLLATE `utf8_general_ci` NOT NULL,
  `TreeID`      int NOT NULL,
  `UserName`    varchar(50) CHARACTER SET `utf8` COLLATE `utf8_general_ci`,
  `FirstName`   varchar(50),
  `LastName`    varchar(50),
  `Phone`       varchar(20),
  `Mobile`      varchar(20),
  `MiddleName`  varchar(50),
  `building`    varchar(20),
  `apartament`  varchar(20)
) ENGINE = InnoDB;

CREATE TABLE `userfiles` (
  ID            bigint AUTO_INCREMENT NOT NULL,
  `Path`        text NOT NULL,
  `dateAdded`   datetime,
  `isDeliver`   bit NOT NULL DEFAULT 0,
  `fromUserID`  int NOT NULL,
  `toUserID`    int NOT NULL,
  `FileName`    text NOT NULL,
  `checkSum`    varchar(20) NOT NULL,
  PRIMARY KEY (ID)
) ENGINE = InnoDB;

CREATE TABLE `usermessages` (
  ID            bigint(50) AUTO_INCREMENT NOT NULL,
  `UserID`      bigint(50) NOT NULL,
  `message`     text NOT NULL,
  `isDeliver`   bit DEFAULT 0,
  `FromUserID`  int NOT NULL,
  `dateAdded`   datetime,
  PRIMARY KEY (ID)
) ENGINE = InnoDB;

/* Procedures */
DELIMITER |

CREATE DEFINER = 'root'@'localhost' PROCEDURE `AddUser` 
(
  IN  `UserIp`      varchar(50),
  IN  `treeid`      integer,
  IN  `firstname`   varchar(20),
  IN  `lastname`    varchar(20),
  IN  `phone`       varchar(20),
  IN  `mobile`      varchar(20),
  IN  `apparts`     varchar(20),
  IN  `building`    varchar(20),
  IN  `middleName`  varchar(50)
)
BEGIN

if( (select COUNT(`user`.`UserIP`) from user WHERE user.`UserIP` = UserIp) = 0)
then 

  SET autocommit=0;
  start TRANSACTION;
  insert into user (user.UserIP, 
  				 	user.TreeID, 
  					user.`FirstName`, 
                    user.`LastName`, 
                    user.`Phone`, 
                    user.`Mobile`,
                    user.`apartament`,
                    user.`building`,
                    `user`.`MiddleName`
                     ) 
  				   values(UserIp, 
                   			treeid, 
                            firstname, 
                            lastname, 
                            phone, 
                            mobile,
                            apparts,
                            building,
                            middleName
                            );
  commit;
  end if;

END|

DELIMITER ;

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

DELIMITER |

CREATE DEFINER = 'root'@'localhost' PROCEDURE `getFilePathByID` 
(
  IN `ids` bigint
)
BEGIN
   select * from userfiles where id = ids;

END|

DELIMITER ;

DELIMITER |

CREATE DEFINER = 'root'@'localhost' PROCEDURE `GetFilesFor` 
(
  IN `UserIp` varchar(20)
)
BEGIN
  select * from userfiles join user 
  on user.UserID = userfiles.toUserID
  and user.UserIP = UserIp
  and isDeliver = 0;
END|

DELIMITER ;

DELIMITER |

CREATE DEFINER = 'root'@'localhost' PROCEDURE `GetMessagesForUser` 
(
  IN `UserIP` varchar(50)
)
BEGIN
 declare usrid bigint;

 if( UserIp is not null and LENGTH(UserIp) > 0 ) then
    select usermessages.message,
             tnu_tree.Name as TreeName
       from usermessages inner join user 
    		on user.UserID = usermessages.UserID
       	    and user.UserIP = UserIp
        	and usermessages.isDeliver = 0
            inner join tnu_tree on tnu_tree.ID = user.TreeID;
       
       update usermessages inner join user on user.UserID = usermessages.UserID
       set usermessages.isDeliver = 1
       where user.userIp = UserIp ;
 end if;
 

END|

DELIMITER ;

DELIMITER |

CREATE DEFINER = 'root'@'localhost' PROCEDURE `getUserInfo` 
(
  IN `userip` varchar(50)
)
BEGIN

   select tnu_tree.Name as TreeName, 
          tnu_tree.ID as TreeID ,
          user.`FirstName` ,
          user.`LastName`,
          user.`UserID`,
          user.`apartament` as Apartament,
          user.`building` as Building,
          user.`MiddleName` as MiddleName 
          
   from user inner join tnu_tree
   		on tnu_tree.ID = user.TreeID
   		and user.UserIP = userip;

END|

DELIMITER ;

DELIMITER |

CREATE DEFINER = 'root'@'localhost' PROCEDURE `getUsersByTreeId` 
(
  IN `treeid` integer
)
BEGIN
	select 
    	  tnu_tree.Name as 	TreeName,
          user.FirstName,
          user.LastName,
          user.UserID,
          user.TreeID,
          user.apartament as Apartament,
          user.building as Building,
          user.MiddleName as MiddleName 
     from user join `tnu_tree` 
     on user.`TreeID` = `tnu_tree`.`ID`
     where user.TreeID = treeid;

END|

DELIMITER ;

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

DELIMITER |

CREATE DEFINER = 'root'@'localhost' PROCEDURE `SendFile` 
(
  IN  `filepath`    text,
  IN  `fname`       text,
  IN  `fromuserip`  varchar(20),
  IN  `touserid`    integer,
  IN  `checkSum`    varchar(20)
)
BEGIN
   declare fromuserId int;
   select user.UserID into fromuserId from user where user.UserIP = fromuserip;
   insert into userfiles (Path, 
   						dateAdded, 
                        isDeliver, 
                         userfiles.FileName, 
                         userfiles.fromUserID, 
                         userfiles.toUserID, 
                         userfiles.checkSum)
   			values(
   				filepath, 
                NOW(),
                0, 
                fname, 
                fromuserId, 
                touserid, 
                checkSum);
END|

DELIMITER ;

DELIMITER |

CREATE DEFINER = 'root'@'localhost' PROCEDURE `SendMessageToUser` 
(
  IN  `UserID`      integer,
  IN  `txtMess`     text CHARACTER SET `utf8`,
  IN  `FromUserID`  integer
)
BEGIN
   if(UserID is not null) then 
     SET autocommit=0;
 	 start TRANSACTION;
  	 insert into usermessages (userid, message, isDeliver, dateAdded, FromUserID)
    					values(userid_, txtMess, 0, NOW(), FromUserID);
      commit; 
   end if;
END|

DELIMITER ;

DELIMITER |

CREATE DEFINER = 'root'@'localhost' PROCEDURE `SetIsDeliverFor` 
(
  IN `UserId` integer
)
BEGIN
  update userfiles
  set userfiles.isDeliver = true
  where userfiles.toUserID = UserId;

END|

DELIMITER ;

/* Functions */
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

CREATE INDEX `TreeID`
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
  (`fromUserID`);

CREATE INDEX `Path`
  ON `userfiles`
  (`Path`(1));

CREATE INDEX `toTreeID`
  ON `userfiles`
  (`toUserID`);

CREATE UNIQUE INDEX ID
  ON `usermessages`
  (ID);

CREATE INDEX `UserID`
  ON `usermessages`
  (`UserID`);