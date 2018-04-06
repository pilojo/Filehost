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
public class ListItemGroupsServlet extends WebInterfaceServlet{
    public String toString(HttpServletRequest request){
         try{
            json = new JSONParser();
            if(json.parseListItemGroups(ByteReconstruct.byteToString(request))){
                if(db.connect() == null){
                    
                    return "\"success\":\"false\"";
                }else{
                        if(json.map.get("type").equals("File")){
                            String[] data = db.fileGroups(json.map.get("path"));
                            if(data == null) {
                                return "\"groups\":[]";
                            }
                            String res = "\"groups\":[";
                            for(int i = 0; i < data.length; i++){
                                res += "\"" + data[i].substring(data[i].indexOf("-")+1) + "\",";
                            }
                            res = res.substring(0, res.length()-1);
                            res +="]";
                            return res;
                        }
                    }
                }
            }
        catch(Exception e){
             
         }
        return "\"success\":\"false\"";
    }
}
