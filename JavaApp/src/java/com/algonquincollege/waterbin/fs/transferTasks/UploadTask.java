/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.algonquincollege.waterbin.fs.transferTasks;

import com.algonquincollege.javaApp.database.DBConnection;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
    
    private String fileName;
    int recommendedResponseCode;
    
    public UploadTask(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
        
        db = new DBConnection();
        System.out.println("Upload Task: Queued");
        localPath = request.getPathInfo();
        fileName = localPath.substring(localPath.lastIndexOf("/"));
    }

    @Override
    public void run() {
        System.out.println("Upload Task: Going Live");
        try{
            if(db.canUserAccessFolder((String)request.getSession().getAttribute("username"), localPath.substring(0, localPath.lastIndexOf("/")))){
                int counter = 1024*1024*5;
                if(!fileName.matches("\\/[A-Za-z0-9\\_]+(\\.?[A-Za-z0-9\\_]+)*")){
                    counter = -1;
                    abnormalEnd = true;
                    System.out.println(fileName + "Invalid File Name");
                }else{
                    InputStream istream = request.getInputStream();
                    FileOutputStream file = new FileOutputStream(Paths.get(root, localPath).toString());

                    byte[] bytes = new byte[counter];
                    if(istream.read(bytes) != -1){
                       if(istream.read(bytes) != -1){ 
                           file.flush();
                           file.close();
                           Files.delete(Paths.get(root, localPath));
                           abnormalEnd = true;
                       }

                    }
                    file.write(bytes);
                    file.flush();
                    file.close();
                }
            }
        }catch(Exception e){
        }
        
        if(!abnormalEnd){
            System.out.println(localPath);
            if(db.connect() == null || !db.newFile(localPath, 123)){
                try {
                    Files.delete(Paths.get(root, localPath));
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

    @Override
    public boolean getSuccess() {
        return Files.exists(Paths.get(root, localPath));
    }
    
    public int getRecommendedCode(){
        return recommendedResponseCode;
    }
}
