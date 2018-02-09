/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.algonquincollege.fileManager.javaApp.servlets;

import com.algonquincollege.fileManager.javaApp.JavaAppServlet;
import com.algonquincollege.javaApp.fileManager.commands.RM;
import javax.servlet.annotation.WebServlet;

/**
 *
 * @author Byzantian
 */
@WebServlet(name = "RemoveServlet", urlPatterns = {"/rm"})
public class RemoveServlet extends JavaAppServlet {

    @Override
    protected void decodeAndRun(String data){
        System.out.println("Remove Servlet");
        RM rm = new RM();
        if(rm.fromString(data))
        {
            System.out.println("Path: " + rm.getPath());
        }
    }
}
