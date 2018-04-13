package com.algonquincollege.waterbin.fs.transferTasks;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

    
/**
 * Abstract class representing a basic asyncronous transfer task
 * @author Devon St. John
 */
public abstract class TransferTask implements Runnable{
    
    protected final String root = "D:\\fileHostRoot"; //This is the path used to resolve relitive paths to absolute with
    
    protected HttpServletRequest request;
    protected HttpServletResponse response;
    
    /**
     * Initial Constructor, taking the calling servlet's request and response objects
     * @param the calling servlet's request object
     * @param the calling servlet's response object
     */
    public TransferTask(HttpServletRequest request, HttpServletResponse response){
        this.request = request;
        this.response = response;
    }

    /**
     * Get whether or not the operation was successful
     */
    public abstract boolean getSuccess();
}
