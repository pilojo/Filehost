/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.algonquincollege.javaApp.webhost.servlets;

import com.algonquincollege.javaApp.fileManager.utils.ByteReconstruct;
import com.algonquincollege.javaApp.utils.json.JSONParser;
import com.algonquincollege.javaApp.webhost.WebInterfaceServlet;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author John Pilon To be implemented later
 */
public class ListContentsServlet extends WebInterfaceServlet {

    @Override
    public String toString(HttpServletRequest request) {
        System.out.println("LIST");
        try {
            json = new JSONParser();
            if (json.parseMkdir(ByteReconstruct.byteToString(request))) {//Mkdir has the same regex needed for list
                if (db.connect() == null) {
                    System.out.println("DB is Null");
                    return "\"success\":\"false\"";
                } else {
                    System.out.println(json.map.get("path"));
                    if (db.canUserAccessFolder((String) request.getSession().getAttribute("username"), json.map.get("path")) || db.verifyOwner((String)request.getSession().getAttribute("email"), json.map.get("path"))) {
                        String[][] data;
                        data = db.list(json.map.get("path"));
                        
                        if (data == null) {
                            
                            System.out.println("HIT");
                            return "\"success\":\"false\"";
                        }   
                        String send = "";
                        
                        send += "\"contents\":[";
                        
                        System.out.println("HIT");
                        for (int i = 0; i < data.length; i++) {
                            
                            send += "{\"name\":\"" + data[i][0] + "\",\"type\":\"" + data[i][1] + "\",\"permission\":\"" + data[i][2] + "\",\"size\":\""+ data[i][4] +"\",\"lastModified\":\"" + data[i][3] +"\"},";
                        }
                        if (data.length > 0) {
                            send = send.substring(0, send.length() - 1);
                        }
                        send += "]";
                        
                        return send;
                    } else {
                        return "\"success\":\"false\"";
                    }
                }
            }
        } catch (Exception IOException) {
            System.out.println(IOException.getMessage());
            return "\"success\":\"false\"";
        }
        return "\"success\":\"false\"";
    }

}
