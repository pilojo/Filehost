/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.algonquincollege.javaApp.webhost.servlets;

import com.algonquincollege.javaApp.fileManager.FileManagerInterface;
import com.algonquincollege.javaApp.fileManager.commands.MV;
import com.algonquincollege.javaApp.fileManager.utils.ByteReconstruct;
import com.algonquincollege.javaApp.webhost.WebInterfaceServlet;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author John Pilon
 * To be implemented later.
 */
public class MoveServlet extends WebInterfaceServlet {

    @Override
    public String toString(HttpServletRequest request) {
        ListContentsServlet ls = new ListContentsServlet();
        try{
            if(json.parseLogin(ByteReconstruct.byteToString(request))){
                if(db.connect() == null){
                    return ls.toString();
                }else{
                    MV mv = new MV(json.map.get("from"), json.map.get("to"));
                    FileManagerInterface.sendCommand(mv);
                    return ls.toString();
                }
            }
        }catch(Exception IOException){}
        return ls.toString();
    }
    
}
