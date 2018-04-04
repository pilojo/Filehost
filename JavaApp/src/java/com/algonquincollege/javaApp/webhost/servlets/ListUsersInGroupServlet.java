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
 * @author jp972
 */
public class ListUsersInGroupServlet extends WebInterfaceServlet{
    public String toString(HttpServletRequest request){
         try{
            json = new JSONParser();
            if(json.parseListUsersInGroup(ByteReconstruct.byteToString(request))){
                if(db.connect() == null){
                    
                    return "\"success\":\"false\"";
                }else{
                        String[] data = db.usersInGroup(json.map.get("groupName"));
                        if(data == null) return "\"success\":\"false\"";
                        String res = "";
                        for(int i = 0; i < data.length; i++){
                            res += "\"name\":\"" + data[i] + "\",";
                        }
                        res = res.substring(0, res.length()-1);
                        return res;
                    }
                }
            }
        catch(Exception e){
             
         }
        return "\"success\":\"false\"";
    }
}
