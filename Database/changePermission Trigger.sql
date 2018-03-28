DROP TRIGGER IF EXISTS checkPermissions;
Delimiter #
CREATE TRIGGER checkPermissions before UPDATE on folders
FOR EACH ROW
BEGIN
	DECLARE parent_permission VARCHAR(16);
    DECLARE current_Permission VARCHAR(16);
    SET parent_permission  = (SELECT permissions.Name FROM permissions INNER JOIN folders ON folders.Permission_ID = permissions.ID WHERE folders.ID = NEW.ParentFolder_ID); 
	
    
    IF parent_permission = 'private' THEN
		SET NEW.Permission_ID = (SELECT ID FROM permissions WHERE Name = parent_permission);
	ELSEIF parent_permission = 'shared' THEN
		SET current_permission  = (SELECT permission.Name FROM permissions INNER JOIN folders on folders.Permission_ID = permissions.ID WHERE folders.ID = NEW.ID);
        IF current_permission = 'public' THEN 
			SET new.Permission_ID = (SELECT ID FROM permissions WHERE Name = 'shared');
		END IF;
	END IF;
    set new.lastModified = now();
    
END#