/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.algonquincollege.javaApp.webhost.servlets;

import com.algonquincollege.javaApp.fileManager.utils.ByteReconstruct;
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
            
            if(json.parseMkdir(ByteReconstruct.byteToString(request))){
                //ls = new ListContentsServlet(json.map.get("path"));
                System.out.println(ByteReconstruct.byteToString(request));
                
                if(db.connect() == null){
                    return ls.toString();
                }else{
                    FSAggregator aggregator = (FSAggregator)getServletContext().getAttribute("aggregator");
                    if(aggregator.addTask(new MakeDirectory(json.map.get("path")))){
                        db.newFolder("/"+db.getUserIDFromUsername(db.getUserIDFromPath(json.map.get("path")))+"/"+json.map.get("path").substring(json.map.get("path").lastIndexOf("/")+1));
                    }else{
                        //Went to /dev/null
                    }
                    return ls.toString();
                }
            }
        }catch(Exception IOException){}
        return ls.toString();
    }
    
}
