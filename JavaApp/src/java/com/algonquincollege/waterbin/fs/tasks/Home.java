package com.algonquincollege.waterbin.fs.tasks;

import com.algonquincollege.javaApp.database.DBConnection;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Class representing an asyncronous creation of a home directory. This has no DB effects
 * @author Devon St. John
 */
public class Home extends FileSystemTask{

    private final String path;
    
    /**
     * Initial Constructor for the home task
     * @param the path to create the directory at
     */
    public Home(String path){
        this.path = path;
        
        db = new DBConnection();
        System.out.println("Make Home Directory Task: Launched to Aggregator");
    }
    
    /**
     * Handles all the logic of the home operation, can be called as part of an executor service
     */
    @Override
    public void run() {
        System.out.println("Make Home Directory Task: " + path + " | Going Live");
        try{
            if(db.connect() != null){
                Files.createDirectory(Paths.get(root, path));
            }
        } catch (IOException ex) {
            System.err.println("Make Home Directory Task Failed: " + path);
        }
        System.out.println("Make Home Directory Task: Completed");
        
    }
    
    /**
     * Get whether or not the operation was successful
     * @return whether or not the directory was created
     */
    @Override
    public boolean getSuccess(){
        return Files.exists(Paths.get(root, path));
    }
    
}
