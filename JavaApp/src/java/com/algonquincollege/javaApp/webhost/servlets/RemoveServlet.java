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
import com.algonquincollege.waterbin.fs.tasks.Remove;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author John Pilon
 * To be implemented later.
 */
public class RemoveServlet extends WebInterfaceServlet {

    @Override
    public String toString(HttpServletRequest request) {
         ListContentsServlet ls = null;
        try{
            json = new JSONParser();
            if(json.parseRm(ByteReconstruct.byteToString(request))){
                    System.out.println(json.map.get("path"));
                ls = new ListContentsServlet();
                if(db.connect() == null){
                    return ls.toString(json.map.get("path"), "false");
                }else{
                    if(db.verifyOwner((String)request.getSession().getAttribute("email"), json.map.get("path"))){
                        FSAggregator aggregator = (FSAggregator)getServletContext().getAttribute("aggregator");
                        if(aggregator.addTask(new Remove(json.map.get("path")))){
                            if(json.map.get("type").equals(new String("File"))){
                                db.deleteFile(json.map.get("path"));
                            }else{
                                db.deleteFolder(json.map.get("path"));
                            }
                        }else{
                           return ls.toString(json.map.get("path"), "false");
                        }
                    }else{
                        return ls.toString(json.map.get("path"), "false");
                    }
                }
            }
        }catch(Exception IOException){}
        return ls.toString(json.map.get("path"), "false");
    }
    
}
