package com.algonquincollege.waterbin.fs.tasks;

import com.algonquincollege.javaApp.database.DBConnection;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 *
 * @author Devon St. John
 */
public class Home extends FileSystemTask{

    private final String path;
    
    
    public Home(String path){
        this.path = path;
        
        db = new DBConnection();
        System.out.println("Make Home Directory Task: Launched to Aggregator");
    }
    
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
    
    @Override
    public boolean getSuccess(){
        return Files.exists(Paths.get(root, path));
    }
    
}
