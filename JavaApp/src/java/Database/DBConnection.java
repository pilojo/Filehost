/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author Ale
 */

public class DBConnection {
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/filehostdb";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    
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
