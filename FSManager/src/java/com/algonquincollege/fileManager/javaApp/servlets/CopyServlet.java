/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.algonquincollege.fileManager.javaApp.servlets;

import com.algonquincollege.fileManager.javaApp.JavaAppServlet;
import com.algonquincollege.javaApp.fileManager.commands.*;
import javax.servlet.annotation.WebServlet;

/**
 *
 * @author Byzantian
 */
@WebServlet(name = "CopyServlet", urlPatterns = {"/cp"})
public class CopyServlet extends JavaAppServlet {

    @Override
    protected void decodeAndRun(String data){
        System.out.println("Copy Servlet");
        CP cp = new CP();
        if(cp.fromString(data))
        {
            System.out.println("From: " + cp.getFrom());
            System.out.println("To: " + cp.getTo());
        }
        
    }
}
