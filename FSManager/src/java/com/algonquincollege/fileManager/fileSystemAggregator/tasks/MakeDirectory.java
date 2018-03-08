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

<<<<<<< HEAD
    private final MKDIR task;
=======
    private String path;
>>>>>>> parent of d59e2b3... Initial code behind all file opperations
    
    private final ObjectOutputStream stream;
    
    public MakeDirectory(MKDIR task, ObjectOutputStream stream){
        this.task = task;
        this.stream = stream;
        
        System.out.println("Make Directory Task: Launched to Aggregator");
    }
    
    @Override
    public void run() {
<<<<<<< HEAD
        System.out.println("Make Directory Task: " + task.getPath() + " | Going Live");
=======
        System.out.println("Make Directory Task: Going Live");
>>>>>>> parent of d59e2b3... Initial code behind all file opperations
        try{
            Files.createDirectory(Paths.get(root, task.getPath()));
        } catch (IOException ex) {
            System.err.println("Make Directory Task Failed: " + task.getPath());
            return;
        }
        System.out.println("Make Directory Task: Completed");
    }
    
}
