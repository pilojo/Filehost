/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.algonquincollege.fileManager.javaApp.servlets;

import com.algonquincollege.fileManager.javaApp.JavaAppServlet;
import com.algonquincollege.javaApp.fileManager.commands.MKDIR;
import javax.servlet.annotation.WebServlet;

/**
 *
 * @author Byzantian
 */
@WebServlet(name = "MakeDirectoryServlet", urlPatterns = {"/mkdir"})
public class MakeDirectoryServlet extends JavaAppServlet {

    @Override
    protected void decodeAndRun(String data){
        System.out.println("Make Directory Servlet");
        MKDIR mkdir = new MKDIR();
        if(mkdir.fromString(data))
        {
            System.out.println("Path: " + mkdir.getPath());
        }
    }
}
