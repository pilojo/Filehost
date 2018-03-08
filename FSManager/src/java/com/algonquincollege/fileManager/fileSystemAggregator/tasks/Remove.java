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
public class Remove extends FileSystemTask {
    
    private final String path;
    
    public Remove(String directory){
        path = directory;
        System.out.println("Remove Task: Launched to Aggregator");
    }
    
    @Override
    public void run() {
        System.out.println("Remove Task: " + path + " | Going Live");
        try{
            Files.delete(Paths.get(root, path));
        } catch (IOException ex) {
            System.err.println("Remove Failed: " + path);
            return;
        }
        System.out.println("Remove Task: Completed");
    }
    
}