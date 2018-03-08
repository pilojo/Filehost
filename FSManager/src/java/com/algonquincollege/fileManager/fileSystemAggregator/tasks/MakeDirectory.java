package com.algonquincollege.fileManager.fileSystemAggregator.tasks;

import com.algonquincollege.javaApp.fileManager.commands.MKDIR;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 *
 * @author Devon St. John
 */
public class MakeDirectory extends FileSystemTask{

    private final MKDIR task;
    
    private final ObjectOutputStream stream;
    
    public MakeDirectory(MKDIR task, ObjectOutputStream stream){
        this.task = task;
        this.stream = stream;
        
        System.out.println("Make Directory Task: Launched to Aggregator");
    }
    
    @Override
    public void run() {
        System.out.println("Make Directory Task: " + task.getPath() + " | Going Live");
        try{
            Files.createDirectory(Paths.get(root, task.getPath()));
        } catch (IOException ex) {
            System.err.println("Make Directory Task Failed: " + task.getPath());
            return;
        }
        System.out.println("Make Directory Task: Completed");
    }
    
}
