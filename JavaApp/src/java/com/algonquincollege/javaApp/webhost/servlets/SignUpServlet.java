/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.algonquincollege.javaApp.webhost.servlets;

import com.algonquincollege.javaApp.database.DBConnection;
import com.algonquincollege.javaApp.utils.json.JSONParser;
import com.algonquincollege.javaApp.webhost.WebInterfaceServlet;
import com.algonquincollege.waterbin.fs.fsAggregator.FSAggregator;
import com.algonquincollege.waterbin.fs.tasks.MakeDirectory;
import java.util.ArrayList;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author John Pilon
 * Inserts a user to the database after receiving signup info. Email verification for signups to come soon.
 */
public class SignUpServlet extends WebInterfaceServlet {
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
            System.out.println("Signup Servlet");
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
            
        if(json.parseSignUp(new String(tmpdata))){
            System.out.println("Signup Servlet: Parsed");
            if(db.connect() == null){
                System.out.println("Signup Servlet: Null DB");
                 return"\"signedup\":\"false\"";
             }else{
                 if(db.signUp(json.map.get("firstName"), json.map.get("lastName"), json.map.get("email"), json.map.get("username"), json.map.get("password"))){
                    System.out.println("Signup Servlet: Created");
                    
                    FSAggregator aggregator = (FSAggregator)getServletContext().getAttribute("aggregator");
                    if(aggregator.addTask(new MakeDirectory(json.map.get("username"))));
                        return "\"signedup\":\"true\"";
                 }
             }
        }
        //Any exceptions means signup failed
        }catch(Exception e){return e.toString();}
        return "\"signedup\":\"false\"";
    }
    
}
