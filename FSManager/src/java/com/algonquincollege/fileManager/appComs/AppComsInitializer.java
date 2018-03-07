/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.algonquincollege.fileManager.appComs;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 *
 * @author Devon
 */
public class AppComsInitializer implements ServletContextListener{
    AppCommunications communicator;
    
    @Override
    public void contextInitialized(ServletContextEvent sce){
        System.out.println("App Coms Initializer: Creating Interlink");
        
        try {
            communicator = new AppCommunications();
        } catch (IOException ex) {
            Logger.getLogger(AppComsInitializer.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("App Coms Initializer: Interlink Failure...");
        }
        
        
        
        //sce.getServletContext().setAttribute("Interlink", communicator);
    }
    
    @Override
    public void contextDestroyed(ServletContextEvent sce){
        System.out.println("App Coms Initializer: Destroying Interlink");
        communicator.shutdown();
    }
}
