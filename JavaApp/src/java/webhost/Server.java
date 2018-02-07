/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webhost;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import javax.servlet.http.HttpServlet;

/**
 *
 * @author jp972
 */
public class Server extends HttpServlet{
    
    final String IP = "10.70.160.109";
    
    void FSCom() throws MalformedURLException, IOException{
        
        HttpURLConnection connection;
        URL url = new URL(IP);
        connection = (HttpURLConnection)url.openConnection();
        connection.connect();
        connection.getOutputStream();
        if(connection == null){
            
        }
    }
}
