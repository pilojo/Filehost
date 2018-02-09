/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.algonquincollege.javaApp.database;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Ale
 */

public class DBConnection {
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    private static final String URL = "jdbc:mysql://10.70.201.114:3306/filehostdb";
    private static final String USERNAME = "John";
    private static final String PASSWORD = "Password1";
    
    private Connection connection;
    
    public Connection connect(){
        if(connection == null){
            try{
                Class.forName(DRIVER).newInstance();
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            }catch( InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e){
                
            }
        }
        return connection;
    }
    
    public boolean login(String username, String hash){
        String query = "select * from users where username = \"" + username + "\" AND password = \"" + hash + "\"";
        System.out.println(username + "\n" + hash);
        try{
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet result = statement.executeQuery();
            System.out.println("Executed Query");
            
            if(result != null) return true;
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return false;
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