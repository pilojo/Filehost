package com.algonquincollege.fileManager.javaApp.servlets;

import com.algonquincollege.fileManager.javaApp.JavaAppServlet;
import com.algonquincollege.javaApp.fileManager.commands.MKDIR;

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
        System.out.println("Make Directory Servlet");//DEBUG STATEMENT, INFORM THE MAKE DIRECTORY SERVLET HAS BEEN CALLED
        MKDIR mkdir = new MKDIR();//Create a MKDIR Command Servlet to parse data with
        if(mkdir.fromString(data))//Parse the data
        {//On success
            System.out.println("Path: " + mkdir.getPath());//DEBUG STATEMENT, REPORT THE PARSED PATH
            return true;//And report the sucess to the caller
        }
        return false;//Return the error to the caller
    }
}
