Drop trigger if exists filePermission;
delimiter #
Create trigger filePermission before update on files
for each row
begin
	Declare parentPermission varchar(32);
    declare currentPermission varchar(32);
    
    set parentPermission = (Select permissions.Name from permissions inner join folders on folders.Permission_ID = permissions.ID where folders.ID = NEW.ParentFolder_ID);
    set currentPermission = (select Name from permissions where ID = new.Permissions_ID);
    
    IF parentPermission = "private" THEN
		set new.Permissions_ID = (Select ID from permissions where Name = "private");
	elseif parentPermission = "shared" then
		if currentPermission = "public" then
			set new.Permissions_ID = (Select ID from permissions where Name = "shared");
		end if;
	end if;
    set new.lastModified = now();
end#