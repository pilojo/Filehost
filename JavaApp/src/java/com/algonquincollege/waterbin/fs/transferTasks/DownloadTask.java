package com.algonquincollege.waterbin.fs.transferTasks;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Class representing the asyncronous handling of a client download task
 * @author Devon St. John
 */
public class DownloadTask extends TransferTask{
    
    /**
     * Initial Constructor, taking the calling servlet's request and response objects
     * @param the calling servlet's request object
     * @param the calling servlet's response object
     */
    public DownloadTask(HttpServletRequest request, HttpServletResponse response){
        super(request, response);
        
        System.out.println("Download Task: Queued");  
    }
    
    /**
     * Handles all the logic of the download operation, can be called as part of an executor service
     */
    @Override
    public void run(){
        System.out.println("Starting download...");
        
        String requestedFile = request.getPathInfo();
        
        System.out.println(requestedFile);

        try{
            // Check if file is actually supplied to the request URI.
            if (requestedFile == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
                return;
            }

            String filepath = URLDecoder.decode(requestedFile, "UTF-8");
            System.out.println(filepath);   

            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            response.setContentType("APPLICATION/OCTET-STREAM");
            String[] paths = requestedFile.split("/");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + paths[paths.length - 1]);

            FileInputStream fileInputStream = new FileInputStream(Paths.get(root, filepath).toString());

            int i;
            while ((i = fileInputStream.read()) != -1) {
                out.write(i);
            }
            fileInputStream.close();
            out.close();
            
        } catch (IOException ex) {
            Logger.getLogger(DownloadTask.class.getName()).log(Level.SEVERE, "Download Task, IO Exception During Download", ex);
        }
    }

    /**
     * Get whether or not the operation was successful
     * @return whether or not the operation was successful
     */
    @Override
    public boolean getSuccess() {
        return true;
    }
}
