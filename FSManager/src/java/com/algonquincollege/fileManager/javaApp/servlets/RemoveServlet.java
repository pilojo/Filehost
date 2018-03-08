package com.algonquincollege.fileManager.javaApp.servlets;

import com.algonquincollege.fileManager.javaApp.JavaAppServlet;
import com.algonquincollege.javaApp.fileManager.commands.RM;
//import javax.servlet.annotation.WebServlet;

/**
 * Handles connections to the management link intending to remove a file or directory
 *
 * @author Devon St. John
 */
public class RemoveServlet extends JavaAppServlet {

    /**
     * Attempts to parse in the given string and run it as a Remove command
     * @param data The data to be parsed
     * @return Whether the command succeeded.
     */
    @Override
    protected boolean decodeAndRun(String data){
        System.out.println("Remove Servlet");
        RM rm = new RM();//Create a RM Command Servlet to parse data with
        if(rm.fromString(data))//Parse the data
        {//On success
            System.out.println("Path: " + rm.getPath());//DEBUG STATEMENT, REPORT THE PARSED PATH
            return true;//And report the sucess to the caller
        }
        return false;//Return the error to the caller
    }
}
