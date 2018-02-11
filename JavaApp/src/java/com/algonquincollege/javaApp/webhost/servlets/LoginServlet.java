/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.algonquincollege.javaApp.webhost.servlets;

import com.algonquincollege.javaApp.webhost.WebInterfaceServlet;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Byzantian
 */
public class LoginServlet extends WebInterfaceServlet {
    
    @Override
    public String toString(HttpSession session) {
        session.setAttribute("username", "lel");
        return "\"logedin\":true";
    }
    
}
