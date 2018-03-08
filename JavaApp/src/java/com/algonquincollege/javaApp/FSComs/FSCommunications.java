/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.algonquincollege.javaApp.FSComs;

import com.algonquincollege.javaApp.fileManager.Command;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author jp972
 */
public class FSCommunications {
    private Socket sock;
    private ObjectOutputStream os;
    private ObjectInputStream is;
    
    public FSCommunications() {
        try{
            sock = new Socket("192.168.0.2", 6832);
            
            System.out.println("Socket connected: ");
            os = new ObjectOutputStream(sock.getOutputStream());
            is = new ObjectInputStream(sock.getInputStream());
            System.out.println("Streams Grabbed");
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    
    public void send(Command cmd){
        try{
            os.writeObject(cmd);
        }catch(Exception e){
            
        }
    }
    
    public void shutdown(){
        
    }
}
