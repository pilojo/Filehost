/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.algonquincollege.fileManager.javaApp.servlets;

import com.algonquincollege.fileManager.javaApp.JavaAppServlet;
import com.algonquincollege.javaApp.fileManager.commands.MV;

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
            System.out.println("From: " + mv.getFrom());//DEBUG STATEMENT, REPORT THE PARSED SOURCE
            System.out.println("To: " + mv.getTo());//DEBUG STATEMENT, REPORT THE PARSED DESTINATION
            return true;//And report the sucess to the caller
        }
        return false;//Return the error to the caller
    }
}
