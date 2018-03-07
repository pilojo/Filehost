/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.algonquincollege.fileManager.fileSystemAggregator.tasks;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 *
 * @author Devon
 */
public class Move extends FileSystemTask {
    private final String source;
    private final String dest;
    
    public Move(String source, String dest){
        this.source = source;
        this.dest = dest;
        
        System.out.println("Move Task: Launched to Aggregator");
    }
    
    @Override
    public void run() {
        System.out.println("Move Task: " + source + " To " + dest + " | Going Live");
        try{
            Files.copy(Paths.get(root, source), Paths.get(root, dest));
        } catch (IOException ex) {
            System.err.println("Move Failed: " + source + " To " + dest);
            return;
        }
        System.out.println("Move Task: Completed");
    }
   
}
