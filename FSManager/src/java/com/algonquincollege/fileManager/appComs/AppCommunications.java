/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.algonquincollege.fileManager.appComs;

import com.algonquincollege.javaApp.fileManager.commands.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Devon
 */
public class AppCommunications implements Runnable{
    private final Socket interlink;
    private ObjectInputStream istream;
    private ObjectOutputStream ostream;
    
    public AppCommunications() throws IOException{
        interlink = new Socket("10.10.10.10", 8632);
        istream = new ObjectInputStream(interlink.getInputStream());
        ostream = new ObjectOutputStream(interlink.getOutputStream());
    }
    
    @Override
    public void run() {
        Object inboundObject;
        
        while(!interlink.isClosed()){
            try {
                inboundObject = istream.readObject();
                
                if (inboundObject instanceof CP){
                    
                }
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(AppCommunications.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void shutdown(){
        try {
            interlink.close();
        } catch (IOException ex) {
            Logger.getLogger(AppCommunications.class.getName()).log(Level.SEVERE, "Error Closing Connetion to App", ex);
        }
    }
}
