/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.algonquincollege.fileManager.javaApp.servlets;

import com.algonquincollege.fileManager.javaApp.JavaAppServlet;
import com.algonquincollege.javaApp.fileManager.commands.MV;
import javax.servlet.annotation.WebServlet;

/**
 *
 * @author Byzantian
 */
public class MoveServlet extends JavaAppServlet {

    @Override
    protected void decodeAndRun(String data){
        System.out.println("Move Servlet");
        MV mv = new MV();
        if(mv.fromString(data))
        {
            System.out.println("From: " + mv.getFrom());
            System.out.println("To: " + mv.getTo());
        }
    }
}
