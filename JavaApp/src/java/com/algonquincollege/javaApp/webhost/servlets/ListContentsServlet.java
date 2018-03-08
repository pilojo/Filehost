/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.algonquincollege.javaApp.webhost.servlets;

import com.algonquincollege.javaApp.fileManager.FileManagerInterface;
import com.algonquincollege.javaApp.fileManager.commands.MKDIR;
import com.algonquincollege.javaApp.fileManager.utils.ByteReconstruct;
import com.algonquincollege.javaApp.webhost.WebInterfaceServlet;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author John Pilon
 * To be implemented later
 */
public class ListContentsServlet extends WebInterfaceServlet {
    
    @Override
    public String toString(HttpServletRequest request) {
        ListContentsServlet ls = new ListContentsServlet();
        try{
            if(json.parseList(ByteReconstruct.byteToString(request))){
                if(db.connect() == null){
                }else{
                    
                }
            }
        }catch(Exception IOException){}
        return new String();
    }
    
}
