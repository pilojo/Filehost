package com.algonquincollege.waterbin.fs.tasks;

import com.algonquincollege.javaApp.database.DBConnection;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Class representing a basic asyncronous copy file task
 * @author Devon St. John
 */
public class Copy extends FileSystemTask {
    
    private final String source;
    private final String dest;
    
    /**
     * Initial Constructor for the copy task
     * @param the file to copy from
     * @param the file to copy to
     */
    public Copy(String source, String dest){
        this.source = source;
        this.dest = dest;
        
        db = new DBConnection();
        
        System.out.println("Move Task: Launched to Aggregator");
    }
    
    /**
     * Handles all the logic of the move operation, can be called as part of an executor service
     */
    @Override
    public void run() {
        System.out.println("Copy Task: " + source + " To " + dest + " | Going Live");
        try{
            if(db.connect() != null){
                Files.copy(Paths.get(root, source), Paths.get(root, dest));
                success = true;
                if(Files.isDirectory(Paths.get(root, dest))){
                    if(!db.newFolder(dest)){
                        Files.delete(Paths.get(root, dest));
                    }
                }else{
                    if(!db.newFile(dest, 123)){
                        Files.delete(Paths.get(root, dest));
                    }
                }
            }
        } catch (IOException ex) {
            System.err.println("Copy Failed: " + source + " To " + dest);
        }
        System.out.println("Copy Task: Completed");
    }
   
    /**
     * Get whether or not the operation was successful
     * @return whether or not the operation was successful
     */
    @Override
    public boolean getSuccess(){
        return Files.exists(Paths.get(root, dest));
    }
}
