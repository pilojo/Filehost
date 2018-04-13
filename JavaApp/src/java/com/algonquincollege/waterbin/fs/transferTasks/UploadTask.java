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
                fileSizeThreshold=1024*10241024*2, // 2MB
                 maxFileSize=1024*1024*1024*2,      // 500KB
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
        int fileLength = Integer.parseInt(request.getHeader("content-length"));
        try{
            System.out.println((String)request.getSession().getAttribute("email") + localPath);
                System.out.println("HIT");

                    InputStream istream = request.getInputStream();
                    FileOutputStream file = new FileOutputStream(Paths.get(root, localPath).toString());
                    System.out.println(request.getHeader("content-length"));
                    byte[] bytes = new byte[fileLength];
                    byte[] temp = new byte[1];
                    for(int i = 0; (istream.read(temp) != -1); i++){
                        bytes[i] = temp[0];
                    }
                    file.write(bytes);
                    file.flush();
                    file.close();
                
        }catch(Exception e){
            
        }
        
        if(!abnormalEnd){
            System.out.println(localPath);
            if(db.connect() == null || !db.newFile(localPath, fileLength)){
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
