/*
Defines the methods for interfacing with the file server
*/
package com.algonquincollege.javaApp.fileManager;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * @author Byzantian
 */
public class FileManagerInterface {
    
    private final static String IP = "http://10.70.218.168:8080/FSManager/manage/cp";
    
    public static void sendCommand(Command cmd){
        try {
            URL url = new URL(IP);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");

            // Send post request
            connection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
           
            wr.writeBytes(cmd.toString());
            wr.flush();
            wr.close();
            
            int responseCode = connection.getResponseCode();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null)
            {
		response.append(inputLine);
            }
            connection.disconnect();
        } catch(Exception e){}
    }
}
