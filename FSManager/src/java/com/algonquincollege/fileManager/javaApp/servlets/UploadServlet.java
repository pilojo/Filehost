package com.algonquincollege.fileManager.javaApp.servlets;

import java.io.File;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@MultipartConfig(fileSizeThreshold=1024*1024*2, // 2MB
                 maxFileSize=1024*1024*10,      // 10MB
                 maxRequestSize=1024*1024*50)   // 50MB
public class UploadServlet extends HttpServlet {
    /**
     * Name of the directory where uploaded files will be saved
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
        
        System.out.println("File Upload: Started");
        
        // constructs path of the directory to save uploaded file
        String savePath = SAVE_DIR;
        
        // creates the save directory if it does not exists
         
        for (Part part : request.getParts()) {
            System.out.println("File Upload: Uploading...");
            String fileName = extractFileName(part);
            // refines the fileName in case it is an absolute path
            fileName = new File(fileName).getName();
            try{part.write(savePath + File.separator + fileName);}catch(Exception e){System.out.println("erg");}
        }
        System.out.println("File Upload: Complete");
        response.sendError(HttpServletResponse.SC_ACCEPTED);//On success report to the client such with a 200
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