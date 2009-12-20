CREATE DEFINER = 'root'@'localhost' PROCEDURE `AddUser`(
        IN UserIp VARCHAR(50),
        IN treeid INTEGER(11)
    )
    NOT DETERMINISTIC
    CONTAINS SQL
    SQL SECURITY DEFINER
    COMMENT ''
BEGIN

if( (select COUNT(`user`.`UserIP`) from user WHERE user.`UserIP` = UserIp) = 0)
then 
  insert into user (User.UserIP, user.TreeID ) 
  				   values(UserIp, treeid);
  end if;

END;

--------------------------------------------------------

CREATE DEFINER = 'root'@'localhost' PROCEDURE `CheckTreeID`(
        id INT,
        name VARCHAR(50)
    )
    NOT DETERMINISTIC
    CONTAINS SQL
    SQL SECURITY DEFINER
    COMMENT ''
BEGIN
  declare nname varchar(50);
  select tnu_tree.Name into nname from tnu_tree
  where tnu_tree.ID = id;
  
  
END;

---------------------------------------------------------
CREATE DEFINER = 'root'@'localhost' FUNCTION `AddNewNode`(
        id INTEGER(11),
        name varchar(50)
    )
    RETURNS varchar(50) CHARSET latin1
    DETERMINISTIC
    CONTAINS SQL
    SQL SECURITY DEFINER
    COMMENT ''
BEGIN
  declare nname varchar(50);
  declare iid int;
  set nname = null;
  select tnu_tree.Name into nname from tnu_tree
  where tnu_tree.ID = id;
  IF(nname is null) then
  	insert into tnu_tree values(id,name);
    return '';
  end if; 
  return nname;

END;
---------------------------------------------------------
CREATE DEFINER = 'root'@'localhost' FUNCTION `FindUser`(
        Ip varchar(50)
    )
    RETURNS tinyint(4)
    NOT DETERMINISTIC
    CONTAINS SQL
    SQL SECURITY DEFINER
    COMMENT ''
BEGIN
 return (select COUNT(`user`.`UserIP`) 
 		from user WHERE user.`UserIP` = UserIp);
END;