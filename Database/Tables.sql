

-- -----------------------------------------------------
-- Table `mydb`.`Folders`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Folders` (
  `ID` BIGINT NOT NULL AUTO_INCREMENT,
  `Name` VARCHAR(32) NOT NULL,
  `Parent_Path` TEXT NOT NULL,
  `Owner_ID` BIGINT NOT NULL,
  `Permission_ID` BIGINT NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE INDEX `ID_UNIQUE` (`ID` ASC),
  INDEX `fk_Folders_Users1_idx` (`Owner_ID` ASC),
  INDEX `fk_Folders_Permissions1_idx` (`Permission_ID` ASC),
  CONSTRAINT `fk_Folders_Permissions1`
    FOREIGN KEY (`Permission_ID`)
    REFERENCES `Permissions` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);


-- -----------------------------------------------------
-- Table `mydb`.`Users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Users` (
  `ID` BIGINT NOT NULL AUTO_INCREMENT,
  `Username` VARCHAR(32) NULL,
  `First_Name` VARCHAR(32) NOT NULL,
  `Last_Name` VARCHAR(150) NOT NULL,
  `Email` VARCHAR(150) NOT NULL,
  `AccountType_ID` BIGINT NOT NULL,
  `RootFolder_ID` BIGINT NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE INDEX `ID_UNIQUE` (`ID` ASC),
  UNIQUE INDEX `Username_UNIQUE` (`Username` ASC),
  UNIQUE INDEX `Email_UNIQUE` (`Email` ASC),
  INDEX `fk_Users_AccountTypes_idx` (`AccountType_ID` ASC),
  INDEX `fk_Users_Folders1_idx` (`RootFolder_ID` ASC),
  CONSTRAINT `fk_Users_AccountTypes`
    FOREIGN KEY (`AccountType_ID`)
    REFERENCES `AccountTypes` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Users_Folders1`
    FOREIGN KEY (`RootFolder_ID`)
    REFERENCES `Folders` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);


-- -----------------------------------------------------
-- Table `mydb`.`Files`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Files` (
  `ID` BIGINT NOT NULL AUTO_INCREMENT,
  `Name` VARCHAR(32) NOT NULL,
  `Permissions_ID` BIGINT NOT NULL,
  `ParentFolder_ID` BIGINT NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE INDEX `ID_UNIQUE` (`ID` ASC),
  INDEX `fk_Files_Permissions1_idx` (`Permissions_ID` ASC),
  INDEX `fk_Files_Folders1_idx` (`ParentFolder_ID` ASC),
  CONSTRAINT `fk_Files_Permissions1`
    FOREIGN KEY (`Permissions_ID`)
    REFERENCES `Permissions` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Files_Folders1`
    FOREIGN KEY (`ParentFolder_ID`)
    REFERENCES `Folders` (`ID`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION
);


-- -----------------------------------------------------
-- Table `mydb`.`SharedFolders`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SharedFolders` (
  `Folder_ID` BIGINT NOT NULL,
  `User_ID` BIGINT NOT NULL,
  PRIMARY KEY (`Folder_ID`, `User_ID`),
  INDEX `fk_SharedFolders_Users1_idx` (`User_ID` ASC),
  CONSTRAINT `fk_SharedFolders_Folders1`
    FOREIGN KEY (`Folder_ID`)
    REFERENCES `Folders` (`ID`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_SharedFolders_Users1`
    FOREIGN KEY (`User_ID`)
    REFERENCES `Users` (`ID`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION
);


-- -----------------------------------------------------
-- Table `mydb`.`SharedFiles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SharedFiles` (
  `Folder_ID` BIGINT NOT NULL,
  `User_ID` BIGINT NOT NULL,
  PRIMARY KEY (`Folder_ID`, `User_ID`),
  INDEX `fk_SharedFiles_Users1_idx` (`User_ID` ASC),
  CONSTRAINT `fk_SharedFiles_Folders1`
    FOREIGN KEY (`Folder_ID`)
    REFERENCES `Folders` (`ID`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_SharedFiles_Users1`
    FOREIGN KEY (`User_ID`)
    REFERENCES `Users` (`ID`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION
);
create table tomcat_sessions (
  session_id     varchar(100) not null primary key,
  valid_session  char(1) not null,
  max_inactive   int not null,
  last_access    bigint not null,
  app_name       varchar(255),
  session_data   mediumblob,
  KEY kapp_name(app_name)
);

ALTER TABLE `test`.`folders` 
DROP FOREIGN KEY `fk_Folders_Permissions1`;
ALTER TABLE `test`.`folders` 
ADD INDEX `fk_Folders_Permissions1_idx` (`Permission_ID` ASC),
DROP INDEX `fk_Folders_Permissions1_idx` ;
ALTER TABLE `test`.`folders` 
ADD CONSTRAINT `fk_Folders_Permissions1`
  FOREIGN KEY (`Permission_ID`)
  REFERENCES `test`.`permissions` (`ID`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_user_id`
  FOREIGN KEY (`Owner_ID`)
  REFERENCES `test`.`users` (`ID`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;
