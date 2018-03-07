package com.algonquincollege.fileManager.javaApp.servlets;

import com.algonquincollege.fileManager.fileSystemAggregator.FSAggregator;
import com.algonquincollege.fileManager.fileSystemAggregator.tasks.Move;
import com.algonquincollege.fileManager.javaApp.JavaAppServlet;
import com.algonquincollege.javaApp.fileManager.commands.MV;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Handles connections to the management link intending to move a file to a new destination
 *
 * @author Devon St. John
 */
public class MoveServlet extends JavaAppServlet {

    /**
     * Attempts to parse in the given string and run it as a Move command
     * @param data The data to be parsed
     * @return Whether the command succeeded.
     */
    @Override
    protected boolean decodeAndRun(String data){
        System.out.println("Move Servlet");
        MV mv = new MV();//Create a MV Command Servlet to parse data with
        if(mv.fromString(data))//Parse the data
        {//On success
            FSAggregator aggregator = (FSAggregator)this.getServletContext().getAttribute("aggregator");
            
            Future ourFuture = aggregator.addTask(new Move(mv.getFrom(),mv.getTo()));
            
            try{
                ourFuture.get();
            } catch (InterruptedException | ExecutionException ex) {
                System.err.println("Move Servlet: Aggregator Exception");
                return false;//Return the error to the caller
            }
            return true;//And report the sucess to the caller
        }
        return false;//Return the error to the caller
    }
}
