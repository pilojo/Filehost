package com.algonquincollege.fileManager.javaApp.servlets;

import com.algonquincollege.fileManager.javaApp.JavaAppServlet;
import com.algonquincollege.javaApp.fileManager.commands.*;

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
            System.out.println("From: " + cp.getFrom());//DEBUG STATEMENT, REPORT THE PARSED SOURCE
            System.out.println("To: " + cp.getTo());//DEBUG STATEMENT, REPORT THE PARSED DESTINATION
            return true;//And report the sucess to the caller
        }
        return false;//Return the error to the caller
    }
}
