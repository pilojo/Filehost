package com.algonquincollege.waterbin.fs.tasks;

import com.algonquincollege.javaApp.database.DBConnection;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 *
 * @author Devon St. John
 */
public class MakeDirectory extends FileSystemTask{

    private final String path;
    
    
    public MakeDirectory(String path){
        this.path = path;
        
        db = new DBConnection();
        System.out.println("Make Directory Task: Launched to Aggregator");
    }
    
    @Override
    public void run() {
        System.out.println("Make Directory Task: " + path + " | Going Live");
        try{
            if(db.connect() != null){
                Files.createDirectory(Paths.get(root, path));
                if(!db.newFolder(path)){
                    Files.delete(Paths.get(root, path));
                    System.err.println("Make Directory Task Failed: DB connection issue: " + path);
                }
                System.out.println("Make Directory Task: Completed");
            }
        } catch (IOException ex) {
            System.err.println("Make Directory Task Failed: " + path);
        }
        
        
    }
    
    @Override
    public boolean getSuccess(){
        return Files.exists(Paths.get(root, path));
    }
    
}
