/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.algonquincollege.javaApp.webhost.servlets;

import com.algonquincollege.javaApp.fileManager.utils.ByteReconstruct;
import com.algonquincollege.javaApp.utils.json.JSONParser;
import com.algonquincollege.javaApp.webhost.WebInterfaceServlet;
import com.algonquincollege.waterbin.fs.fsAggregator.FSAggregator;
import com.algonquincollege.waterbin.fs.tasks.MakeDirectory;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author John Pilon
 * To be implemented later.
 */
public class MakeDirectoryServlet extends WebInterfaceServlet {

    @Override
    public String toString(HttpServletRequest request) {
        ListContentsServlet ls = null;
        try{
            json = new JSONParser();
            if(json.parseMkdir(ByteReconstruct.byteToString(request))){
                ls = new ListContentsServlet();
                if(db.connect() == null){
                    return "\"success\":\"false\"";
                }else{
                    if(db.verifyOwner((String)request.getSession().getAttribute("email"), json.map.get("path"))){
                        FSAggregator aggregator = (FSAggregator)getServletContext().getAttribute("aggregator");
                        if(aggregator.addTask(new MakeDirectory(json.map.get("path")))){
                            return "\"success\":\"true\"";
                        }else{
                            
                            return "\"success\":\"false\"";
                        }
                    }else{
                        return "\"success\":\"false\"";
                    }
                    
                }
            }else{
                return "\"success\":\"false\"";
            }
        }catch(Exception IOException){
            System.out.println(IOException.getMessage());
            return "\"success\":\"false\"";
        }
    }
    
}
