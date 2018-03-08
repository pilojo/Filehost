/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.algonquincollege.javaApp.database;

import com.algonquincollege.javaApp.database.encryption.PasswordHashUtils;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Ale
 */

public class DBConnection {
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    private static final String URL = "jdbc:mysql://10.70.213.182:3306/filehostdb";
    private static final String USERNAME = "John";
    private static final String PASSWORD = "Password1";
    
    private Connection connection;
    
    public Connection connect(){
        if(connection == null){
            try{
                Class.forName(DRIVER).newInstance();
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            }catch( InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e){
                
                return null;
            }
        }
        return connection;
    }
    
    public boolean login(String email, String hash){
        System.out.println();
        PasswordHashUtils password = new PasswordHashUtils();
        String query = "select password from users where email = \"" + email + "\";";
        try{
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet result = statement.executeQuery();
            System.out.println("Executed Query");
            if (result.next()){
                System.out.println(hash + " " + result.getString("Password"));
                if(password.verifyHash(hash, result.getString("Password"))){

                    System.out.println("logino passed");
                    return true;
                }
                result.close();
            }
           
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return false;
    }
    
    /**
     * Create user and home folder
     * @param firstName
     * @param lastName
     * @param email
     * @param username
     * @param password
     * @return 
     */
    public boolean signUp(String firstName, String lastName, String email, String username, String password){
        System.out.println(firstName+" "+lastName);
        String createUser = "INSERT INTO users (Username, First_Name, Last_Name, Email,AccountType_ID, Password) VALUES ('"+username+"' ,'"+firstName+"' ,'"+lastName+"' ,'"+email+"' ,4,'"+PasswordHashUtils.hash(password)+"')";
        //String getUIDQuery = "SELECT ID FROM users WHERE Username = '"+username+"'";
        try{
            //Create the new user
            PreparedStatement userStmt = connection.prepareStatement(createUser, Statement.RETURN_GENERATED_KEYS);
            if(userStmt.executeUpdate() == 0){
                
                System.out.println("create user");
                return false;
            }
            ResultSet rs = userStmt.getGeneratedKeys();
            
            int UID = 0;
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
            
            //create new root folder
            String createRoot = "INSERT INTO folders (Name, Parent_Path, Owner_ID, Permission_ID) VALUES ('"+UID+"', '/', "+UID+", 1)";
            PreparedStatement folderStmt = connection.prepareStatement(createRoot, Statement.RETURN_GENERATED_KEYS);
            if(folderStmt.executeUpdate() == 0){
                System.out.println("create root");
                return false;
            }
               
            //get newly created folder id
            //String getFIDQuery = "SELECT ID FROM folders WHERE Owner_ID = " +UID;
            //PreparedStatement getFID = connection.prepareStatement(getFIDQuery, Statement.RETURN_GENERATED_KEYS);
            
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
            //set the newly created user's root folder to the newly created folder
            String linkRoot = "UPDATE users SET RootFolder_ID = "+FID+" WHERE ID = "+ UID;
            PreparedStatement linkStmt = connection.prepareStatement(linkRoot);
            
            if(linkStmt.executeUpdate()==0){
                System.out.println("Root folder could not be set.");
            }
            
            
        }catch(Exception e){e.printStackTrace();return false;}
        return true;
    }
    
    
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