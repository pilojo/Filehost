package com.algonquincollege.fileManager.javaApp;

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Abstract Template class used as a base for the Management Servlets
 *
 * @author Devon St. John
 */
public abstract class JavaAppServlet extends HttpServlet {
    
    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response, 200 on valid and verified command, 404
     *  otherwise
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        //Extract the data from the post request, which SHOULD be from the
        //Webhost
        
        byte[] bytes = new byte[1];//Array to store the read byte
        ArrayList<Byte> bindata = new ArrayList();//Arraylist of bytes used to Store the binary representation of the data string
        
        while(-1 != request.getInputStream().read(bytes)){//While there is still data in the stream
            bindata.add(bytes[0]);//Add it to the arraylist
        }
        
        byte[] tmpdata = new byte[bindata.size()];//Now that we have all the data and know the length, we need it back in array form for later
        
        for(int i = 0; i < bindata.size(); i++){//While the arraylist still has bytes
            tmpdata[i] = bindata.get(i);//Move them into the array
        }
        
        if (decodeAndRun(new String(tmpdata))){//Convert the bytes array to a st
            response.sendError(HttpServletResponse.SC_ACCEPTED);//On success report to the webhost such with a 200
        }
        else{
            response.sendError(HttpServletResponse.SC_NOT_FOUND);//On fail, assume this is an attack, hide the management link's exisitance with a 404
        }
        
    }
    
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response, Always 404
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
        
    }
    
    /**
     * Abstract Template Method used for parsing commands
     * 
     * @param data the string to be parsed
     * @return true on success, false if an error was found while parsing
     */
    protected abstract boolean decodeAndRun(String data);
}
