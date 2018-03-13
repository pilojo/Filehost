/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.algonquincollege.javaApp.webhost.servlets;

import com.algonquincollege.javaApp.database.DBConnection;
import com.algonquincollege.javaApp.fileManager.utils.ByteReconstruct;
import com.algonquincollege.javaApp.utils.json.JSONParser;
import com.algonquincollege.javaApp.webhost.WebInterfaceServlet;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author John Pilon
 * Communicates with the database after receiving POST data from browser to validate login information
 */
public class LoginServlet extends WebInterfaceServlet {
    /**
     * Sends JSON to a client that connects to this servlet.
     *
     * @param request servlet request
     * @return String: JSON formatted data for the client
     */
    @Override
    public String toString(HttpServletRequest request) {
        try{
            json = new JSONParser();
            if(json.parseLogin(ByteReconstruct.byteToString(request))){
                if(db.connect() == null){
                     return"\"logedin\":\"false\"";
                 }else{
                    System.out.print(json.map.get("email") + json.map.get("password"));
                     if(db.login(json.map.get("email"), json.map.get("password"))){
                         request.getSession().setAttribute("email", json.map.get("email"));
                         request.getSession().setAttribute("username", db.getUsernameFromEmail(json.map.get("email")));
                         return "\"logedin\":\"true\",\"username\":\"" + request.getSession().getAttribute("username")+"\"";
                     }
                 }

            }
            //Any exceptions thrown means the login failed.
        }catch(Exception e){
            System.out.println(e.getMessage());
            return "\"logedin\":\"false\"";}
        
        
        
        return "\"logedin\":\"false\"";
    }
    
}
