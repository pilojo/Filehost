package com.algonquincollege.javaApp.database;

import com.algonquincollege.javaApp.database.encryption.PasswordHashUtils;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Handles Database connection to the web host and any other MySQL operation
 * that it might need to perform.
 *
 * @author Alejandra Acosta
 */
public class DBConnection {

    private static final String DRIVER = "com.mysql.jdbc.Driver"; //MySQL db driver
    private static final String URL = "jdbc:mysql://localhost/filehostdb"; //database to use
    private static final String USERNAME = "root";
    private static final String PASSWORD = "60143088431472276664";
    private static final int NAME = 1;
    private static final int PATH = 0;
    private static final long MAX_USAGE = 50000000;

    private Connection connection; //Holds the connnection object to the db so that it can be re-used

    /**
     * Connects to the db.
     *
     * @return connection, returns the connection as an object
     */
    public Connection connect() {
        if (connection == null) {
            try {
                Class.forName(DRIVER).newInstance();
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                System.out.println("DB Connect");
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
                return null;
            }
        }
        return connection;
    }

    /**
     * Makes sure that the user typed in the correct username and password
     *
     * @param username the user's username
     * @param hash the user's hash
     * @return boolean, returns false if there was an exception thrown of if the
     * password sent does not match with the password in the db. Return true if
     * the passwords match.
     */
    public boolean login(String username, String hash) {
        PasswordHashUtils password = new PasswordHashUtils();
        //MySQL query to get the users's password using thier username
        String query = "select Password from users where email = ?";
        System.out.println(username + "\n" + hash); //debugging purposes
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            ResultSet result = statement.executeQuery();//run SQL
            System.out.println("Executed Query");//debugging purposes
            //verify the password is correct
            if (result.next() && password.verifyHash(hash, result.getString("password"))) {
                return true;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    /**
     * Create user and home folder
     *
     * @param firstName new user's first name
     * @param lastName new user's last name
     * @param email new user's email address
     * @param username new user's username
     * @param password new user's password
     * @return true if the user's account was successfully created, false if it
     * wasn't
     */
    public boolean signUp(String firstName, String lastName, String email, String username, String password) {
        System.out.println(firstName + " " + lastName);
        String createUser = "INSERT INTO users (Username, First_Name, Last_Name, Email,AccountType_ID, Password) VALUES (?, ?, ?, ?, 4, ?)";
        //String getUIDQuery = "SELECT ID FROM users WHERE Username = '"+username+"'";
        try {
            //Create the new user
            PreparedStatement userStmt = connection.prepareStatement(createUser, Statement.RETURN_GENERATED_KEYS);
            userStmt.setString(1, username);
            userStmt.setString(2, firstName);
            userStmt.setString(3, lastName);
            userStmt.setString(4, email);
            userStmt.setString(5, password);

            if (userStmt.executeUpdate() == 0) {

                System.out.println("create user");
                return false;
            }
            //gets the newly generated primary key
            ResultSet rs = userStmt.getGeneratedKeys();

            int UID = 0; //holds the new user's Primary key
            if (rs.next()) {
                UID = rs.getInt(1);
                rs.close();
            } else {
                rs.close();
                System.out.println("Hit 1"); //debugging
                return false;
            }

            //create new root folder using the newly created user's primary key for the folder name and Owner_ID
            String createRoot = "INSERT INTO folders (Name, Parent_Path, Owner_ID, Permission_ID) VALUES (?, '/', " + UID + ", 1)";
            PreparedStatement folderStmt = connection.prepareStatement(createRoot, Statement.RETURN_GENERATED_KEYS);
            folderStmt.setString(1, username);
            if (folderStmt.executeUpdate() == 0) {
                System.out.println("create root");
                return false;
            }

            //get newly created folder id
            //String getFIDQuery = "SELECT ID FROM folders WHERE Owner_ID = " +UID;
            //PreparedStatement getFID = connection.prepareStatement(getFIDQuery, Statement.RETURN_GENERATED_KEYS);
            //get the newly generated folder primary key
            ResultSet rsFID = folderStmt.getGeneratedKeys();
            int FID = 0;
            if (rsFID.next()) {
                FID = rsFID.getInt(1);
                rsFID.close();
            } else {
                rsFID.close();
                System.out.println("Hit 2");
                return false;
            }
            //set the newly created user's root folder as the newly created folder
            String linkRoot = "UPDATE users SET RootFolder_ID = " + FID + " WHERE ID = " + UID;
            PreparedStatement linkStmt = connection.prepareStatement(linkRoot);

            if (linkStmt.executeUpdate() == 0) {
                System.out.println("Root folder could not be set.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * ***************************************************************************
     * Folder manipulations
     * ***************************************************************************
     */
    /**
     *
     * @param path
     * @return
     */
    public boolean newFolder(String path) {
        Pattern regex = Pattern.compile("\\/[1-9a-zA-Z]*\\/*");
        Matcher m = regex.matcher(path);
        if (m.matches()) {
            System.out.println("DO NOT MAKE ROOT FOLDERS");
            return false;
        } else if (folderExists(path)) {
            System.out.println("Folder already exists with that name at location");
            return false;
        }
        String[] folder = splitPath(path);
        String[] parentFolder = splitPath(folder[PATH]);
        String user = getUsernameFromPath(path);
        String folderSQL = "INSERT INTO folders (ParentFolder_ID, Name, Parent_Path, Owner_ID, Permission_ID ) SELECT folders.ID, ?, ?, users.ID, folders.Permission_ID FROM folders, users WHERE folders.Name = ? AND folders.Parent_Path = ? AND users.Username = ?";

        try {
            PreparedStatement nFolder = connection.prepareStatement(folderSQL);
            nFolder.setString(1, folder[NAME]);
            nFolder.setString(2, folder[PATH]);
            nFolder.setString(3, parentFolder[NAME]);
            nFolder.setString(4, parentFolder[PATH]);
            nFolder.setString(5, user);
            System.out.println(nFolder.toString());

            if (nFolder.executeUpdate() == 0) {
                System.out.println("New folder could not be created");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        System.out.println("folder created");
        return true;
    }

    /**
     *
     * @param path
     * @return
     */
    public boolean deleteFolder(String path) {
        Pattern regex = Pattern.compile("\\/[1-9a-zA-Z]*\\/*");
        Matcher m = regex.matcher(path);
        if (m.matches()) {
            System.out.println("DO NOT DELETE ROOT FOLDERS");
            return false;
        } else if (!folderExists(path)) {
            System.out.println("Folder doesn't exist with that name at location");
            return false;
        }
        String[] folder = splitPath(path);
        String folderSQL = "DELETE FROM folders WHERE Name = ? AND Parent_Path = ?";
        try {
            PreparedStatement delFolder = connection.prepareStatement(folderSQL);
            delFolder.setString(1, folder[NAME]);
            delFolder.setString(2, folder[PATH]);

            int numDeleted = delFolder.executeUpdate();
            if (numDeleted == 0) {
                System.out.print("Could not delete folder");
                return false;
            } else if (numDeleted > 1) {
                System.out.println("MULTIPLE FOLDERS DELETED");
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        System.out.println(folder[NAME] + " deleted.");
        return true;
    }

    /**
     *
     * @param src
     * @param dst
     * @return
     */
    public boolean updateFolder(String src, String dst) {
        Pattern regex = Pattern.compile("\\/[1-9a-zA-Z]*\\/*");
        Matcher m = regex.matcher(src);
        Matcher m1 = regex.matcher(dst);
        if (m.matches() || m1.matches()) {
            System.out.println("DO NOT TOUCH ROOT FOLDERS");
            return false;
        }
        if (folderExists(dst)) {
            System.out.println("Folder already exists with that name in specified destination");
            return false;
        }
        String folderSQL = "UPDATE folders AS child INNER JOIN (SELECT ID FROM folders WHERE Name = ? and Parent_Path = ?) AS parent SET child.Name = ?, child.Parent_Path = ?, child.ParentFolder_ID = parent.ID WHERE child.Name = ?;";
        String[] old = splitPath(src);
        if (old[PATH].equals("/")) {
            System.out.println("no updates on root folders");
            return false;
        }
        String[] updated = splitPath(dst);
        String[] parent = splitPath(updated[PATH]);
        try {
            PreparedStatement changeFolder = connection.prepareStatement(folderSQL);
            changeFolder.setString(1, parent[NAME]);
            changeFolder.setString(2, parent[PATH]);
            changeFolder.setString(3, updated[NAME]);
            changeFolder.setString(4, updated[PATH]);
            changeFolder.setString(5, old[NAME]);

            if (changeFolder.executeUpdate() == 0) {
                System.out.println("could not update folder");
                return false;
            }
            String getFID = "SELECT ID FROM folders WHERE Name = ? AND Parent_Path = ?";
            PreparedStatement FID = connection.prepareStatement(getFID);
            FID.setString(1, updated[NAME]);
            FID.setString(2, updated[PATH]);

            ResultSet FIDrs = FID.executeQuery();
            System.out.println("plz");
            if (FIDrs.next()) {
                changeChild(FIDrs.getString("ID"), updated[NAME], updated[PATH]);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        System.out.println(src + " updated to " + dst);
        return true;
    }

    /**
     *
     * @param parentID
     * @param parentName
     * @param parentPath
     * @return
     */
    public boolean changeChild(String parentID, String parentName, String parentPath) {
        System.out.println("inChangeChild");
        String kidsSQL = "SELECT ID, Name, Parent_Path FROM folders WHERE ParentFolder_ID = ?";
        String updateKid = "UPDATE folders SET Parent_Path = ? WHERE ParentFolder_ID = ?";
        try {
            PreparedStatement kidsQuery = connection.prepareStatement(kidsSQL);
            kidsQuery.setString(1, parentID);

            //System.out.println(kidsQuery.toString());
            ResultSet kids = kidsQuery.executeQuery();//get all the children info

            int rowCount = kids.last() ? kids.getRow() : 0; //get number of kids
            if (rowCount <= 0) {
                System.out.println("No kids");
                return false;//return false if there are no children
            }
            String newParentPath = parentPath + parentName + "/"; //make the new parent path using the parent parent path (not a typo)
            PreparedStatement changeKids = connection.prepareStatement(updateKid);
            changeKids.setString(1, newParentPath);
            changeKids.setString(2, parentID);

            if (changeKids.executeUpdate() == 0) {//update kids and ensure it's successfull
                System.out.println("Could not update kids");
                return false;
            }

            kids.beforeFirst();
            while (kids.next()) {
                changeChild(kids.getString("ID"), kids.getString("Name"), newParentPath);
            }
        } catch (SQLException e) {
            return false;
        }
        System.out.println("updated children");
        return true;
    }

    public boolean copyFolder(String src, String dst) {
        String[] orig = splitPath(src);
        if (!newFolder(dst + orig[NAME] + "/")) {
            System.out.println("Parent folder could not be created");
            return false;
        }
        return copyTree(src, dst);
    }

    /**
     *
     * @param src
     * @param dst
     * @return
     */
    public boolean copyTree(String src, String dst) {
        String[] orig = splitPath(src);

        String childFolderSQL = "SELECT Name FROM folders WHERE Parent_Path = ?";
        String childFileSQL = "SELECT files.Name, files.storageUsage_Bytes FROM files INNER JOIN folders ON folders.ID = files.ParentFolder_ID WHERE folders.Name = ? AND folders.Parent_PATH = ?";

        try {
            PreparedStatement kidFolders = connection.prepareStatement(childFolderSQL);
            kidFolders.setString(1, src);
            PreparedStatement kidFiles = connection.prepareStatement(childFileSQL);
            kidFiles.setString(1, orig[NAME]);
            kidFiles.setString(2, orig[PATH]);

            ResultSet fileRS = kidFiles.executeQuery();
            while (fileRS.next()) {
                newFile(dst + fileRS.getString("Name"), fileRS.getLong("storageUsage_Bytes"));
            }

            ResultSet folderRS = kidFolders.executeQuery();
            String folderName;
            while (folderRS.next()) {
                folderName = folderRS.getString("Name");
                newFolder(dst + orig[NAME] + "/" + folderName);
                copyTree(src + folderName + "/", dst + orig[NAME] + "/" + folderName + "/");
            }
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    /**
     *
     * @param path
     * @param permissionName
     * @return
     */
    public boolean changeFolderPermission(String path, String permissionName) {
        if (!folderExists(path)) {
            System.out.println("Cannot find folder " + path);
            return false;
        }
        String[] folder = splitPath(path);
        if(folder[PATH].equals("/")){
            System.out.println("DO NOT CHANGE ROOT FOLDER PERMISSION.");
            return false;
        }
        
        String[] parent = splitPath(folder[PATH]);
        String permissionSQL = "SELECT permissions.Name as pName From folders INNER JOIN permissions ON permissions.ID = folders.Permission_ID WHERE folders.Name AND Parent_Path = ?;";
        String folderSQL = "UPDATE folders SET Permission_ID = (SELECT ID FROM permissions WHERE Name = ?) WHERE Name = ? AND Parent_Path = ? ;";

        try {
            PreparedStatement permissionStmt = connection.prepareStatement(permissionSQL);
            permissionStmt.setString(1, parent[NAME]);
            permissionStmt.setString(2, parent[PATH]);
            
            ResultSet permissionRS = permissionStmt.executeQuery();
            if(permissionRS.getString("pName").equals(permissionName)){
                System.out.println("Folder already has that permission.");
                return true;
            }else if(permissionRS.getString("pName").equals("private")){
                System.out.println("Parent folder is private, cannot change child folder permission.");
                return false;
            }
            
            PreparedStatement changeFolder = connection.prepareStatement(folderSQL);
            changeFolder.setString(1, permissionName);
            changeFolder.setString(2, folder[NAME]);
            changeFolder.setString(3, folder[PATH]);

            int updated = changeFolder.executeUpdate();

            if (updated == 0) {
                System.out.println("No folder permission changed");
                return false;
            }
            System.out.println("Folder permission changed.");
            if (permissionName.equals("public")) {
                return true;
            }

            String getFID = "SELECT ID FROM folders WHERE Name = ? AND Parent_Path = ?";
            PreparedStatement FID = connection.prepareStatement(getFID);
            FID.setString(1, folder[NAME]);
            FID.setString(2, folder[PATH]);

            ResultSet FIDrs = FID.executeQuery();
            System.out.println("plz");
            if (FIDrs.next()) {
                childPermissions(FIDrs.getString("ID"), permissionName);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     *
     * @param parentID
     * @param permission
     * @return
     */
    public boolean childPermissions(String parentID, String permission) {
        String childFolderSQL = "SELECT folders.ID, permissions.Name FROM folders INNER JOIN permissions ON folders.Permission_ID = permissions.ID WHERE ParentFolder_ID = ?";
        String updateFolder = "UPDATE folders SET Permission_ID = (SELECT ID FROM permissions WHERE Name = ?) WHERE folders.ID = ?;";
        String childFileSQL = "SELECT files.ID, permissions.Name FROM files INNER JOIN permissions ON files.Permissions_ID = permissions.ID WHERE ParentFolder_ID = ?";
        String updateFile = "UPDATE files SET Permissions_ID = (SELECT ID FROM permissions WHERE Name = ?) WHERE ID = ?";

        try {
            PreparedStatement kidFiles = connection.prepareStatement(childFileSQL);
            kidFiles.setString(1, parentID);
            PreparedStatement kidFolders = connection.prepareStatement(childFolderSQL);
            kidFolders.setString(1, parentID);
            PreparedStatement updateFol = connection.prepareStatement(updateFolder);
            updateFol.setString(1, permission);
            PreparedStatement updateFil = connection.prepareStatement(updateFile);
            updateFil.setString(1, permission);

            ResultSet fileRS = kidFiles.executeQuery();

            while (fileRS.next()) {
                updateFil.setString(2, fileRS.getString("ID"));
                if (permission.equals("shared")) {
                    if (fileRS.getString("Name").equals("public")) {
                        updateFil.executeUpdate();
                    }
                } else {
                    updateFil.executeUpdate();
                }
            }

            ResultSet folderRS = kidFolders.executeQuery();
            while (folderRS.next()) {
                updateFol.setString(2, folderRS.getString("ID"));
                if (permission.equals("shared")) {
                    if (folderRS.getString("Name").equals("public")) {
                        updateFol.executeUpdate();
                    }
                } else {
                    updateFol.executeUpdate();
                }
                childPermissions(folderRS.getString("ID"), permission);
            }

        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    /**
     * *******************************************************************************************
     * File manipulations
     * ******************************************************************************************
     */
    /**
     *
     * @param src
     * @param dst
     * @return
     */
    public boolean updateFile(String src, String dst) {
        if (fileExists(dst)) {
            System.out.println("File already exists at location");
            return false;
        }
        String[] file = splitPath(dst);
        String[] parentFolder = splitPath(file[PATH]);
        String[] old = splitPath(src);
        String[] oldParent = splitPath(old[PATH]);
        String fileSQL = "UPDATE files SET files. Name = ?, ParentFolder_ID = (SELECT ID FROM folders WHERE folders.Name = ? AND folders.Parent_Path = ?),  files.Permissions_ID  = (SELECT Permission_ID FROM folders WHERE folders.Name = ? AND folders.Parent_Path = ?) WHERE files.Name = ? AND files.ParentFolder_ID = (SELECT ID FROM folders WHERE folders.Name = ? AND folders.Parent_Path = ?);";
        try {
            PreparedStatement mvFile = connection.prepareStatement(fileSQL);
            mvFile.setString(1, file[NAME]);
            mvFile.setString(2, parentFolder[NAME]);
            mvFile.setString(3, parentFolder[PATH]);
            mvFile.setString(4, parentFolder[NAME]);
            mvFile.setString(5, parentFolder[PATH]);
            mvFile.setString(6, old[NAME]);
            mvFile.setString(7, oldParent[NAME]);
            mvFile.setString(8, oldParent[PATH]);
            System.out.println(mvFile.toString());

            if (mvFile.executeUpdate() == 0) {
                System.out.println("Could not change file.");
                return false;
            }

        } catch (SQLException e) {
            System.out.println("Exception in file update.");
            return false;
        }
        System.out.println(src + " file updated to " + dst);
        return true;
    }

    /**
     *
     * @param path
     * @return
     */
    public boolean deleteFile(String path) {
        String[] file = splitPath(path);
        String[] parent = splitPath(file[PATH]);
        String fileSQL = "DELETE FROM files WHERE files.Name = ? AND files.ParentFolder_ID = (SELECT ID FROM folders WHERE folders.Name = ? AND folders.Parent_Path = ?);";
        try {
            PreparedStatement delFile = connection.prepareStatement(fileSQL);
            delFile.setString(1, file[NAME]);
            delFile.setString(2, parent[NAME]);
            delFile.setString(3, parent[PATH]);
            System.out.println(delFile);

            if (delFile.executeUpdate() == 0) {
                System.out.println("Could not delete file.");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        System.out.println("File was deleted.");
        return true;
    }

    /**
     *
     * @param filePath
     * @param size
     * @return
     */
    public boolean newFile(String filePath, long size) {
        if (fileExists(filePath)) {
            System.out.println("File with that name already exists at " + filePath);
            return false;
        }
        String[] file = splitPath(filePath);
        if (file[PATH].equals("/")) {
            System.out.println("DO NOT CREATE IN ROOT ROOT");
            return false;
        }
        if (size > MAX_USAGE) {
            System.out.println("File cannot be bigger than " + MAX_USAGE);
            return false;
        }
        String username = getUsernameFromPath(filePath);
        String[] parentFolder = splitPath(file[PATH]);
        //String fileQuery = "SELECT * FROM files INNER JOIN folders ON folders.ID = files.ParentFolder_ID WHERE folders.Name = ? AND folders.Parent_Path = ?;";
        String fileSQL = "INSERT INTO files (Name, Permissions_ID, ParentFolder_ID, byteSize) VALUES (?, 1, (SELECT ID FROM folders WHERE folders.Name = ? AND folders.Parent_Path = ?), ?);";
        String userSQL = "UPDATE users SET storageUsage_Bytes = storageUsage_Bytes + ? WHERE Username = ? AND (storageUsage_Bytes + ?) <= ?;";
        try {
            PreparedStatement updateMax = connection.prepareStatement(userSQL);
            updateMax.setLong(1, size);
            updateMax.setString(2, username);
            updateMax.setLong(3, size);
            updateMax.setLong(4, MAX_USAGE);
            if (updateMax.executeUpdate() == 0) {
                System.out.println("User has reached max usage, or doesn't exist.");
                return false;
            }

            PreparedStatement nFile = connection.prepareStatement(fileSQL);
            nFile.setString(1, file[NAME]);
            nFile.setString(2, parentFolder[NAME]);
            nFile.setString(3, parentFolder[PATH]);
            nFile.setLong(4, size);

            if (nFile.executeUpdate() == 0) {
                System.out.println("Could not create new file");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("File was not created due to an Exception being thrown");
            return false;
        }
        System.out.println("File created");
        return true;
    }

    /**
     *
     * @param path
     * @param permissionName
     * @return
     */
    public boolean changeFilePermission(String path, String permissionName) {
        String[] file = splitPath(path);
        String[] parent = splitPath(file[PATH]);
        String fileSQL = "UPDATE files INNER JOIN folders ON files.ParentFolder_ID = folders.ID SET files.Permissions_ID = (SELECT ID FROM permissions WHERE NAME = ?) WHERE files.Name = ? AND folders.Name = ? AND folders.Parent_Path = ?;";
        String permissionSQL = "SELECT permissions.Name as pName From folders INNER JOIN permissions ON permissions.ID = folders.Permission_ID WHERE folders.Name AND Parent_Path = ?;";
        try {
            PreparedStatement permissionStmt = connection.prepareStatement(permissionSQL);
            permissionStmt.setString(1, parent[NAME]);
            permissionStmt.setString(2, parent[PATH]);
            
            ResultSet permissionRS = permissionStmt.executeQuery();
            if(permissionRS.getString("pName").equals(permissionName)){
                System.out.println("File already has that permission.");
                return true;
            }else if(permissionRS.getString("pName").equals("private")){
                System.out.println("Parent folder is private, cannot change child file permission.");
                return false;
            }
            PreparedStatement files = connection.prepareStatement(fileSQL);
            files.setString(1, permissionName);
            files.setString(2, file[NAME]);
            files.setString(3, parent[NAME]);
            files.setString(4, parent[PATH]);

            int updated = files.executeUpdate();
            if (updated == 0) {
                System.out.println("Could not change file permission for " + path);
                return false;
            } else if (updated > 1) {
                System.out.println("MUTLIPLE FILES UPDATED");
            } else {
                return true;
            }
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    /**
     * ****************************************************************************
     * Search and list
     * ****************************************************************************
     */
    /**
     *
     * @param path
     * @return
     */
    public String[][] list(String path) {
        String[] folder = splitPath(path);
        String fileSQL = "SELECT lastModified, byteSize, isVirus, isScanned, files.Name, permissions.Name pName FROM files INNER JOIN folders ON folders.ID = files.ParentFolder_ID INNER JOIN permissions ON files.Permissions_ID = permissions.ID WHERE folders.Name = ? AND folders.Parent_PATH = ?;";
        String folderSQL = "SELECT folders.Name, permissions.Name AS pName FROM folders INNER JOIN  permissions ON Permission_ID = permissions.ID WHERE Parent_Path = ?;";
        String[][] contents = null;
        int numData = 7;
        try {
            PreparedStatement getFiles = connection.prepareStatement(fileSQL);
            getFiles.setString(1, folder[NAME]);
            getFiles.setString(2, folder[PATH]);
            ResultSet fileRS = getFiles.executeQuery();

            int rowCount = fileRS.last() ? fileRS.getRow() : 0;
            fileRS.beforeFirst();

            PreparedStatement getFolders = connection.prepareStatement(folderSQL);
            getFolders.setString(1, path);

            ResultSet folderRS = getFolders.executeQuery();

            rowCount += folderRS.last() ? folderRS.getRow() : 0;
            folderRS.beforeFirst();

            contents = new String[rowCount][numData];
            int i = 0;
            while (folderRS.next()) {
                contents[i][0] = folderRS.getString("Name");
                contents[i][1] = "Folder";
                contents[i][2] = folderRS.getString("pName");
                for (int j = 3; j < numData; j++) {
                    contents[i][j] = "";
                }
                i++;
            }
            while (fileRS.next()) {
                contents[i][0] = fileRS.getString("Name");
                contents[i][1] = "File";
                contents[i][2] = fileRS.getString("pName");
                contents[i][3] = fileRS.getString("lastModified");
                contents[i][4] = fileRS.getString("byteSize");
                contents[i][5] = fileRS.getString("isScanned");
                contents[i][6] = fileRS.getString("isVirus");
                i++;
            }

        } catch (SQLException e) {
        }
        return contents;
    }

    /**
     *
     * @param username
     * @param firstname
     * @param lastname
     * @param filename
     * @return
     */
    public String[][] searchFiles(String username, String firstname, String lastname, String filename) {
        String[][] files = null;
        String fileSQL = "SELECT files.Name, Username, Parent_Path, folders.Name AS folderName FROM files INNER JOIN folders ON files.ParentFolder_ID = folders.ID INNER JOIN users ON folders.Owner_ID = users.ID WHERE Username LIKE ? AND First_Name LIKE ? AND Last_Name LIKE ? AND files.Name LIKE ?;";

        if (username == null) {
            username = "%";
        }
        if (firstname == null) {
            firstname = "%";
        }
        if (lastname == null) {
            lastname = "%";
        }
        if (filename == null) {
            filename = "%";
        }

        try {
            PreparedStatement getFiles = connection.prepareStatement(fileSQL);
            getFiles.setString(1, username);
            getFiles.setString(2, firstname);
            getFiles.setString(3, lastname);
            getFiles.setString(4, filename);

            ResultSet fileRS = getFiles.executeQuery();

            int rowCount = fileRS.last() ? fileRS.getRow() : 0;
            fileRS.beforeFirst();

            if (rowCount == 0) {
                System.out.println("No results found.");
                return files;
            }

            files = new String[rowCount][4];
            int i = 0;
            while (fileRS.next()) {
                files[i][0] = fileRS.getString("Name");
                files[i][1] = "File";
                files[i][2] = fileRS.getString("Username");
                files[i][3] = fileRS.getString("Parent_Path") + fileRS.getString("folderName") + "/";
                i++;
            }

        } catch (SQLException e) {

        }
        return files;
    }

    public String[][] searchUsers(String username, String firstname, String lastname) {
        String[][] users = null;
        String userSQL = "SELECT Username, First_Name, Last_Name FROM users WHERE Username LIKE ? AND First_Name LIKE ? AND Last_Name LIKE ?;";

        if (username == null) {
            username = "%";
        }
        if (firstname == null) {
            firstname = "%";
        }
        if (lastname == null) {
            lastname = "%";
        }
        try {
            PreparedStatement getUsers = connection.prepareStatement(userSQL);
            getUsers.setString(1, username);
            getUsers.setString(2, firstname);
            getUsers.setString(3, lastname);

            ResultSet userRS = getUsers.executeQuery();

            int rowCount = userRS.last() ? userRS.getRow() : 0;
            userRS.beforeFirst();

            if (rowCount == 0) {
                System.out.println("No results found.");
                return users;
            }

            int i = 0;
            users = new String[rowCount][3];
            while (userRS.next()) {
                users[i][0] = userRS.getString("Username");
                users[i][1] = userRS.getString("First_Name");
                users[i][2] = userRS.getString("Last_Name");
                i++;
            }
        } catch (SQLException e) {
            return null;
        }
        return users;
    }

    public String[] getGroupName(String type, String path) {
        String[] groups = null;
        try {
            PreparedStatement stmt;
            if (type.equals("File")) {
                String[] file = splitPath(path);
                String[] parent = splitPath(file[PATH]);
                String SQL = "SELECT Groupname FROM sharedfiles INNER JOIN files ON files.ID = sharedfiles.File_ID INNER JOIN folders folders.ID = files.ParentFolder_ID WERE files.Name = ? AND folders.Name = ? AND folders.Parent_Path = ?;";
                stmt = connection.prepareStatement(SQL);
                stmt.setString(1, file[NAME]);
                stmt.setString(2, parent[NAME]);
                stmt.setString(3, parent[PATH]);
            }else{
                String[] folder = splitPath(path);
                String SQL = "SELECT Groupname FROM sharedfolders INNER JOIN folders ON folders.ID = sharedfolders.Folder_ID WHERE folders.Name = ? AND folders.Parent_Path = ?;";
                stmt = connection.prepareStatement(SQL);
                stmt.setString(1, folder[NAME]);
                stmt.setString(2, folder[PATH]);
                
            }
            ResultSet groupRS = stmt.executeQuery();
            int rowCount = groupRS.last() ? groupRS.getRow() : 0;
            groupRS.beforeFirst();

            if (rowCount == 0) {
                System.out.println(path+" has no groups associated to it.");
                return groups;
            }
            
            groups = new String[rowCount];
            for(int i = 0; i < rowCount; i++){
                groups[i] = groupRS.getString("Groupname");
            }
        } catch (SQLException e) {
            return null;
        }
        return groups;
    }

    /**
     * ***************************************************************************
     * info from path
     * ****************************************************************************
     */
    /**
     *
     * @param fullPath
     * @return
     */
    public static String[] splitPath(String fullPath) {
        String[] retVals = new String[2];
        fullPath = fullPath.charAt(fullPath.length() - 1) == '/' ? fullPath.substring(0, fullPath.length() - 1) : fullPath;
        retVals[PATH] = fullPath.substring(0, fullPath.lastIndexOf('/') + 1); //get ParentPath
        retVals[NAME] = fullPath.substring(fullPath.lastIndexOf('/') + 1, fullPath.length());//get folder name
        return retVals;
    }

    /**
     *
     * @param fullPath
     * @return
     */
    public String getUsernameFromPath(String fullPath) {
        String[] sepPath = fullPath.split("/");
        return sepPath[1];
    }

    /**
     *
     * @param path
     * @return
     */
    public boolean fileExists(String path) {
        String fileSQL = "SELECT files.Name FROM files INNER JOIN folders ON folders.ID = files.ParentFolder_ID WHERE folders.Name = ? AND folders.Parent_PATH = ? AND files.Name = ?;";
        String[] file = splitPath(path);
        String[] parentFolder = splitPath(file[PATH]);
        try {
            PreparedStatement getFiles = connection.prepareStatement(fileSQL);
            getFiles.setString(1, parentFolder[NAME]);
            getFiles.setString(2, parentFolder[PATH]);
            getFiles.setString(3, file[NAME]);
            //System.out.println(getFiles.toString());

            ResultSet fileRS = getFiles.executeQuery();
            return fileRS.next();

        } catch (SQLException e) {
            return false;
        }
    }

    /**
     *
     * @param path
     * @return
     */
    public boolean folderExists(String path) {
        String[] folder = splitPath(path);
        String folderSQL = "SELECT ID FROM folders WHERE Name = ? AND Parent_Path = ?";
        try {
            PreparedStatement getFolders = connection.prepareStatement(folderSQL);
            getFolders.setString(1, folder[NAME]);
            getFolders.setString(2, folder[PATH]);

            ResultSet folderRS = getFolders.executeQuery();

            return folderRS.next();
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean verifyPath(String path) {
        String[] item = splitPath(path);
        boolean exists = folderExists(item[PATH]);

        while (!item[PATH].equals("/") && exists) {
            //System.out.println(item[PATH]);
            item = splitPath(item[PATH]);
        }

        return exists;
    }

    public String getUsernameFromEmail(String email) {
        String userSQL = "SELECT Username FROM users WHERE Email = ?";
        try {
            PreparedStatement getU = connection.prepareStatement(userSQL);
            getU.setString(1, email);

            ResultSet userRS = getU.executeQuery();
            userRS.next();

            return userRS.getString("Username");
        } catch (SQLException e) {

        }
        return null;
    }

    public String getUserIDFromUsername(String username) {
        String userSQL = "SELECT ID FROM users WHERE username = ?";
        try {
            PreparedStatement getU = connection.prepareStatement(userSQL);
            getU.setString(1, username);

            ResultSet userRS = getU.executeQuery();
            userRS.next();

            return userRS.getString("ID");
        } catch (SQLException e) {

        }
        return null;
    }

    public boolean verifyOwner(String email, String path) {
        String userSQL = "SELECT Username FROM users WHERE Email = ?";
        try {
            PreparedStatement UID = connection.prepareStatement(userSQL);
            UID.setString(1, email);

            ResultSet UIDrs = UID.executeQuery();
            if (!UIDrs.next()) {
                System.out.println("No user with email: " + email);
                return false;
            }

            return getUsernameFromPath(path).equals(UIDrs.getString("Username"));

        } catch (SQLException e) {
            System.out.println("Bad things happened while verifying user.");
            return false;
        }
    }

    /**
     * ************************************************************************
     * flags
     * ************************************************************************
     */
    /**
     *
     * @param path
     * @return
     */
    public boolean setScannedFlag(String path) {
        String[] file = splitPath(path);

        if (file[PATH] == "/") {
            System.out.println("There shouldn't be any files in root root. Stop.");
            return false;
        }
        String[] parent = splitPath(file[PATH]);
        String fileSQL = "UPDATE files SET isScanned = 1 WHERE Name = ? AND ParentFolder_ID = (SELECT ID FROM folders WHERE Name = ? AND Parent_PATH = ?);";
        try {
            PreparedStatement setFlag = connection.prepareStatement(fileSQL);
            setFlag.setString(1, file[NAME]);
            setFlag.setString(2, parent[NAME]);
            setFlag.setString(3, parent[PATH]);
            if (setFlag.executeUpdate() == 0) {
                System.out.println("flag not set");
                return false;
            }
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    /**
     *
     * @param path
     * @return
     */
    public boolean setVirusFlag(String path) {
        String[] file = splitPath(path);

        if (file[PATH] == "/") {
            System.out.println("There shouldn't be any files in root root. Stop.");
            return false;
        }
        String[] parent = splitPath(file[PATH]);
        String fileSQL = "UPDATE files SET isVirus = 1 WHERE Name = ? AND ParentFolder_ID = (SELECT ID FROM folders WHERE Name = ? AND Parent_PATH = ?);";
        try {
            PreparedStatement setFlag = connection.prepareStatement(fileSQL);
            setFlag.setString(1, file[NAME]);
            setFlag.setString(2, parent[NAME]);
            setFlag.setString(3, parent[PATH]);
            if (setFlag.executeUpdate() == 0) {
                System.out.println("flag not set");
                return false;
            }
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    /**
     * ***************************************************************************************************************************************************
     * Groups (kill me)
     * ****************************************************************************************************************************************************8
     */
    /**
     *
     * @param groupName
     * @param username
     * @return
     */
    public boolean newGroup(String groupName, String username) {
        if (groupExists(groupName)) {
            System.out.println("Group already exists.");
            return false;
        }

        String groupSQL = "INSERT INTO groups (Name, Owner_username) VALUES (?,?);";
        try {
            PreparedStatement newGroup = connection.prepareStatement(groupSQL);
            newGroup.setString(1, groupName);
            newGroup.setString(2, username);

            if (newGroup.executeUpdate() == 0) {
                System.out.println(groupName + "not created.");
                return false;
            }
            if (!addUserToGroup(username, groupName)) {
                System.out.println("Could not give group access to owner.");
                return false;
            }

        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    /**
     *
     * @param groupName
     * @return
     */
    public boolean groupExists(String groupName) {
        String groupSQL = "Select * FROM groups WHERE Name = ?";
        try {
            PreparedStatement groupStmt = connection.prepareStatement(groupSQL);
            groupStmt.setString(1, groupName);

            ResultSet groupRS = groupStmt.executeQuery();

            return groupRS.next();

        } catch (SQLException e) {

        }
        return false;
    }

    /**
     *
     * @param groupName
     * @return
     */
    public boolean deleteGroup(String groupName) {
        if (groupExists(groupName)) {
            System.out.println("Group doesn't exist.");
            return false;
        }
        String groupSQL = "DELETE FROM groups WHERE Name = ?;";
        try {
            PreparedStatement delGroup = connection.prepareStatement(groupSQL);
            delGroup.setString(1, groupName);

            if (delGroup.executeUpdate() == 0) {
                System.out.println("No groups deleted");
                return false;
            }
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    /**
     *
     * @param username
     * @param groupName
     * @return
     */
    public boolean addUserToGroup(String username, String groupName) {
        String userGroupSQL = "INSERT INTO user_group (username, groupname) VALUES (?,?);";
        try {
            PreparedStatement addUser = connection.prepareStatement(userGroupSQL);
            addUser.setString(1, username);
            addUser.setString(2, groupName);

            if (addUser.executeUpdate() == 0) {
                System.out.println("Could not share with user.");
                return false;
            }
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    /**
     *
     * @param path
     * @param groupName
     * @return
     */
    public boolean shareFolderWithGroup(String path, String groupName) {
        String[] folder = splitPath(path);
        if (folder[PATH].equals("/")) {
            System.out.println("CANNOT SHARE ROOT FOLDER.");
            return false;
        }
        if (!groupExists(groupName)) {
            System.out.println("Group doesn't exist.");
            return false;
        }

        if (!changeFolderPermission(path, "shared")) {
            System.out.println("Could not change folder permission to shared.");
            return false;
        }

        String groupSQL = "INSERT INTO sharedfolders (Folder_ID, GroupName) SELECT folders.ID, ? FROM folders WHERE folders.Name = ? AND folders.Parent_Path = ?;";
        try {
            PreparedStatement groupStmt = connection.prepareStatement(groupSQL);
            groupStmt.setString(1, groupName);
            groupStmt.setString(2, folder[NAME]);
            groupStmt.setString(3, folder[PATH]);

            if (groupStmt.executeUpdate() == 0) {
                System.out.println("folder not shared.");
                return false;
            }
            String folderSQL = "SELECT Name FROM folders WHERE Parent_Path = ?;";
            PreparedStatement folderStmt = connection.prepareStatement(folderSQL);
            folderStmt.setString(1, path);

            ResultSet childFolRS = folderStmt.executeQuery();
            while (childFolRS.next()) {
                shareFolderWithGroup((path + childFolRS.getString("Name")), groupName);
            }

            String fileSQL = "SELECT files.Name FROM files WHERE ParentFolder_ID = (SELECT ID FROM folders WHERE Name = ? AND Parent_Path = ?);";
            PreparedStatement fileStmt = connection.prepareStatement(fileSQL);
            fileStmt.setString(1, folder[NAME]);
            fileStmt.setString(2, folder[PATH]);

            ResultSet childFileRS = fileStmt.executeQuery();
            while (childFileRS.next()) {
                shareFileWithGroup(path + childFileRS.getString("Name"), groupName);
            }

        } catch (SQLException e) {
            return false;
        }

        return true;
    }

    /**
     *
     * @param path
     * @param groupname
     * @return
     */
    public boolean shareFileWithGroup(String path, String groupname) {
        String[] file = splitPath(path);
        String[] parent = splitPath(file[PATH]);
        String fileSQL = "INSERT INTO shared_files (File_ID, Groupname) SELECT files.ID, ? FROM files WHERE Name = ? AND ParentFolder_ID = (SELECT ID FROM folders WHERE Name = ? AND Parent_Path = ?);";

        if (!fileExists(path) || !groupExists(groupname)) {
            System.out.println("Something doesn't exist.");
            return false;
        }
        if (!changeFilePermission(path, "shared")) {
            System.out.println("Could not change file permission.");
            return false;
        }

        try {
            PreparedStatement fileStmt = connection.prepareStatement(fileSQL);
            fileStmt.setString(1, groupname);
            fileStmt.setString(2, file[NAME]);
            fileStmt.setString(3, parent[NAME]);
            fileStmt.setString(4, parent[PATH]);

            if (fileStmt.executeUpdate() == 0) {
                System.out.println("File was not shared with group.");
                return false;
            }

        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    /**
     *
     * @param username
     * @param path
     * @return
     */
    public boolean canUserAccessFolder(String username, String path) {
        String[] folder = splitPath(path);
        String folderSQL = "SELECT permissions.Name as pName FROM folders INNER JOIN permissions ON folders.permission_ID = permissions.ID WHERE folders.Name = ? AND folders.Parent_Path = ?;";
        if (!fileExists(path)) {
            System.out.println("folder cannot be accessed, as it doesn't exist.");
            return false;
        }

        try {
            PreparedStatement folderStmt = connection.prepareStatement(folderSQL);
            folderStmt.setString(1, folder[NAME]);
            folderStmt.setString(2, folder[PATH]);

            ResultSet permission = folderStmt.executeQuery();
            if (permission.next()) {
                String permissionName = permission.getString("pName");
                if (permissionName.equals("public") || (getUsernameFromPath(path).equals(username))) {
                    return true;
                } else if (permissionName.equals("shared")) {
                    String sharedSQL = "SELECT * FROM folders INNER JOIN sharedfolders ON sharedfolders.Folder_ID = folders.ID INNER JOIN groups ON sharedfolders.Groupname = groups.Name INNER JOIN user_group ON user_group.groupname = groups.Name WHERE user_group.username = ? AND folders.Name = ? AND folders.Parent_Path = ?;";
                    PreparedStatement sharedStmt = connection.prepareStatement(sharedSQL);
                    sharedStmt.setString(1, username);
                    sharedStmt.setString(2, folder[NAME]);
                    sharedStmt.setString(3, folder[PATH]);

                    ResultSet fileRS = sharedStmt.executeQuery();
                    return fileRS.next();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    /**
     *
     * @param username
     * @param path
     * @return
     */
    public boolean canUserAccessFile(String username, String path) {
        String[] file = splitPath(path);
        String[] parent = splitPath(file[PATH]);
        String fileSQL = "SELECT permissions.Name as pName FROM files INNER JOIN permissions ON files.permissions_ID = permissions.ID WHERE files.Name = ? AND ParentFolder_ID = (SELECT ID FROM folders WHERE folders.Name = ? AND folders.Parent_Path = ?);";
        if (!fileExists(path)) {
            System.out.println("file cannot be accessed, as it doesn't exist.");
            return false;
        }

        try {
            PreparedStatement fileStmt = connection.prepareStatement(fileSQL);
            fileStmt.setString(1, file[NAME]);
            fileStmt.setString(2, parent[NAME]);
            fileStmt.setString(3, parent[PATH]);

            ResultSet permission = fileStmt.executeQuery();
            if (permission.next()) {
                String permissionName = permission.getString("pName");
                if (permissionName.equals("public") || (getUsernameFromPath(path).equals(username))) {
                    return true;
                } else if (permissionName.equals("shared")) {
                    String sharedSQL = "SELECT * FROM files INNER JOIN sharedfiles ON sharedfiles.File_ID = files.ID INNER JOIN groups ON sharedfiles.Groupname = groups.Name INNER JOIN user_group ON user_group.groupname = groups.Name WHERE user_group.username = ? AND files.ParentFolder_ID = (SELECT ID FROM folders WHERE Name = ? AND Parent_Path = ?);";
                    PreparedStatement sharedStmt = connection.prepareStatement(sharedSQL);
                    sharedStmt.setString(1, username);
                    sharedStmt.setString(2, parent[NAME]);
                    sharedStmt.setString(3, parent[PATH]);

                    ResultSet fileRS = sharedStmt.executeQuery();
                    return fileRS.next();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    /**
     * Disconnects from the database.
     */
    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                System.out.println("DB disconnected");
            } catch (SQLException e) {

            }
        }
    }

}
