/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.algonquincollege.waterbin.fs.transferAggregator;

import com.algonquincollege.waterbin.fs.transferTasks.*;
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
public class TransferAggregator {
    
    private final ExecutorService pool;
    
    public TransferAggregator(){
        pool = Executors.newFixedThreadPool(8);
        System.out.println("File Transfer Aggregator: Started");
    }
    
    public boolean addTask(TransferTask task){
        System.out.println("File Transfer Aggregator: New Task Submitted");
        Future<TransferTask> ourFuture = (Future<TransferTask>) pool.submit(task);
        
        
        
        try {
            ourFuture.get();
            return task.getSuccess();
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(TransferAggregator.class.getName()).log(Level.SEVERE, "Task encountered abnormal conditions", ex);
        }
        return false;
    }
    
    public void shutdown(){
        System.out.println("File Transfer Aggregator: Shuting Down");
        pool.shutdown();
    }
    
    
    
}
