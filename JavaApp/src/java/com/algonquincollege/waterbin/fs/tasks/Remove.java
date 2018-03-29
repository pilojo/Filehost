/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.algonquincollege.waterbin.fs.tasks;

import com.algonquincollege.javaApp.database.DBConnection;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 *
 * @author Devon St. John
 */
public class Remove extends FileSystemTask {
    private final DBConnection db;
    private final String path;
    
    public Remove(String path){
        this.path = path;
        
        db = new DBConnection();
        System.out.println("Remove Task: Launched to Aggregator");
    }
    
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
    
    
    @Override
    public boolean getSuccess(){
        return !Files.exists(Paths.get(root, path));
    }
   
}
