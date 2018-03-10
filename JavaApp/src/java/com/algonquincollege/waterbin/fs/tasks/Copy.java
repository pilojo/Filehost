/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.algonquincollege.waterbin.fs.tasks;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 *
 * @author Devon
 */
public class Copy extends FileSystemTask {
    
    private final String source;
    private final String dest;
    
    public Copy(String source, String dest){
        this.source = source;
        this.dest = dest;
        
        System.out.println("Copy Task: Launched to Aggregator");
    }
    
    @Override
    public void run() {
        System.out.println("Copy Task: | Going Live");
        try{
            Files.copy(Paths.get(root, source), Paths.get(root, dest));
            success = true;
        } catch (IOException ex) {
            System.err.println("Copy Failed: " + source + " To " + dest);
        }
    }
    
    @Override
    public boolean getSuccess(){
        return Files.exists(Paths.get(root, dest));
    }
    
}
