/*
Template for java application interface servlets
 */
package com.algonquincollege.fileManager.javaApp;

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Byzantian
 */
public abstract class JavaAppServlet extends HttpServlet {
    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        byte[] bytes = new byte[1];
        ArrayList<Byte> bindata = new ArrayList();
        while(-1 != request.getInputStream().read(bytes)){
            bindata.add(bytes[0]);
        }
        byte[] tmpdata = new byte[bindata.size()];
        for(int i = 0; i < bindata.size(); i++){
            tmpdata[i] = bindata.get(i);
        }
        decodeAndRun(new String(tmpdata));
        
    }
    
    /*Override this and decode the data to use how is needed*/
    protected abstract void decodeAndRun(String data);
}
