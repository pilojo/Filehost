/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.algonquincollege.javaApp.webhost.servlets;

import com.algonquincollege.javaApp.database.DBConnection;
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
    private JSONParser json = new JSONParser();
    private DBConnection db = new DBConnection();
    /**
     * Sends JSON to a client that connects to this servlet.
     *
     * @param request servlet request
     * @return String: JSON formatted data for the client
     */
    @Override
    public String toString(HttpServletRequest request) {
        try{
            //Beginning of String reconstruction
            byte[] bytes = new byte[1];
            ArrayList<Byte> bindata = new ArrayList();
            while(-1 != request.getInputStream().read(bytes)){
                bindata.add(bytes[0]);
            }
            byte[] tmpdata = new byte[bindata.size()];
            for(int i = 0; i < bindata.size(); i++){
                tmpdata[i] = bindata.get(i);
            }//End of String reconstruction
            
            if(json.parseLogin(new String(tmpdata))){
                if(db.connect() == null){
                     return"\"logedin\":\"false\"";
                 }else{
                     if(db.login(json.map.get("username"), json.map.get("password"))){
                         return "\"logedin\":\"true\"";
                     }
                 }

            }
            //Any exceptions thrown means the login failed.
        }catch(Exception e){return "\"logedin\":\"false\"";}
        
        return "\"logedin\":\"false\"";
    }
    
}
