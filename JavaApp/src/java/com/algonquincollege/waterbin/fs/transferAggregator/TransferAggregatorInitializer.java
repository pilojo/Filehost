package com.algonquincollege.waterbin.fs.transferAggregator;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Servlet Context Listener responsible for initializing the transfer aggregator
 * upon tomcat startup. This will also prevent the initialization of any servlets
 * until the aggregator is ready
 *
 * @author Devon St. John
 */
public class TransferAggregatorInitializer implements ServletContextListener {
    
    /**
     * Starts the aggregator and injects it into this web application's servlet context
     * 
     * @param sce The context that all Tomcat Servlets of this web application use
     */
    @Override
    public void contextInitialized(ServletContextEvent sce){
        System.out.println("File Transfer Aggregator Initializer: Starting File Transfer Aggregator...");
        //Get the servlet context from the context event, set an attribute to represent
        //the reference to the aggregator, and initialize an aggregator and inject it
        sce.getServletContext().setAttribute("transfer aggregator", new TransferAggregator());
    }
    
    
    /**
     * Called when the Tomcat server is shutting down, will inform the aggregator
     * that it must finish all transfers and shut down gracefully
     * 
     * @param sce The context that all Tomcat Servlets of this web application use
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce){
        //extract the aggregator reference from the Tomcat Servlet Context
        TransferAggregator aggregator = (TransferAggregator)sce.getServletContext().getAttribute("transfer aggregator");
        System.out.println("File Transfer Aggregator Initializer: Preparing to Shutdown File Transfer Aggregator...");
        //Begin shutdown of the aggregator
        aggregator.shutdown();
    }
}
