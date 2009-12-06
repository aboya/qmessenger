CREATE DEFINER = 'root'@'localhost' FUNCTION `AddNewNode`(
        id INTEGER(11),
        name varchar(50)
    )
    RETURNS varchar(50) CHARSET latin1
    NOT DETERMINISTIC
    CONTAINS SQL
    SQL SECURITY DEFINER
    COMMENT ''
BEGIN
  declare nname varchar(50);
  set nname = null;
  select tnu_tree.Name into nname from tnu_tree
  where tnu_tree.ID = id;
  IF(nname is null) then
  	insert into tnu_tree values(id,name);
    return '';
  end if; 
  return nname;

END;