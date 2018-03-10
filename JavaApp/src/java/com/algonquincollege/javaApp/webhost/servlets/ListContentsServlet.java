/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.algonquincollege.javaApp.webhost.servlets;

import com.algonquincollege.javaApp.fileManager.utils.ByteReconstruct;
import com.algonquincollege.javaApp.webhost.WebInterfaceServlet;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author John Pilon
 * To be implemented later
 */
public class ListContentsServlet extends WebInterfaceServlet {
    private String path = null;
    
    public ListContentsServlet(){
        path = null;
    }
    public ListContentsServlet(String path){
        this.path = path;
    }
    
    @Override
    public String toString(HttpServletRequest request) {
        try{
            if(json.parseMkdir(ByteReconstruct.byteToString(request))){//Mkdir has the same regex needed for list
                if(db.connect() == null){
                    System.out.println("DB is Null");
                    return new String();
                }else{
                    System.out.println("/"+db.getUserIDFromUsername(db.getUserIDFromPath(json.map.get("path")))+"/"+json.map.get("path").substring(json.map.get("path").indexOf("/", 1)+1));
                    String[][] data;
                    if(path==null){
                        data = db.list("/"+db.getUserIDFromUsername(db.getUserIDFromPath(json.map.get("path")))+"/"+json.map.get("path").substring(json.map.get("path").indexOf("/", 1)+1));
                    }else{
                        data = db.list("/"+db.getUserIDFromUsername(db.getUserIDFromPath(path))+"/"+path.substring(path.indexOf("/", 1)+1));
                    }
                    System.out.println(data[0][0]);
                    if(data == null) return new String();
                    String send = new String("");
                    send += "\"contents\":[";
                    for(int i = 0; i < data.length; i++){
                        send += "{\"name\":\"" + data[i][0] + "\",\"type\":\"" + data[i][1] + "\",\"size\":\"\",\"lastModified\":\"\"},";
                    }
                    if(data.length > 0)send = send.substring(0, send.length()-1);
                    send +="]";
                    return send;
                }
            }
        }catch(Exception IOException){
            
        }
        return new String();
    }
    
}
