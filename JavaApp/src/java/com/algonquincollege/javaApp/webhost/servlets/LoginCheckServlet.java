/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.algonquincollege.javaApp.webhost.servlets;

import com.algonquincollege.javaApp.webhost.WebInterfaceServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author John Pilon
 * To be fully implemented later. Sessions not yet working.
 */
public class LoginCheckServlet extends WebInterfaceServlet {

    @Override
    public String toString(HttpServletRequest request) {
        System.out.println(request.getSession().getAttribute("email")!=null);
        return request.getSession().getAttribute("email")!=null?"\"logedin\":true":"\"logedin\":false";
    }
    
}
