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
        ListContentsServlet ls = null;
        try{
            json = new JSONParser();
            if(json.parseCp(ByteReconstruct.byteToString(request))){
                //ls = new ListContentsServlet(json.map.get("from"));
                if(db.connect() == null){
                    return "\"success\":\"false\"";
                }else{
                    if(db.verifyOwner((String)request.getSession().getAttribute("email"), json.map.get("from")) && db.verifyOwner((String)request.getSession().getAttribute("email"), json.map.get("to"))){
                        FSAggregator aggregator = (FSAggregator)getServletContext().getAttribute("aggregator");
                        if(aggregator.addTask(new Copy(json.map.get("from"),json.map.get("to")))){
                            if(json.map.get("type").equals(new String("File"))){
                                db.newFile(json.map.get("to"));
                                return "\"success\":\"true\"";
                            }else{
                                db.copyFolder(json.map.get("from"), json.map.get("to"));
                                return "\"success\":\"true\"";
                            }
                        }else{
                           return "\"success\":\"false\"";
                        }
                    }else{
                        return "\"success\":\"false\"";
                    }
                }
            }
        }catch(Exception IOException){}
        return "\"success\":\"false\"";
    }
    
}
