package com.algonquincollege.waterbin.fs.tasks;

import com.algonquincollege.javaApp.database.DBConnection;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Class representing a basic asyncronous remove file task
 * @author Devon St. John
 */
public class Remove extends FileSystemTask {
    private final DBConnection db;
    private final String path;
    
    /**
     * Initial Constructor for the copy task
     * @param the file to copy from
     * @param the file to copy to
     */
    public Remove(String path){
        this.path = path;
        
        db = new DBConnection();
        System.out.println("Remove Task: Launched to Aggregator");
    }
    
    /**
     * Handles all the logic of the remove operation, can be called as part of an executor service
     */
    @Override
    public void run() {
        System.out.println("Remove Task: " + path + " | Going Live");
        try{
            if(db.connect() != null){
                boolean wasFolder = Files.isDirectory(Paths.get(root, path));

                Files.delete(Paths.get(root, path));
                if(wasFolder){
                    if(db.deleteFolder(path)){
                        success = true;
                    }else success = false;
                }else{
                    if(db.deleteFile(path)){
                        success = true;
                    }else success = false;
                }
            }
        } catch (IOException ex) {
            System.err.println("Remove Failed: " + path);
        }
        System.out.println("Remove Task: Completed");
    }
    
    /**
     * Get whether or not the operation was successful
     * @return whether or not the operation was successful
     */
    @Override
    public boolean getSuccess(){
        return !Files.exists(Paths.get(root, path));
    }
   
}
