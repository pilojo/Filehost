/*
The base parent of all json returning servlets for front end
*/
package com.algonquincollege.javaApp.webhost;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
/**
 *
 * @author Byzantian
 */
public abstract class WebInterfaceServlet extends HttpServlet {
    /**
     * Sends JSON to a client that connects to any children servlets. Ensure toString is overriden
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/json;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("{"+this.toString(request)+"}");
        }
    }
    
    public abstract String toString(HttpServletRequest session);
}
