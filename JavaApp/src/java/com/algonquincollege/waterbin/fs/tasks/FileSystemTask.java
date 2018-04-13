package com.algonquincollege.waterbin.fs.tasks;
    
import com.algonquincollege.javaApp.database.DBConnection;

/**
 * Class representing a basic asyncronous copy file task
 * @author Devon St. John
 */
public abstract class FileSystemTask implements Runnable{

    protected final String root = "D:\\fileHostRoot"; //This is the path used to resolve relitive paths to absolute with

    protected DBConnection db;
    
    protected boolean abort;
    protected boolean success;
    
    /**
     * Get whether or not the operation was successful
     * @return whether or not the operation was successful
     */
    public abstract boolean getSuccess();
}
