/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.algonquincollege.javaApp.webhost.servlets;

import com.algonquincollege.javaApp.database.DBConnection;
import com.algonquincollege.waterbin.fs.transferAggregator.TransferAggregator;
import com.algonquincollege.waterbin.fs.transferTasks.DownloadTask;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 *
 * @author Devon St. John
 */


public class DownloadServlet extends HttpServlet {

    /**
     * UID to satisfy Serializable interface
     */
    private static final long serialVersionUID = 8321120808388866663L;
    private DBConnection db;
    /*@Override
    public void init() {
    }*/

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Download Servlet Called.");
        db = new DBConnection();
        try{
            if(db.connect() == null){
                System.out.println("HIT");
                response.sendError(404);
            }else{
                TransferAggregator taggregate = (TransferAggregator)this.getServletContext().getAttribute("transfer aggregator");
                DownloadTask task = new DownloadTask(request, response);
                if(taggregate.addTask(task)){
                    response.sendError(200);
                }else{

                    response.sendError(404);
                }
            }
        }catch(Exception IOException){
            System.out.println(IOException.getMessage());
            response.sendError(404);
        }
        response.sendError(404);
    }
}
