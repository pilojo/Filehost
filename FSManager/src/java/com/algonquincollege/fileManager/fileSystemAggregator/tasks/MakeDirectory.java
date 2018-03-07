package com.algonquincollege.fileManager.fileSystemAggregator.tasks;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 *
 * @author Devon St. John
 */
public class MakeDirectory extends FileSystemTask{

    private final String path;
    
    public MakeDirectory(String directory){
        path = directory;
        System.out.println("Make Directory Task: Launched to Aggregator");
    }
    
    @Override
    public void run() {
        System.out.println("Make Directory Task: " + path + " | Going Live");
        try{
            Files.createDirectory(Paths.get(root, path));
        } catch (IOException ex) {
            System.err.println("Make Directory Task Failed: " + path);
            return;
        }
        System.out.println("Make Directory Task: Completed");
    }
    
}
