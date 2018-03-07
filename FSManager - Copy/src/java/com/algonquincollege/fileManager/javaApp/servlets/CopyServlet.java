package com.algonquincollege.fileManager.javaApp.servlets;

import com.algonquincollege.fileManager.fileSystemAggregator.FSAggregator;
import com.algonquincollege.fileManager.fileSystemAggregator.tasks.Copy;
import com.algonquincollege.fileManager.javaApp.JavaAppServlet;
import com.algonquincollege.javaApp.fileManager.commands.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Handles connections to the management link intending to copy one file to
 * a new destination
 *
 * @author Devon St. John
 */
public class CopyServlet extends JavaAppServlet {

    /**
     * Attempts to parse in the given string and run it as a copy command
     * @param data The data to be parsed
     * @return Whether the command succeeded.
     */
    @Override
    protected boolean decodeAndRun(String data){
        System.out.println("Copy Servlet");//DEBUG STATEMENT, INFORM THE COPY SERVLET HAS BEEN CALLED
        CP cp = new CP();//Create a CP Command Servlet to parse data with
        if(cp.fromString(data))//Parse the data
        {//On success
            FSAggregator aggregator = (FSAggregator)this.getServletContext().getAttribute("aggregator");
            
            Future ourFuture = aggregator.addTask(new Copy(cp.getFrom(),cp.getTo()));
                
            try{
                ourFuture.get();
            } catch (InterruptedException | ExecutionException ex) {
                System.err.println("Copy Servlet: Aggregator Exception");
                return false;//Return the error to the caller
            }
            
            return true;//And report the sucess to the caller
        }
        return false;//Return the error to the caller
    }
}
