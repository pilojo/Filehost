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
import com.algonquincollege.waterbin.fs.tasks.Move;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author John Pilon
 * To be implemented later.
 */
public class MoveServlet extends WebInterfaceServlet {

    @Override
    public String toString(HttpServletRequest request) {
        try{
            json = new JSONParser();
            if(json.parseMv(ByteReconstruct.byteToString(request))){
                if(db.connect() == null){
                    System.out.println("DB is Null");
                    return "\"success\":\"false\"";
                }else{
                    if(db.verifyOwner((String)request.getSession().getAttribute("email"), json.map.get("from")) && db.verifyOwner((String)request.getSession().getAttribute("email"), json.map.get("to"))){
                        FSAggregator aggregator = (FSAggregator)getServletContext().getAttribute("aggregator");
                        if(aggregator.addTask(new Move(json.map.get("from"),json.map.get("to")))){
                            return "\"success\":\"true\"";
                        }else{
                            return "\"success\":\"false\"";
                        }
                    }else{
                        return "\"success\":\"false\"";
                    }
                }
            }
        }catch(Exception e){}
        return "\"success\":\"false\"";
    }
    
}
