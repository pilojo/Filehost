/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.algonquincollege.fileManager.appComs;

import com.algonquincollege.fileManager.fileSystemAggregator.FSAggregator;
import com.algonquincollege.fileManager.fileSystemAggregator.tasks.*;
import com.algonquincollege.javaApp.fileManager.commands.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Devon
 */
public class AppCommunications{
    
    private ServerSocket serv;
    private Socket interlink;
    private ObjectInputStream istream;
    private ObjectOutputStream ostream;
    
    public AppCommunications(){
        System.out.println("Constructing");
        
        try{serv = new ServerSocket(6832);
        System.out.println("Listening");
        interlink = serv.accept();
        
        System.out.println("Socket Created");
        ostream = (ObjectOutputStream)interlink.getOutputStream();
        istream = (ObjectInputStream)interlink.getInputStream();
        
        System.out.println("Streams grabbed");
        }catch(IOException e){
        }
    }
    
    /*public void run() {
        
        System.out.println("Interlink: Launched");
        
        Object inboundObject;
        
        while(!interlink.isClosed()){
            try {
                FileSystemTask task = null;
                
                inboundObject = istream.readObject();
                
                System.out.println("Interlink: New Task Received");
                
                if (inboundObject instanceof CP){
                    task = new Copy((CP)inboundObject, ostream);
                }
                else if (inboundObject instanceof MKDIR){
                    task = new MakeDirectory((MKDIR)inboundObject, ostream);
                }
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(AppCommunications.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }*/
    
    public void shutdown(){
        try {
            interlink.close();
        } catch (IOException ex) {
            Logger.getLogger(AppCommunications.class.getName()).log(Level.SEVERE, "Error Closing Connetion to App", ex);
        }
    }
}
