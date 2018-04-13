package com.algonquincollege.waterbin.fs.transferAggregator;

import com.algonquincollege.waterbin.fs.transferTasks.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

/** 
 * This class serves as the framework for a multi-threaded executor pool for upload and download tasks
 *
 * @author Devon St. John
 */
public class TransferAggregator {
    
    //The executor thread pool itself
    private final ExecutorService pool;
    
    /**Constructor for the aggregator, initializes the thread pool*/
    public TransferAggregator(){
        //a fixed thread pool is used to prevent overloading the host
        pool = Executors.newFixedThreadPool(8);
        System.out.println("File Transfer Aggregator: Started");
    }
    
    /**
     * Add a new transfer task to the executor pool, if no threads are avaliable,
     * the task will be queued, and ran when a thread becomes available. This is
     * a blocking function
     * 
     * @param task the task to execute
     * @return whether the task succeeded
     */
    public boolean addTask(TransferTask task){
        System.out.println("File Transfer Aggregator: New Task Submitted");
        //Commit the task to the executor pool and get a listener for it
        Future<TransferTask> ourFuture = (Future<TransferTask>) pool.submit(task);
        
        try {
            //Wait for the task to complete
            ourFuture.get();
            //return the status of the task
            return task.getSuccess();
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(TransferAggregator.class.getName()).log(Level.SEVERE, "Task encountered abnormal conditions", ex);
        }
        //if the task encountered an abnormal contition, it is assumed to have failed
        return false;
    }
    
    /**
     * Called to inform the executor that it must stop accepting tasks and finish
     * all committed tasks.
     */
    public void shutdown(){
        System.out.println("File Transfer Aggregator: Shuting Down");
        //Shutdown the executor pool to stop new tasks from being added
        //Also finishes all tasks already submitted
        pool.shutdown();
    }
}
