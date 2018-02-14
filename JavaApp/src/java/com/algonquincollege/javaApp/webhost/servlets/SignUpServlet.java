/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.algonquincollege.javaApp.webhost.servlets;

import com.algonquincollege.javaApp.database.DBConnection;
import com.algonquincollege.javaApp.utils.json.JSONParser;
import com.algonquincollege.javaApp.webhost.WebInterfaceServlet;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Byzantian
 */
public class SignUpServlet extends WebInterfaceServlet {
    private JSONParser json = new JSONParser();
    private DBConnection db = new DBConnection();
    @Override
    public String toString(HttpServletRequest request) {
       
        if(json.parseLogin(new String(""))){
            if(db.connect() == null){
                 return"\"signedup\":\"false\"";
             }else{
                 if(db.signUp(json.map.get("firstName"), json.map.get("lastName"), json.map.get("email"), json.map.get("username"), json.map.get("password"))){
                     return "\"signedup\":\"true\"";
                 }
             }
             
        }
        return "\"signedup\":\"false\"";
    }
    
}
