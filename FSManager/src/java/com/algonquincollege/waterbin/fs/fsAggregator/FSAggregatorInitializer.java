/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.algonquincollege.waterbin.fs.fsAggregator;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 *
 * @author Devon
 */
public class FSAggregatorInitializer implements ServletContextListener {
    
    @Override
    public void contextInitialized(ServletContextEvent sce){
        System.out.println("File System Aggregator Initializer: Starting File System Aggregator...");
        sce.getServletContext().setAttribute("aggregator", new FSAggregator());
    }
    
    
    
    @Override
    public void contextDestroyed(ServletContextEvent sce){
        FSAggregator aggregator = (FSAggregator)sce.getServletContext().getAttribute("aggregator");
        System.out.println("File System Aggregator Initializer: Preparing to Shutdown File System Aggregator...");
        aggregator.shutdown();
    }
    
    
}
