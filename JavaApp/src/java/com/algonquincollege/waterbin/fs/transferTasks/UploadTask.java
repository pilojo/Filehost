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
 * Class representing the asyncronous handling of a client upload task
 * @author Devon St. John
 */
public class UploadTask extends TransferTask {
  
    DBConnection db;
    final String localPath; 
    
    private boolean abnormalEnd;
    
    private String fileName;
    int recommendedResponseCode;
    
    /**
     * Initial Constructor, taking the calling servlet's request and response objects
     * @param the calling servlet's request object
     * @param the calling servlet's response object
     */
    public UploadTask(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
        
        db = new DBConnection();
        System.out.println("Upload Task: Queued");
        localPath = request.getPathInfo();
        fileName = localPath.substring(localPath.lastIndexOf("/"));
    }

    /**
     * Handles all the logic of the upload operation, can be called as part of an executor service
     */
    @Override
    public void run() {
        System.out.println("Upload Task: Going Live");
        int fileLength = Integer.parseInt(request.getHeader("content-length"));//Get the file length from the mime header
      
        try{
            System.out.println((String)request.getSession().getAttribute("email") + localPath);
                    InputStream istream = request.getInputStream();
                    FileOutputStream file = new FileOutputStream(Paths.get(root, localPath).toString());
                    System.out.println(request.getHeader("content-length"));
                    //Read in the data, byte by byte
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
        
        if(!abnormalEnd){//If all went well, commit this change to the database
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

    /**
     * Get whether or not the operation was successful
     * @param whether the file was created
     */
    @Override
    public boolean getSuccess() {
        return Files.exists(Paths.get(root, localPath));
    }
  
    /**
     * Gets the code that was generated during run in response to the tranfer operation
     * @return the code recommended to return to the client
     */
    public int getRecommendedCode(){
        return recommendedResponseCode;
    }
}
