package com.algonquincollege.javaApp.database;

import com.algonquincollege.javaApp.database.encryption.PasswordHashUtils;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
    private static final String URL = "jdbc:mysql://localhost/filehosttest"; //database to use
    private static final String USERNAME = "root";
    private static final String PASSWORD = "password";
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
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {

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
        PasswordHashUtils pass = new PasswordHashUtils();
        System.out.println(firstName + " " + lastName);
        String createUser = "INSERT INTO users (Username, First_Name, Last_Name, Email,AccountType_ID, Password) VALUES (?, ?, ?, ?, 5, ?)";
        //String getUIDQuery = "SELECT ID FROM users WHERE Username = '"+username+"'";
        try {
            //Create the new user
            PreparedStatement userStmt = connection.prepareStatement(createUser, Statement.RETURN_GENERATED_KEYS);
            userStmt.setString(1, username);
            userStmt.setString(2, firstName);
            userStmt.setString(3, lastName);
            userStmt.setString(4, email);
            userStmt.setString(5, pass.hash(password));

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
            String createRoot = "INSERT INTO folders (Name, Parent_Path, Owner_ID, Permission_ID) VALUES ('" + UID + "', '/', " + UID + ", 4)";
            PreparedStatement folderStmt = connection.prepareStatement(createRoot, Statement.RETURN_GENERATED_KEYS);
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
        String user = getUserIDFromPath(path);
        String folderSQL = "INSERT INTO folders (ParentFolder_ID, Name, Parent_Path, Owner_ID, Permission_ID ) SELECT folders.ID, ?, ?, users.ID, 1 FROM folders, users WHERE folders.Name = ? AND folders.Parent_Path = ? AND users.Username = ?;";
        //String parentSQL = "SELECT Name FROM folders WHERE Parent_Path = ?";
        try {
            /*PreparedStatement getFolders = connection.prepareStatement(parentSQL);
            getFolders.setString(1, folder[PATH]);
            ResultSet folderRS = getFolders.executeQuery();
            while (folderRS.next()) {
                if (folderRS.getString("Name").equals(folder[NAME])) {
                    System.out.println("Same name folder in parent path");
                    return false;
                }
            }*/

            PreparedStatement nFolder = connection.prepareStatement(folderSQL);
            nFolder.setString(1, folder[NAME]);
            nFolder.setString(2, folder[PATH]);
            nFolder.setString(3, parentFolder[NAME]);
            nFolder.setString(4, parentFolder[PATH]);
            nFolder.setString(5, user);

            if (nFolder.executeUpdate() == 0) {
                System.out.print("New folder could not be created");
                return false;
            }
        } catch (SQLException e) {

        }
        return true;
    }
    /**
     * 
     * @param path
     * @return 
     */
    public boolean deleteFolder(String path) {
        String[] folder = splitPath(path);
        String folderSQL = "DELETE FROM folders WHERE Name = ? AND Parent_Path = ?";
        try {
            PreparedStatement delFolder = connection.prepareStatement(folderSQL);
            delFolder.setString(1, folder[NAME]);
            delFolder.setString(2, folder[PATH]);

            if (delFolder.executeUpdate() == 0) {
                System.out.print("Could not delete folder");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return true;
    }
    
    /**
     * 
     * @param src
     * @param dst
     * @return 
     */
    public boolean updateFolder(String src, String dst) {
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
            changeChild(FIDrs.getString("ID"), updated[1], updated[0]);

        } catch (SQLException e) {

        }
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
        String kidsSQL = "SELECT ID, Name, Parent_Path FROM folders WHERE ParentFolder_ID = ?";
        String updateKid = "UPDATE folders SET Parent_Path = ? WHERE ParentFolder_ID = ?";
        try {
            PreparedStatement kidsQuery = connection.prepareStatement(kidsSQL);
            kidsQuery.setString(1, parentID);

            ResultSet kids = kidsQuery.executeQuery();//get all the children info

            int rowCount = kids.last() ? kids.getRow() : 0; //get number of kids
            if (rowCount <= 0) {
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
                changeChild(kids.getString("ID"), kids.getString("Name"), kids.getString("Parent_Path"));
            }
        } catch (SQLException e) {

        }

        return true;
    }
    
    /**
     * 
     * @param src
     * @param dst
     * @return 
     */
    public boolean moveFile(String src, String dst) {
        if (fileExists(dst)) {
            System.out.println("File already exists at location");
            return false;
        }
        String[] file = splitPath(dst);
        String[] parentFolder = splitPath(file[PATH]);
        String[] old = splitPath(src);
        String[] oldParent = splitPath(old[PATH]);
        String fileSQL = "UPDATE files SET ParentFolder_ID = (SELECT ID FROM folders WHERE folders.Name = ? AND folders.Parent_Path = ?) WHERE files.Name = ? AND files.ParentFolder_ID = (SELECT ID FROM folders WHERE folders.Name = ? AND folders.Parent_Path = ?);";
        try {
            PreparedStatement mvFile = connection.prepareStatement(fileSQL);
            mvFile.setString(1, parentFolder[NAME]);
            mvFile.setString(2, parentFolder[PATH]);
            mvFile.setString(3, file[NAME]);
            mvFile.setString(4, oldParent[NAME]);
            mvFile.setString(5, oldParent[PATH]);

            if (mvFile.executeUpdate() == 0) {
                System.out.println("Could not change file parent folder");
                return false;
            }

        } catch (SQLException e) {

        }
        return true;
    }
    
    /**
     * 
     * @param src
     * @param dst
     * @return 
     */
    public boolean changeFileName(String src, String dst) {
        if (fileExists(dst)) {
            System.out.println("File name already exists at location");
            return false;
        }
        String fileSQL = "UPDATE files SET files.Name = ? WHERE files.Name = ? AND files.ParentFolder_ID = (SELECT ID FROM folders WHERE folders.Name = ? AND folders.Parent_Path = ?);";
        String[] old = splitPath(src);
        String[] updated = splitPath(dst);
        String[] parent = splitPath(updated[PATH]);
        try {
            PreparedStatement updateName = connection.prepareStatement(fileSQL);
            updateName.setString(1, updated[NAME]);
            updateName.setString(2, old[NAME]);
            updateName.setString(3, parent[NAME]);
            updateName.setString(4, parent[PATH]);

            if (updateName.executeUpdate() == 0) {
                System.out.println("Could not change filename");
                return false;
            }
        } catch (SQLException e) {

        }
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

            if (delFile.executeUpdate() == 0) {
                System.out.println("Could not delete file.");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
    
    /**
     * 
     * @param filePath
     * @return 
     */
    public boolean newFile(String filePath) {
        if (fileExists(filePath)) {
            System.out.println("File with that name already exists at location");
            return false;
        }
        String[] file = splitPath(filePath);
        String[] parentFolder = splitPath(file[PATH]);
        //String fileQuery = "SELECT * FROM files INNER JOIN folders ON folders.ID = files.ParentFolder_ID WHERE folders.Name = ? AND folders.Parent_Path = ?;";
        String fileSQL = "INSERT INTO files (Name, Permissions_ID, ParentFolder_ID) VALUES (?, 1, (SELECT ID FROM folders WHERE folders.Name = ? AND folders.Parent_Path = ?));";
        try {
            /*PreparedStatement getFiles = connection.prepareStatement(fileQuery);
            getFiles.setString(1, parentFolder[NAME]);
            getFiles.setString(2, parentFolder[PATH]);
            ResultSet fileRS = getFiles.executeQuery();
            while (fileRS.next()) {
                if (file[NAME].equals(fileRS.getString("Name"))) {
                    System.out.println("Same name file in parent path");
                    return false;
                }
            }*/

            PreparedStatement nFile = connection.prepareStatement(fileSQL);
            nFile.setString(1, file[NAME]);
            nFile.setString(2, parentFolder[NAME]);
            nFile.setString(3, parentFolder[PATH]);

            if (nFile.executeUpdate() == 0) {
                System.out.println("Could not create new file");
                return false;
            }
        } catch (SQLException e) {

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
        String fileSQL = "SELECT files.Name FROM files INNER JOIN folders ON folders.ID = files.ParentFolder_ID WHERE folders.Name = ? AND folders.Parent_PATH = ?";
        String folderSQL = "SELECT Name FROM folders WHERE Parent_Path = ?";
        String[][] contents = null;
        int numData = 1;
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
            for (int i = 0; i < rowCount; i++) {
                if (folderRS.next()) {
                    contents[i][0] = folderRS.getString("Name");
                    contents[i][numData] = "Folder";
                } else if (fileRS.next()) {
                    contents[i][0] = fileRS.getString("Name");
                    contents[i][numData] = "File";
                } else {
                    System.out.println("Well fuck.");
                }
                /*for(int j = 0; j < numData; j++){
                        
                    }*/
            }
        } catch (SQLException e) {

        }
        return contents;
    }
    /**
     * 
     * @param username
     * @return 
     */
    public String[][] listRoot(String username) {
        String userSQL = "SELECT ID FROM users WHERE Username = ?";
        String[][] contents = null;
        try {
            PreparedStatement getUID = connection.prepareStatement(userSQL);
            getUID.setString(1, username);

            ResultSet UID = getUID.executeQuery();
            contents = list("/"+UID.getString("ID")+"/");

        } catch (SQLException e) {

        }
        return contents;
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
    public String getUserIDFromPath(String fullPath) {
        String[] path = splitPath(fullPath);
        while (!path[0].equals("/")) {
            path = splitPath(path[0]);
        }
        return path[1];
    }
    
    /**
     * 
     * @param path
     * @return 
     */
    public boolean fileExists(String path) {
        String fileSQL = "SELECT Name FROM files INNER JOIN folders ON folders.ID = files.ParentFolder_ID WHERE folders.Name = ? AND folders.Parent_PATH = ?;";
        String[] file = splitPath(path);
        String[] parentFolder = splitPath(file[PATH]);
        try {
            PreparedStatement getFiles = connection.prepareStatement(fileSQL);
            getFiles.setString(1, parentFolder[NAME]);
            getFiles.setString(2, parentFolder[PATH]);

            ResultSet fileRS = getFiles.executeQuery();
            return fileRS.next();

        } catch (SQLException e) {

        }

        return true;
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

        }
        return true;
    }

    /**
     * Disconnects from the database.
     */
    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {

            }
        }
    }

}