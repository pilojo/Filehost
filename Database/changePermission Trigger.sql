Delimiter #
CREATE TRIGGER checkPermissions AFTER UPDATE on folders
FOR EACH ROW
BEGIN
	DECLARE parent_permission VARCHAR(16);
    DECLARE currentPermission VARCHAR(16);
    INSERT INTO parent_permission SELECT permissions.Name FROM permissions INNER JOIN folders ON folders.Permission_ID = permissions.ID WHERE folders.ID = NEW.ParentFolder_ID; 
	
    
    IF parent_permission = 'private' THEN
		SET NEW.Permission_ID = (SELECT ID FROM permissions WHERE Name = parent_permission);
	ELSEIF parent_permission = 'shared' THEN
		INSERT INTO current_permission SELECT permission.Name FROM permissions INNER JOIN folders on folders.Permission_ID = permissions.ID WHERE folders.ID = NEW.ID;
        IF current_permission = 'public' THEN 
			SET NEW.Permission_ID = (SELECT ID FROM permissions WHERE Name = 'shared');
		END IF;
	END IF;
    
END#