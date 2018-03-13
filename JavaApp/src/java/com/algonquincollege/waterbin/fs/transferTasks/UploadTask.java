/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.algonquincollege.waterbin.fs.transferTasks;

import com.algonquincollege.javaApp.database.DBConnection;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 *
 * @author Devon
 */
@MultipartConfig(
                fileSizeThreshold=1024*1024*2, // 2MB
                 maxFileSize=1024*1024*2,      // 500KB
                 maxRequestSize=1024*1024*50)   // 50MB
public class UploadTask extends TransferTask {

    DBConnection db;
    final String localPath;
    
    private boolean abnormalEnd;
    
    String fileName;
    int recommendedResponseCode;
    
    public UploadTask(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
        
        db = new DBConnection();
        System.out.println("Upload Task: Queued");
        localPath = request.getPathInfo();
        fileName = "";
    }

    @Override
    public void run() {
        System.out.println("Upload Task: Going Live");
        try {
            for (Part part : request.getParts()) {
                if(fileName.isEmpty()){
                    fileName = extractFileName(part);
                    fileName = new File(fileName).getName();
                    if(!fileName.matches("[A-Za-z\\.\\-0-9]+")) {
                        recommendedResponseCode = 403;
                        abnormalEnd = true;
                        break;
                    }
                }
                else {
                    String thisFileName = extractFileName(part);
                    thisFileName = new File(thisFileName).getName();

                    if (!thisFileName.equals(fileName)){
                        Logger.getLogger(UploadTask.class.getName()).log(Level.SEVERE, "ATTEMPT TO INSERT MULTIPLE FILES INTO UPLOAD! This upload is aborting NOW, all progress writen will be reverted!");
                        if(Files.exists(Paths.get(root, localPath, fileName)))
                            Files.delete(Paths.get(root, localPath, fileName));
                        recommendedResponseCode = 403;
                        abnormalEnd = true;
                        break;
                    }
                }
                part.write(Paths.get(root, localPath, fileName).toString());
                System.out.println("Upload Task: Wrote Part");
            }
        } catch (IOException ex) {
            Logger.getLogger(UploadTask.class.getName()).log(Level.SEVERE, "IO Exception during file upload", ex);
            recommendedResponseCode = 500;
            abnormalEnd = true;
            try {
                Files.delete(Paths.get(root, localPath, fileName));
            } catch (IOException e) {
                Logger.getLogger(UploadTask.class.getName()).log(Level.SEVERE, "Error in Attempt to delete Abandoned File", e);
            }
        } catch (ServletException ex) {
            Logger.getLogger(UploadTask.class.getName()).log(Level.SEVERE, "Servlet Threw an Exception", ex);
            recommendedResponseCode = 500;
            abnormalEnd = true;
            try {
                Files.delete(Paths.get(root, localPath, fileName));
            } catch (IOException e) {
                Logger.getLogger(UploadTask.class.getName()).log(Level.SEVERE, "Error in Attempt to delete Abandoned File", e);
            }
        }
        
        System.out.println("Upload Task: Ending");
        
        if(!abnormalEnd){
            System.out.println(localPath+"/"+fileName);
            if(db.connect() == null || !db.newFile(localPath+"/"+fileName)){
                try {
                    Files.delete(Paths.get(root, localPath, fileName));
                } catch (IOException ex) {
                    Logger.getLogger(UploadTask.class.getName()).log(Level.SEVERE, "Error in Attempt to delete Abandoned File", ex);
                }
                recommendedResponseCode = 500;
            }
            else{
                recommendedResponseCode = 202;
            }
        }
    }
    
    /**
     * Extracts file name from HTTP header content-disposition
     */
    private String extractFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");
        for (String s : items) {
            if (s.trim().startsWith("filename")) {
                return s.substring(s.indexOf("=") + 2, s.length() - 1);
            }
        }
        return "";
    }

    @Override
    public boolean getSuccess() {
        return Files.exists(Paths.get(root, localPath, fileName));
    }
    
    public int getRecommendedCode(){
        return recommendedResponseCode;
    }
}
