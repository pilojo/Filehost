/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.algonquincollege.javaApp.webhost.servlets;

import com.algonquincollege.javaApp.fileManager.utils.ByteReconstruct;
import com.algonquincollege.javaApp.utils.json.JSONParser;
import com.algonquincollege.javaApp.webhost.WebInterfaceServlet;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author John Pilon
 */
public class SearchFileServlet extends WebInterfaceServlet{
    @Override
    public String toString(HttpServletRequest request){
        try{
            json = new JSONParser();
            if(json.parseSearchFile(ByteReconstruct.byteToString(request))){
                if(db.connect() == null){
                    
                    return "\"success\":\"false\"";
                }else{
                    if(json.map.get("fileName").isEmpty() && json.map.get("username").isEmpty() && json.map.get("firstName").isEmpty() && json.map.get("lastName").isEmpty()) return "\"success\":\"false\"";
                    String[][] data = db.searchFiles(json.map.get("username").isEmpty() ? null:json.map.get("username"), json.map.get("firstName").isEmpty() ? null:json.map.get("firstName"), json.map.get("lastName").isEmpty() ? null:json.map.get("lastName"), json.map.get("fileName").isEmpty() ? null:json.map.get("fileName"));
                    String send = "\"files\":[";
                    if (data == null) {
                        return "\"success\":\"false\"";
                    }
                    for(String[] data1 : data){
                        
                        send+= "{\"name\":\"" + data1[0] + "\",\"type\":\"File\",\"username\":\"" + data1[2] + "\",\"path\":\"" +data1[3] + "\"},";
                    }
                    
                    send = send.substring(0, send.length()-1);
                    send += "]";
                    return send;
                }
            }
        }catch(Exception e){
        }
        return "\"success\":\"false\"";
    }
}
