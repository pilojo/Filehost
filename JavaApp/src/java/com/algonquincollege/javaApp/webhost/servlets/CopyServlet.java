/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.algonquincollege.javaApp.webhost.servlets;

import com.algonquincollege.javaApp.fileManager.utils.ByteReconstruct;
import com.algonquincollege.javaApp.webhost.WebInterfaceServlet;
import com.algonquincollege.waterbin.fs.fsAggregator.FSAggregator;
import com.algonquincollege.waterbin.fs.tasks.Copy;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author John Pilon
 * To be implemented later
 */
public class CopyServlet extends WebInterfaceServlet {
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        super.doPost(request, response);
    }
    
    @Override
    public String toString(HttpServletRequest request) {
        ListContentsServlet ls = new ListContentsServlet();
        try{
            if(json.parseLogin(ByteReconstruct.byteToString(request))){
                if(db.connect() == null){
                    return ls.toString();
                }else{
                    FSAggregator aggregator = (FSAggregator)getServletContext().getAttribute("aggregator");
                    aggregator.addTask(new Copy("Lol","Woot"));
                    return ls.toString();
                }
            }
        }catch(Exception IOException){}
        return ls.toString();
    }
    
}
