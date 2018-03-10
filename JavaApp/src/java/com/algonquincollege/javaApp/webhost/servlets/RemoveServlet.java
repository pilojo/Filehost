/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.algonquincollege.javaApp.webhost.servlets;

import com.algonquincollege.javaApp.fileManager.utils.ByteReconstruct;
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
            if(json.parseRm(ByteReconstruct.byteToString(request))){
                    System.out.println(json.map.get("path"));
                //ls = new ListContentsServlet(new String(json.map.get("path")));
                if(db.connect() == null){
                    return ls.toString();
                }else{
                    FSAggregator aggregator = (FSAggregator)getServletContext().getAttribute("aggregator");
                    if(aggregator.addTask(new Remove(json.map.get("path")))){
                        System.out.println("TRUE");
                        if(json.map.get("type").equals(new String("File"))){
                            System.out.println("/"+db.getUserIDFromUsername(db.getUserIDFromPath(json.map.get("path")))+"/"+json.map.get("path").substring(json.map.get("path").indexOf("/", 1)+1));
                            db.deleteFile("/"+db.getUserIDFromUsername(db.getUserIDFromPath(json.map.get("path")))+"/"+json.map.get("path").substring(json.map.get("path").indexOf("/", 1)+1));
                        }else{
                            db.deleteFolder("/"+db.getUserIDFromUsername(db.getUserIDFromPath(json.map.get("path")))+"/"+json.map.get("path").substring(json.map.get("path").indexOf("/", 1)+1));
                        }
                    }else{
                       System.out.println("FALSE");//Went to /dev/null
                    }
                    return ls.toString();
                }
            }
        }catch(Exception IOException){}
        return ls.toString();
    }
    
}
