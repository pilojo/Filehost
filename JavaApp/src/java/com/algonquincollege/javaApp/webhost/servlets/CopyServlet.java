/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.algonquincollege.javaApp.webhost.servlets;

import com.algonquincollege.javaApp.webhost.WebInterfaceServlet;
import javax.servlet.annotation.WebServlet;

/**
 *
 * @author Byzantian
 */
@WebServlet(name = "CopyServlet", urlPatterns = {"/server/copy"})
public class CopyServlet extends WebInterfaceServlet {

    @Override
    public String toString() {
        return "";
    }
    
}
