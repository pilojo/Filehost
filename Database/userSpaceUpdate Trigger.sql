DROP TRIGGER IF EXISTS spaceUsageUpdate;
Delimiter #
CREATE TRIGGER spaceUsageUpdate AFTER INSERT ON files
FOR EACH ROW
BEGIN
	DECLARE ownerID BIGINT;
    
    SET ownerID = (SELECT users.ID FROM files INNER JOIN folders ON folders.ID = ParentFolder_ID INNER JOIN users ON users.ID = Owner_ID WHERE files.ID = NEW.ID);
    
    UPDATE users SET storageUsage_Bytes = storageUsage_Bytes + NEW.byteSize WHERE users.ID = ownerID;
END#