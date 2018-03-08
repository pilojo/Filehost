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
    
    @Override
    public String toString(HttpServletRequest request) {
        ListContentsServlet ls = new ListContentsServlet();
        try{
            if(json.parseMkdir(ByteReconstruct.byteToString(request))){//Mkdir has the same regex needed for list
                if(db.connect() == null){
                    System.out.println("DB is Null");
                    return "\"sucess\":\"false\"";
                }else{
                    String[][] data = db.list(json.map.get("path"));
                    if(data == null) return "\"success\":\"false\"";
                    String send = new String();
                    for(int i = 0; i < data.length; i++){
                        send += "{\"name\"" + data[i][0] + ":\"type\"" + data[i][1] + "\",\"size\":\"\",\"lastmodified\":\"\"}";
                    }
                    send+="\"success\":\"true\"";
                    return send;
                }
            }
        }catch(Exception IOException){}
        return new String();
    }
    
}
