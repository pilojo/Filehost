package com.algonquincollege.fileManager.javaApp.servlets;

import com.algonquincollege.fileManager.fileSystemAggregator.FSAggregator;
import com.algonquincollege.fileManager.fileSystemAggregator.tasks.MakeDirectory;
import com.algonquincollege.fileManager.javaApp.JavaAppServlet;
import com.algonquincollege.javaApp.fileManager.commands.MKDIR;
import java.util.Enumeration;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles connections to the management link intending to make a new directory
 *
 * @author Devon St. John
 */
public class MakeDirectoryServlet extends JavaAppServlet {

    /**
     * Attempts to parse in the given string and run it as a Make Directory command
     * @param data The data to be parsed
     * @return Whether the command succeeded.
     */
    @Override
    protected boolean decodeAndRun(String data){
        System.out.println("Make Directory Servlet: Started");//DEBUG STATEMENT, INFORM THE MAKE DIRECTORY SERVLET HAS BEEN CALLED
        MKDIR mkdir = new MKDIR();//Create a MKDIR Command Servlet to parse data with
        System.out.println("Make Directory Servlet: Data: " + data);
        if(mkdir.fromString(data))//Parse the data
        {//On success
                       
            FSAggregator aggregator = (FSAggregator)this.getServletContext().getAttribute("aggregator");
            
            Future ourFuture = aggregator.addTask(new MakeDirectory(mkdir.getPath()));
            
            try{
                ourFuture.get();
            } catch (InterruptedException | ExecutionException ex) {
                System.err.println("Make Directory Servlet: Aggregator Exception");
                return false;//Return the error to the caller
            }
            
            return true;//And report the sucess to the caller
        }
        System.err.println("Make Directory Servlet: INVALID REQUEST");
        return false;//Return the error to the caller
    }
}
