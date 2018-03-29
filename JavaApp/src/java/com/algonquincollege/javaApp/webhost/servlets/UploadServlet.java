/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.algonquincollege.javaApp.webhost.servlets;

import com.algonquincollege.javaApp.database.DBConnection;
import com.algonquincollege.javaApp.fileManager.utils.ByteReconstruct;
import com.algonquincollege.javaApp.utils.json.JSONParser;
import com.algonquincollege.waterbin.fs.fsAggregator.FSAggregator;
import com.algonquincollege.waterbin.fs.tasks.MakeDirectory;
import com.algonquincollege.waterbin.fs.transferAggregator.TransferAggregator;
import com.algonquincollege.waterbin.fs.transferTasks.UploadTask;
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
@MultipartConfig(
                fileSizeThreshold=1024*1024*2, // 2MB
                 maxFileSize=1024*1024*2,      // 2MB
                 maxRequestSize=1024*1024*50)   // 50MB
public class UploadServlet extends HttpServlet {
    
    private DBConnection db;
    
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
        
        System.out.println("Upload Servlet Called.");
        try{
            db = new DBConnection();
                if(db.connect() == null){
                    response.sendError(404);
                }else{
                    System.out.println(request.getPathInfo());
                    if(db.verifyOwner((String)request.getSession().getAttribute("email"), request.getPathInfo())){
                        TransferAggregator taggregate = (TransferAggregator)this.getServletContext().getAttribute("transfer aggregator");
                        UploadTask task = new UploadTask(request, response);
                        if(taggregate.addTask(task)){
                            
                            response.sendError(200);
                        }else{
                            
                            response.sendError(404);
                        }
                    }else{
                        response.sendError(404);
                    }
                }
        }catch(Exception IOException){

            System.out.println(IOException.getMessage());
            response.sendError(404);
        }

    }
}