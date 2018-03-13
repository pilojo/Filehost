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
 * @author Devon St John
 */
public class Move extends FileSystemTask {
    
    private final String source;
    private final String dest;
    
    public Move(String source, String dest){
        this.source = source;
        this.dest = dest;
        
        db = new DBConnection();
        
        System.out.println("Move Task: Launched to Aggregator");
    }
    
    @Override
    public void run() {
        System.out.println("Move Task: " + source + " To " + dest + " | Going Live");
        try{
            if(db.connect() != null){
                Files.move(Paths.get(root, source), Paths.get(root, dest));
                success = true;
                if(Files.isDirectory(Paths.get(root, dest))){
                    if(!db.updateFolder(source, dest)){
                        Files.move(Paths.get(root, dest), Paths.get(root, source));
                    }
                }else{
                    if(!db.updateFile(source, dest)){
                        Files.move(Paths.get(root, dest), Paths.get(root, source));
                    }
                }
            }
        } catch (IOException ex) {
            System.err.println("Move Failed: " + source + " To " + dest);
        }
        System.out.println("Move Task: Completed");
    }
   
    @Override
    public boolean getSuccess(){
        return Files.exists(Paths.get(root, dest));
    }
}
