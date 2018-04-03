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
public class ChangePermissionServlet extends WebInterfaceServlet{
    
    public String toString(HttpServletRequest request){
         try{
            json = new JSONParser();
            if(json.parseChangePermission(ByteReconstruct.byteToString(request))){
                if(db.connect() == null){
                    
                    return "\"success\":\"false\"";
                }else{
                    if(json.map.get("type").equals("File")){
                        if(db.changeFilePermission(json.map.get("path"), json.map.get("Permission"))){
                            return "\"success\":\"true\"";
                        }
                    }else if(json.map.get("type").equals("Folder")){
                        if(db.changeFolderPermission(json.map.get("path"), json.map.get("Permission"))){
                            return "\"success\":\"true\"";
                        }
                    }
                }
            }
         }catch(Exception e){
             
         }
        return "\"success\":\"false\"";
    }
}
