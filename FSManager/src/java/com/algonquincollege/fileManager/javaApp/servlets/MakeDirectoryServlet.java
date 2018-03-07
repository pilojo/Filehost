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
            System.out.println("Make Directory Servlet: Path: " + mkdir.getPath());//DEBUG STATEMENT, REPORT THE PARSED PATH
            
            Future ourFuture = null;
            
            for (Enumeration<String> e = this.getServletContext().getAttributeNames(); e.hasMoreElements();)
                System.out.println("Make Directory: " + e.nextElement());
            
            Object aggregator = this.getServletContext().getAttribute("aggregator");
            if(aggregator != null){
                ourFuture = ((FSAggregator)aggregator).addTask(new MakeDirectory(mkdir.getPath()));
                //System.out.println("Make Directory Servlet: ");
            }
            else{
                System.out.println("Make Directory Servlet: Aggregator Failiure");
            }
            
            try{
                ourFuture.get();
            } catch (InterruptedException | ExecutionException ex) {
                System.out.println("Make Directory Servlet: Aggregator Exception");
            }
            
            System.out.println("Make Directory Servlet: Complete");//DEBUG STATEMENT, REPORT COMPLETE
            return true;//And report the sucess to the caller
        }
        System.err.println("Make Directory Servlet: INVALID REQUEST");
        return false;//Return the error to the caller
    }
}
