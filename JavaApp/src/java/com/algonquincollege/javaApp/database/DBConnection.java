
package com.algonquincollege.javaApp.database;

import com.algonquincollege.javaApp.database.encryption.PasswordHashUtils;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *Handles the connection with the Database and the Web Host. Handles any SQL the WebHost needs to perform
 * @author Alejandra Acosta
 */

public class DBConnection {
    private static final String DRIVER = "com.mysql.jdbc.Driver";//MySQL driver
    private static final String URL = "jdbc:mysql://localhost/filehostdb";//the database to use
    private static final String USERNAME = "root";
    private static final String PASSWORD = "60143088431472276664";
    
    private Connection connection; //connection object to the database, so that it can be re-used
    
    /**
     * Connects to the db
     * @return connection, returns connection object to the db
     */
    public Connection connect(){
        if(connection == null){//makes sure there isn't a connection already
            try{
                Class.forName(DRIVER).newInstance();//creates new instance for MySQL
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);//Connects to the db
            }catch( InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e){
                
            }
        }
        return connection;
    }
    /**
     * Makes sure the username and passward that the user entered match the one in the db
     * @param username, inputed username
     * @param hash, inputed hash password
     * @return returns true if the user entered the correct password, returns false if it doesn't match or an exception is thrown.
     */
    public boolean login(String username, String hash){
        PasswordHashUtils password = new PasswordHashUtils();
        String query = "select password from users where username = '" + username+"'";//get password from db
        System.out.println(username + "\n" + hash);//debugging purposes
        try{
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet result = statement.executeQuery();
            System.out.println("Executed Query");
            //verify the passwords match
            if(password.verifyHash(hash, result.getString("password"))){
                return true;
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return false;
    }
    
    /**
     * Create user and home folder
     * @param firstName, new user's first name
     * @param lastName, new user's last name
     * @param email, new user's email address, unique value
     * @param username, new user's username, unique value
     * @param password, new user's password
     * @return true if the account was created successfully, false if an exception was thrown
     */
    public boolean signUp(String firstName, String lastName, String email, String username, String password){
        System.out.println(firstName+" "+lastName);//debugging purposes
        //create a new users with inputed variables
        String createUser = "INSERT INTO users (Username, First_Name, Last_Name, Email,AccountType_ID, Password) VALUES ('"+username+"' ,'"+firstName+"' ,'"+lastName+"' ,'"+email+"' ,4,'"+password+"')";
        //String getUIDQuery = "SELECT ID FROM users WHERE Username = '"+username+"'";
        try{
            //Create the new user
            PreparedStatement userStmt = connection.prepareStatement(createUser, Statement.RETURN_GENERATED_KEYS);
            if(userStmt.executeUpdate() == 0){
                
                System.out.println("create user");
                return false;
            }
            //get the primary key of the newly generated user
            ResultSet rs = userStmt.getGeneratedKeys();
            
            int UID = 0;//stores the primary key of user
            if (rs.next()){
                UID=rs.getInt(1);
                rs.close();
            }
            else
            {
                rs.close();
                System.out.println("Hit 1");
                return false;
            }
            
            //create new root folder with the newly created user primary key for name and Owner_ID
            String createRoot = "INSERT INTO folders (Name, Parent_Path, Owner_ID, Permission_ID) VALUES ('"+UID+"', '/', "+UID+", 1)";
            PreparedStatement folderStmt = connection.prepareStatement(createRoot, Statement.RETURN_GENERATED_KEYS);
            if(folderStmt.executeUpdate() == 0){
                System.out.println("create root");
                return false;
            }
               
            //get newly created folder id
            //String getFIDQuery = "SELECT ID FROM folders WHERE Owner_ID = " +UID;
            //PreparedStatement getFID = connection.prepareStatement(getFIDQuery, Statement.RETURN_GENERATED_KEYS);
            
            //get primary key of newly created folder
            ResultSet rsFID = folderStmt.getGeneratedKeys();
            int FID = 0;
            if (rsFID.next()){
                FID=rsFID.getInt(1);
                rsFID.close();
            }
            else
            {
                rsFID.close();
                System.out.println("Hit 2");
                return false;
            }
            //set the newly created user's root folder as the newly created folder
            String linkRoot = "UPDATE users SET RootFolder_ID = "+FID+" WHERE ID = "+ UID;
            PreparedStatement linkStmt = connection.prepareStatement(linkRoot);
            
            if(linkStmt.executeUpdate()==0){
                System.out.println("Root folder could not be set.");
            }
            
            
        }catch(Exception e){e.printStackTrace();return false;}
        return true;
    }
    
    /**
     * disconnects the db from the WebHost
     */
    public void disconnect(){
        if(connection != null){
            try{
                connection.close();
                connection = null;
            }catch(SQLException e){
                
            }
        }
    }
        
}