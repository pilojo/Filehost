/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.algonquincollege.waterbin.fs.fsAggregator;

import com.algonquincollege.waterbin.fs.tasks.FileSystemTask;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    
    public void addTask(FileSystemTask task){
        System.out.println("File System Aggregator: New Task Submitted");
        Future<FileSystemTask> ourFuture = (Future<FileSystemTask>) pool.submit(task);
        
        try {
            ourFuture.get();
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(FSAggregator.class.getName()).log(Level.SEVERE, "Task encountered abnormal conditions", ex);
        }
    }
    
    public void shutdown(){
        System.out.println("File System Aggregator: Shuting Down");
        pool.shutdown();
    }
    
    
    
}
