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
        ListContentsServlet ls = null;
        try{
            if(json.parseCp(ByteReconstruct.byteToString(request))){
                //ls = new ListContentsServlet(json.map.get("from"));
                if(db.connect() == null){
                    return ls.toString();
                }else{
                    FSAggregator aggregator = (FSAggregator)getServletContext().getAttribute("aggregator");
                    if(aggregator.addTask(new Copy(json.map.get("from"),json.map.get("to")))){
                        if(json.map.get("type").equals(new String("File"))){
                            System.out.println("/"+db.getUserIDFromUsername(db.getUserIDFromPath(json.map.get("to")))+"/"+json.map.get("to").substring(json.map.get("to").indexOf("/",1)+1));
                            db.newFile("/"+db.getUserIDFromUsername(db.getUserIDFromPath(json.map.get("to")))+"/"+json.map.get("to").substring(json.map.get("to").indexOf("/",1)+1));
                        }else{
                            //Copy Folder to come. Not ready yet.
                        }
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
