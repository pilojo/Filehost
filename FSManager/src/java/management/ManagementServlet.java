/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package management;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import serializable.commands.CP;

/**
 *
 * @author Devon
 */
public class ManagementServlet extends HttpServlet {

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
        System.out.println("Post Started");
        String requestedFile = request.getPathInfo();

        // Check if file is actually supplied to the request URI.
        if (requestedFile == null) {
            // Do your thing if the file is not supplied to the request URI.
            // Throw an exception, or send 404, or show default/warning page, or just ignore it.
            response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
            return;
        }

        String commandpath = URLDecoder.decode(requestedFile, "UTF-8");
        
        byte[] bytes = new byte[1];
        ArrayList<Byte> bindata = new ArrayList();
        while(-1 != request.getInputStream().read(bytes)){
            bindata.add(bytes[0]);
        }
        byte[] tmpdata = new byte[bindata.size()];
        for(int i = 0; i < bindata.size(); i++){
            tmpdata[i] = bindata.get(i);
        }
        String data = new String(tmpdata);
        
        switch (commandpath){
            case "/cp":
                    CP cp = new CP();
                    cp.fromString(data);
                    
                    System.out.println("From: " + cp.getFrom());
                    System.out.println("To: " + cp.getTo());
                    System.out.println("Password:" + cp.getPassword());
                return;
            case "rm":
                
                return;
            case "mv":
                
                return;
            case "mkdir":
                
                return;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
