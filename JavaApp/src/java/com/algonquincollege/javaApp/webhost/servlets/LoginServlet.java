/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.algonquincollege.javaApp.webhost.servlets;

import com.algonquincollege.javaApp.database.DBConnection;
import com.algonquincollege.javaApp.utils.json.JSONParser;
import com.algonquincollege.javaApp.webhost.WebInterfaceServlet;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Byzantian
 */
public class LoginServlet extends WebInterfaceServlet {
    private JSONParser json = new JSONParser();
    private DBConnection db = new DBConnection();
    @Override
    public String toString(HttpServletRequest request) {
        if(json.parseLogin(new String(""))){
            if(db.connect() == null){
                 return"\"logedin\":\"false\"";
             }else{
                 if(db.login(json.map.get("username"), json.map.get("password"))){
                     return "\"logedin\":\"true\"";
                 }
             }
             
        }
        return "\"logedin\":\"false\"";
    }
    
}
