/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.algonquincollege.javaApp.webhost.servlets;

import com.algonquincollege.javaApp.database.DBConnection;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
//import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 *
 * @author Devon St. John
 */

//@WebServlet("/UploadServlet")
@MultipartConfig(fileSizeThreshold=1024*1024*2, // 2MB
                 maxFileSize=1024*500,      // 500KB
                 maxRequestSize=1024*1024*50)   // 50MB
public class UploadServlet extends HttpServlet {
    /**
     * UID to satisfy Serializable interface
     */
    private static final long serialVersionUID = 5762973754703989733L;
    /**
     * Name of the directory where uploaded files will be saved, relative to
     * the web application directory.
     */
    private static final String SAVE_DIR = "D:\\fileHostRoot";
     
    @Override
    public void init(){
        //System.out.println("I was just created");
    }
    
    /**
     * handles file upload
     * @param request
     * @param response
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        // constructs path of the directory to save uploaded file
        DBConnection db = new DBConnection();
        String localPath = request.getPathInfo();
        
        String savePath = SAVE_DIR;
        
         String fileName = new String();
        for (Part part : request.getParts()) {
            
            fileName = extractFileName(part);
            if(!fileName.matches("[A-Za-z\\.\\-0-9]+")) {
                response.sendError(403);
                return;
            }
            // refines the fileName in case it is an absolute path1
            fileName = new File(fileName).getName();
            try{
                part.write(Paths.get(savePath, localPath, fileName).toString());
                
            }
            catch(IOException e){System.out.println("It goes here to die");}
        }
        if(db.connect()!=null){
            System.out.println(fileName + "\n" + localPath);
            System.out.println("/"+db.getUserIDFromUsername(db.getUserIDFromPath(localPath))+localPath.substring(localPath.indexOf("/",1), localPath.length()-1)+"/"+fileName);
            db.newFile("/"+db.getUserIDFromUsername(db.getUserIDFromPath(localPath))+localPath.substring(localPath.indexOf("/",1), localPath.length()-1)+"/"+fileName);
        }
        request.setAttribute("message", "Upload has been done successfully!");
        getServletContext().getRequestDispatcher("/status.jsp").forward(
                request, response);
    }
    /**
     * Extracts file name from HTTP header content-disposition
     */
    private String extractFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");
        for (String s : items) {
            if (s.trim().startsWith("filename")) {
                return s.substring(s.indexOf("=") + 2, s.length()-1);
            }
        }
        return "";
    }
}