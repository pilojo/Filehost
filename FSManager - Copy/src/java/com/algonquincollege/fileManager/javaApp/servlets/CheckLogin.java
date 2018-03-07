/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.algonquincollege.fileManager.javaApp.servlets;

import java.io.PrintWriter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Devon
 */
public class CheckLogin extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response){
        System.out.println("STARTED");
        response.setContentType("text/json;charset=UTF-8");
        try(PrintWriter out = response.getWriter()){
            out.println("("+request.getSession().getAttribute("email"));
        }catch(Exception e){}
        System.out.println("CONTEXT: " + request.getSession().getAttribute("email"));
    }
}
