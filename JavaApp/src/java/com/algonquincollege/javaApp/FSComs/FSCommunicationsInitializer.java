/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.algonquincollege.javaApp.FSComs;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 *
 * @author jp972
 */
public class FSCommunicationsInitializer implements ServletContextListener {
    
    @Override
    public void contextInitialized(ServletContextEvent sce){
        System.out.println("File System Communications Initializer: Starting File System Communications...");
        sce.getServletContext().setAttribute("FSCommunications", new FSCommunications());
    }
    
    @Override
    public void contextDestroyed(ServletContextEvent sce){
        FSCommunications coms = (FSCommunications)sce.getServletContext().getAttribute("aggregator");
        System.out.println("File System Communications Initializer: Preparing to Shutdown File System Communications....");
        //coms.shutdown();
    }
}
