/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.algonquincollege.waterbin.fs.transferAggregator;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 *
 * @author Devon
 */
public class TransferAggregatorInitializer implements ServletContextListener {
    
    @Override
    public void contextInitialized(ServletContextEvent sce){
        System.out.println("File Transfer Aggregator Initializer: Starting File Transfer Aggregator...");
        sce.getServletContext().setAttribute("transfer aggregator", new TransferAggregator());
    }
    
    
    
    @Override
    public void contextDestroyed(ServletContextEvent sce){
        TransferAggregator aggregator = (TransferAggregator)sce.getServletContext().getAttribute("transfer aggregator");
        System.out.println("File Transfer Aggregator Initializer: Preparing to Shutdown File Transfer Aggregator...");
        aggregator.shutdown();
    }
    
    
}
