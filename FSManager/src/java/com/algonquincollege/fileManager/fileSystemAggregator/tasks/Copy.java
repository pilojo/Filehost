/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.algonquincollege.fileManager.fileSystemAggregator.tasks;

import com.algonquincollege.javaApp.fileManager.commands.CP;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 *
 * @author Devon
 */
public class Copy extends FileSystemTask {
    private final ObjectOutputStream stream;
    
    private final CP task;
    
    public Copy(CP task, ObjectOutputStream stream){
        this.task = task;
        this.stream = stream;
        
        System.out.println("Copy Task: Launched to Aggregator");
    }
    
    @Override
    public void run() {
        System.out.println("Copy Task: | Going Live");
        /*try{
            //Files.copy(Paths.get(root, source), Paths.get(root, dest));
        } catch (IOException ex) {
            System.err.println("Copy Failed: " + source + " To " + dest);
            return;
        }*/
        System.out.println("Copy Task: Completed");
    }
    
}
