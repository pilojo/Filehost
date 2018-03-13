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
     * File/Folder manipulations
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
        String childFileSQL = "SELECT files.Name FROM files INNER JOIN folders ON folders.ID = files.ParentFolder_ID WHERE folders.Name = ? AND folders.Parent_PATH = ?";

        try {
            PreparedStatement kidFolders = connection.prepareStatement(childFolderSQL);
            kidFolders.setString(1, src);
            PreparedStatement kidFiles = connection.prepareStatement(childFileSQL);
            kidFiles.setString(1, orig[NAME]);
            kidFiles.setString(2, orig[PATH]);

            ResultSet fileRS = kidFiles.executeQuery();
            while (fileRS.next()) {
                newFile(dst + fileRS.getString("Name"));
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
     * @return
     */
    public boolean newFile(String filePath) {
        if (fileExists(filePath)) {
            System.out.println("File with that name already exists at " + filePath);
            return false;
        }
        String[] file = splitPath(filePath);
        if (file[PATH].equals("/")) {
            System.out.println("DO NOT CREATE IN ROOT ROOT");
            return false;
        }
        String[] parentFolder = splitPath(file[PATH]);
        //String fileQuery = "SELECT * FROM files INNER JOIN folders ON folders.ID = files.ParentFolder_ID WHERE folders.Name = ? AND folders.Parent_Path = ?;";
        String fileSQL = "INSERT INTO files (Name, Permissions_ID, ParentFolder_ID) VALUES (?, 1, (SELECT ID FROM folders WHERE folders.Name = ? AND folders.Parent_Path = ?));";
        try {
            PreparedStatement nFile = connection.prepareStatement(fileSQL);
            nFile.setString(1, file[NAME]);
            nFile.setString(2, parentFolder[NAME]);
            nFile.setString(3, parentFolder[PATH]);

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
    
    public boolean changeFolderPermission(String path, String permissionName){
        String[] folder = splitPath(path);
        String folderSQL = "UPDATE folders SET Permission_ID = (SELECT ID FROM permissions WHERE Name = ?) WHERE Name = ? AND Parent_Path = ? ;";
        
        try{
            PreparedStatement changeFolder = connection.prepareStatement(folderSQL);
            changeFolder.setString(1, permissionName);
            changeFolder.setString(2, folder[NAME]);
            changeFolder.setString(3, folder[PATH]);
            
            int updated = changeFolder.executeUpdate();
            
            if(updated == 0){
                System.out.println("No folder permission changed");
                return false;
            }
            System.out.println("Folder permission changed.");
            
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
    public boolean changeFilePermission(String path, String permissionName){
        String[] file = splitPath(path);
        String[] parent = splitPath(file[PATH]);
        String fileSQL = "UPDATE files INNER JOIN folders ON files.ParentFolder_ID = folders.ID SET files.Permissions_ID = (SELECT ID FROM permissions WHERE NAME = ?) WHERE files.Name = ? AND folders.Name = ? AND folders.Parent_Path = ?;";
        
        try{
            PreparedStatement files = connection.prepareStatement(fileSQL);
            files.setString(1, permissionName);
            files.setString(2, file[NAME]);
            files.setString(3, parent[NAME]);
            files.setString(4, parent[PATH]);
            
            int updated = files.executeUpdate();
            if(updated== 0){
                System.out.println("Could not change file permission for "+ path);
                return false;
            }else if(updated > 1){
                System.out.println("MUTLIPLE FILES UPDATED");
            }else{
                return true;
            }
        }catch(SQLException e){
            return false;
        }
        return true;
    }

    /**
     *
     * @param path
     * @return
     */
    public String[][] list(String path) {
        String[] folder = splitPath(path);
        String fileSQL = "SELECT files.Name, permissions.Name pName FROM ((files INNER JOIN folders ON folders.ID = files.ParentFolder_ID ) INNER JOIN permissions ON files.Permissions_ID = permissions.ID) WHERE folders.Name = ? AND folders.Parent_PATH = ?;";
        String folderSQL = "SELECT folders.Name, permissions.Name AS pName FROM folders INNER JOIN  permissions ON Permission_ID = permissions.ID WHERE Parent_Path = ?;";
        String[][] contents = null;
        int numData = 3;
        try {
            PreparedStatement getFiles = connection.prepareStatement(fileSQL);
            getFiles.setString(1, folder[NAME]);
            getFiles.setString(2, folder[PATH]);
            ResultSet fileRS = getFiles.executeQuery();
            System.out.println(getFiles);

            int rowCount = fileRS.last() ? fileRS.getRow() : 0;
            fileRS.beforeFirst();

            PreparedStatement getFolders = connection.prepareStatement(folderSQL);
            getFolders.setString(1, path);
            System.out.println(getFolders);

            ResultSet folderRS = getFolders.executeQuery();

            rowCount += folderRS.last() ? folderRS.getRow() : 0;
            folderRS.beforeFirst();

            contents = new String[rowCount][numData];
            int i = 0;
            while (folderRS.next()) {
                contents[i][0] = folderRS.getString("Name");
                contents[i][1] = "Folder";
                contents[i][2] = folderRS.getString("pName");
                i++;
            }
            while (fileRS.next()) {
                contents[i][0] = fileRS.getString("Name");
                contents[i][1] = "File";
                contents[i][2] = fileRS.getString("pName");
                i++;
            }

        } catch (SQLException e) {
        }
        return contents;
    }
    
    public String[][] searchFiles(String username,  String firstname, String lastname, String filename){
        String [][]files = null;
        String fileSQL = "SELECT files.Name, Username, Parent_Path, folders.Name AS folderName FROM files INNER JOIN folders ON files.ParentFolder_ID = folders.ID INNER JOIN users ON folders.Owner_ID = users.ID WHERE Username LIKE ? AND First_Name LIKE ? AND Last_Name LIKE ? AND files.Name LIKE ?;";
        
        if(username == null) username = "%";
        if(firstname == null) firstname = "%";
        if(lastname == null) lastname = "%";
        if(filename == null) filename = "%";
        
        try{
            PreparedStatement getFiles = connection.prepareStatement(fileSQL);
            getFiles.setString(1, username);
            getFiles.setString(2, firstname);
            getFiles.setString(3, lastname);
            getFiles.setString(4, filename);
            
            ResultSet fileRS = getFiles.executeQuery();
            
            int rowCount = fileRS.last() ? fileRS.getRow() : 0;
            fileRS.beforeFirst();
            
            if(rowCount == 0){
                System.out.println("No results found.");
                return files;
            }
            
            files = new String[rowCount][4];
            int i = 0;
            while(fileRS.next()){
                files[i][0] = fileRS.getString("Name");
                files[i][1] = "File";
                files[i][2] = fileRS.getString("Username");
                files[i][3] = fileRS.getString("Parent_Path") + fileRS.getString("folderName") +"/";
                i++;
            }
            
            
        }catch(SQLException e){
            
        }
        return files;
    }

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
        String userSQL = "SELECT ID FROM users WHERE Email = ?";
        try {
            PreparedStatement UID = connection.prepareStatement(userSQL);
            UID.setString(1, email);

            ResultSet UIDrs = UID.executeQuery();
            if (!UIDrs.next()) {
                System.out.println("No user with email: " + email);
                return false;
            }

            return getUsernameFromPath(path).equals(UIDrs.getString("ID"));

        } catch (SQLException e) {
            System.out.println("Bad things happened while verifying user.");
            return false;
        }
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
