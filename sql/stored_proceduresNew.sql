-- Generated by SQL Maestro for MySQL. Release date 14.04.2009
-- 18.05.2010 22:56:27
-- ----------------------------------
-- Alias: qmessenger at 192.168.2.2
-- Database name: qmessenger
-- Host: 192.168.2.2
-- Port number: 3306
-- User name: root
-- Server: 5.1.41-community
-- Session ID: 1
-- Character set: utf8
-- Collation: utf8_general_ci


DROP DATABASE IF EXISTS `qmessenger`;

CREATE DATABASE `qmessenger`
  CHARACTER SET `utf8`
  COLLATE `utf8_general_ci`;

USE `qmessenger`;

/* Tables */
DROP TABLE IF EXISTS `tnu_tree`;

CREATE TABLE `tnu_tree` (
  ID      int NOT NULL,
  `Name`  varchar(30) NOT NULL,
  PRIMARY KEY (ID)
) ENGINE = InnoDB;

DROP TABLE IF EXISTS `tree_edges`;

CREATE TABLE `tree_edges` (
  `parent`  int NOT NULL,
  `child`   int NOT NULL,
  PRIMARY KEY (`parent`, `child`)
) ENGINE = InnoDB;

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `UserID`      bigint(50) AUTO_INCREMENT NOT NULL,
  `UserIP`      varchar(50) NOT NULL,
  `TreeID`      int NOT NULL,
  `UserName`    varchar(50),
  `FirstName`   varchar(50),
  `LastName`    varchar(50),
  `Phone`       varchar(20),
  `Mobile`      varchar(20),
  `MiddleName`  varchar(50),
  `building`    varchar(20),
  `apartament`  varchar(20),
  PRIMARY KEY (`UserID`)
) ENGINE = InnoDB;

DROP TABLE IF EXISTS `userfiles`;

CREATE TABLE `userfiles` (
  ID            bigint AUTO_INCREMENT NOT NULL,
  `Path`        text NOT NULL,
  `dateAdded`   datetime,
  `isDeliver`   bit NOT NULL DEFAULT 'b''0''',
  `fromUserID`  int NOT NULL,
  `toUserID`    int NOT NULL,
  `FileName`    text NOT NULL,
  `checkSum`    varchar(20) NOT NULL,
  PRIMARY KEY (ID)
) ENGINE = InnoDB;

DROP TABLE IF EXISTS `usermessages`;

CREATE TABLE `usermessages` (
  ID            bigint(50) AUTO_INCREMENT NOT NULL,
  `UserID`      bigint(50) NOT NULL,
  `message`     text NOT NULL,
  `isDeliver`   bit DEFAULT 'b''0''',
  `FromUserID`  int NOT NULL,
  `dateAdded`   datetime,
  PRIMARY KEY (ID)
) ENGINE = InnoDB;

/* Procedures */
DROP PROCEDURE IF EXISTS `AddUser`;

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

DROP PROCEDURE IF EXISTS `add_edge`;

DELIMITER |

CREATE DEFINER = 'root'@'%' PROCEDURE `add_edge` 
(
  IN  `parent`  int,
  IN  `child`   int
)
BEGIN
   insert into tree_edges
   values(parent, child);
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
   select * from userfiles where id = ids;

END|

DELIMITER ;

DROP PROCEDURE IF EXISTS `GetFilesFor`;

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

DROP PROCEDURE IF EXISTS `GetMessagesForUser`;

DELIMITER |

CREATE DEFINER = 'root'@'localhost' PROCEDURE `GetMessagesForUser` 
(
  IN `UserIP` varchar(50)
)
BEGIN
 declare usrid bigint;

 if( UserIp is not null and LENGTH(UserIp) > 0 ) then
    select usermessages.message,
             tnu_tree.Name as TreeName,
             FromUser.`FirstName` as FirstName,
             FromUser.`LastName` as Lastname
       from usermessages inner join user 
    		on user.UserID = usermessages.UserID
       	    and user.UserIP = UserIp
        	and usermessages.isDeliver = 0
            inner join User as FromUser on
            FromUser.UserID = usermessages.FromUserID
            inner join tnu_tree on tnu_tree.ID = FromUser.TreeID;
       
       update usermessages inner join user on user.UserID = usermessages.UserID
       set usermessages.isDeliver = 1
       where user.userIp = UserIp ;
 end if;
 

END|

DELIMITER ;

DROP PROCEDURE IF EXISTS `getUserInfo`;

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

DROP PROCEDURE IF EXISTS `getUsersByTreeId`;

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

DROP PROCEDURE IF EXISTS `get_parent_path`;

DELIMITER |

CREATE DEFINER = 'root'@'%' PROCEDURE `get_parent_path` 
(
  IN `user_id` int
)
BEGIN
     declare tree_id int;
     declare tree_ids_path text;
     declare root_node_id int;
     declare current_node_id int;
     declare tree_names_path text;
     declare done INT DEFAULT 0;
     declare dbg text;

     
     -- find tree id
     select user.TreeID into current_node_id from user 
     where user.UserID = user_id;
      
     -- generating ids path
     set tree_ids_path = CONCAT(cast(current_node_id as char(8)) , cast(',' as char(1)));
     set tree_names_path = concat((select Name from tnu_tree where ID = current_node_id) , cast(',' as char(1)) );
     set done = (select count(parent) from tree_edges where child = current_node_id);
    -- set dbg = concat( done , cast(',' as char(1)) );
     while done <> 0 
     do
     
        set current_node_id = (select parent from tree_edges where child = current_node_id);
        set tree_names_path = concat(tree_names_path , (select Name from tnu_tree where ID = current_node_id), cast(',' as char(1)));
        set tree_ids_path = concat(tree_ids_path , cast(current_node_id as char(8)) , cast(',' as char(1)));
        set done = (select count(parent) from tree_edges where child = current_node_id);
       -- set dbg = concat(dbg, done , cast(',' as char(1)));
         
     
     end while;
     
     select substring(tree_ids_path from 1 for length(tree_ids_path) - 1) as TreeIDPath, 
            substring(tree_names_path from 1  for length(tree_names_path)-1)  as TreeNamesPath;
       --     dbg as DEBUG;
      
END|

DELIMITER ;

DROP PROCEDURE IF EXISTS `get_received_message_history`;

DELIMITER |

CREATE DEFINER = 'root'@'%' PROCEDURE `get_received_message_history` 
(
  IN  `user_id`           int,
  IN  `is_get_full_path`  bit
)
BEGIN
  if( is_get_full_path = 0) then
     
     select `usermessages`.`dateAdded`,
            `usermessages`.`message`,
            `user`.`FirstName`,
            user.`LastName`,
            user.`MiddleName`
      from usermessages
     inner join user on user.`UserID` = `usermessages`.`FromUserID`
     where usermessages.`UserID` = user_id
     and isDeliver = true;
  else 
     select `usermessages`.`dateAdded`,
            `usermessages`.`message`,
            `user`.`FirstName`,
            user.`LastName`,
            user.`MiddleName`,
            select_parent_path_names(FromUserID) as TreeNamesPath
     from usermessages 
     inner join user on user.`UserID` = `usermessages`.`FromUserID`
     where usermessages.`UserID` = user_id
     and isDeliver = true;
  end if;
     
END|

DELIMITER ;

DROP PROCEDURE IF EXISTS `get_sended_message_history`;

DELIMITER |

CREATE DEFINER = 'root'@'%' PROCEDURE `get_sended_message_history` 
(
  IN `user_id` int
)
BEGIN
     select `usermessages`.`dateAdded`,
            `usermessages`.`message`,
            `user`.`FirstName`,
            user.`LastName`,
            user.`MiddleName`
     from usermessages 
     inner join user on user.`UserID` = `usermessages`.`UserID`
     where usermessages.`FromUserID` = user_id
     and isDeliver = true;
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

DROP PROCEDURE IF EXISTS `save_message_on_server`;

DELIMITER |

CREATE DEFINER = 'root'@'%' PROCEDURE `save_message_on_server` 
(
  IN  `txt_message`   text,
  IN  `user_id`       int,
  IN  `from_user_id`  int
)
BEGIN
    insert into usermessages (userid, message, isDeliver, dateAdded, FromUserID)
   				values(user_id, txt_message , true, NOW(), from_user_id);
END|

DELIMITER ;

DROP PROCEDURE IF EXISTS `SendFile`;

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

DROP PROCEDURE IF EXISTS `SendMessageToUser`;

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
    					values(UserID, txtMess, 0, NOW(), FromUserID);
      commit; 
   end if;
END|

DELIMITER ;

DROP PROCEDURE IF EXISTS `SetIsDeliverFor`;

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

DROP FUNCTION IF EXISTS `select_parent_path_names`;

DELIMITER |

CREATE DEFINER = 'root'@'%' FUNCTION `select_parent_path_names` 
(
  `user_id` int
)
RETURNS text charset utf8
BEGIN
     declare tree_ids_path text;
     declare current_node_id int;
     declare tree_names_path text;
     declare done INT DEFAULT 0;
     
     -- find tree id
     select user.TreeID into current_node_id from user 
     where user.UserID = user_id;
      
     -- generating ids path
     set tree_names_path = concat((select Name from tnu_tree where ID = current_node_id) , cast(',' as char(1)) );
     set done = (select count(parent) from tree_edges where child = current_node_id);
    -- set dbg = concat( done , cast(',' as char(1)) );
     while done <> 0 
     do
        set current_node_id = (select parent from tree_edges where child = current_node_id);
        set tree_names_path = concat(tree_names_path , (select Name from tnu_tree where ID = current_node_id), cast(',' as char(1)));
        set done = (select count(parent) from tree_edges where child = current_node_id);
     end while;
     return substring(tree_names_path from 1  for length(tree_names_path)-1);
END|

DELIMITER ;

/* Indexes */
CREATE UNIQUE INDEX ID
  ON `tnu_tree`
  (ID);

CREATE INDEX `foreign_key_child`
  ON `tree_edges`
  (`child`);

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

/* Foreign Keys */
ALTER TABLE `tree_edges`
  ADD CONSTRAINT `foreign_key_child`
  FOREIGN KEY (`child`)
    REFERENCES `tnu_tree`(ID)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT;

ALTER TABLE `tree_edges`
  ADD CONSTRAINT `foreign_key_parent`
  FOREIGN KEY (`parent`)
    REFERENCES `tnu_tree`(ID)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT;