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
public class ListGroupsServlet extends WebInterfaceServlet{

    public String toString(HttpServletRequest request){
        try{
                json = new JSONParser();
                if(json.parseListGroups(ByteReconstruct.byteToString(request))){
                    if(db.connect() == null){
                        return "\"success\":\"false\"";
                    }else{
                        String[] groups = db.ownedGroups(json.map.get("username"));
                        String res = "";
                        for(int i = 0; i < groups.length; i++){
                            res += "\"name\":\"" + groups[i] + "\",";
                        }
                        res = res.substring(0, res.length()-1);
                        return res;
                    }
                }
        }catch(Exception e){
            
        }
        return "\"success\":\"false\"";               
    }

}
