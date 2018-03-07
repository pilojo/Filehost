/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.algonquincollege.fileManager.fileSystemAggregator;

import com.algonquincollege.fileManager.fileSystemAggregator.tasks.FileSystemTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 *
 * @author Devon St. John
 */
public class FSAggregator {
    
    private final ExecutorService pool;
    
    public FSAggregator(){
        pool = Executors.newFixedThreadPool(8);
        System.out.println("File System Aggregator: Started");
    }
    
    public Future<FileSystemTask> addTask(FileSystemTask task){
        System.out.println("File System Aggregator: New Task Submitted");
        return (Future<FileSystemTask>) pool.submit(task);
    }
    
    public void shutdown(){
        System.out.println("File System Aggregator: Shuting Down");
        pool.shutdown();
    }
    
    
    
}
