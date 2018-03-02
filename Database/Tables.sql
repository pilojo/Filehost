-- Author: Alejandra Acosta
-- -----------------------------------------------------
-- Table `AccountTypes`
-- Contains a list of all the different account types
-- that a user can have
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `AccountTypes` (
  `ID` BIGINT NOT NULL AUTO_INCREMENT,
  `Name` VARCHAR(16) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE INDEX `ID_UNIQUE` (`ID` ASC),
  UNIQUE INDEX `Name_UNIQUE` (`Name` ASC)
);



-- -----------------------------------------------------
-- Table `Permissions`
-- Stores all available permission that folders and files
-- can have
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Permissions` (
  `ID` BIGINT NOT NULL AUTO_INCREMENT,
  `Name` VARCHAR(16) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE INDEX `ID_UNIQUE` (`ID` ASC),
  UNIQUE INDEX `Name_UNIQUE` (`Name` ASC)
);


-- -----------------------------------------------------
-- Table `Folders`
-- Holds information about every folder that has been 
-- created, including root folders. The information 
-- includes the name, path of the folder as well as 
-- foreign keys that link to the users table to say
-- which user created the folder. As well as a foreign
-- to the permissions table to know what permissons
-- a folder has.
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
-- Table `Users`
-- Holds information about the users that have created an
-- acount. Includes their first name, last name, a unique
-- user name and a unique email address. It contains a 
-- foreign key to the folders table to say which folder
-- is the user's root folder and to the accountTypes table
-- to know the account type of the user's account.
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Users` (
  `ID` BIGINT NOT NULL AUTO_INCREMENT,
  `Username` VARCHAR(32) NOT NULL,
  `Password` VARCHAR(150) NOT NULL,
  `First_Name` VARCHAR(32) NOT NULL,
  `Last_Name` VARCHAR(150) NOT NULL,
  `Email` VARCHAR(150) NOT NULL,
  `AccountType_ID` BIGINT NOT NULL,
  `RootFolder_ID` BIGINT NULL,
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
-- Table `Files`
-- Stores all uploaded files from the users. It holds
-- information for the file name and 2 foreign keys. One
-- foreign key for the folder that it is in and one for
-- the permissions table.
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
-- Table `SharedFolders`
-- relational table containing information on which users
-- have access to wich folders with the shared permission
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
-- Table `SharedFiles`
-- relational table with information on which users have
-- access to which files.
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
-- -----------------------------------------------------
-- Table tomcat_sessions
-- table containing session information for the Web host
-- and the FileManager
-- -----------------------------------------------------
create table tomcat_sessions (
  session_id     varchar(100) not null primary key,
  valid_session  char(1) not null,
  max_inactive   int not null,
  last_access    bigint not null,
  app_name       varchar(255),
  session_data   mediumblob,
  KEY kapp_name(app_name)
);
-- -----------------------------------------------------
-- Alter folders
-- this block of code creates the users foreign key inside
-- of the folders table. This allows for the entire script
-- to be run without any errors because the table creation
-- is trying to create a foreign key to a table that doesn't
-- exist yet.
-- -----------------------------------------------------
ALTER TABLE `folders` 
DROP FOREIGN KEY `fk_Folders_Permissions1`;
ALTER TABLE `folders` 
ADD INDEX `fk_Folders_Permissions1_idx` (`Permission_ID` ASC),
DROP INDEX `fk_Folders_Permissions1_idx` ;
ALTER TABLE `folders` 
ADD CONSTRAINT `fk_Folders_Permissions1`
  FOREIGN KEY (`Permission_ID`)
  REFERENCES `permissions` (`ID`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_user_id`
  FOREIGN KEY (`Owner_ID`)
  REFERENCES `users` (`ID`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION;
