package com.algonquincollege.waterbin.fs.fsAggregator;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Servlet Context Listener responsible for initializing the fs aggregator
 * upon tomcat startup. This will also prevent the initialization of any servlets
 * until the aggregator is ready
 *
 * @author Devon St. John
 */
public class FSAggregatorInitializer implements ServletContextListener {
    
    /**
     * Starts the aggregator and injects it into this web application's servlet
     * context
     * 
     * @param sce The context that all Tomcat Servlets of this web application use
     */
    @Override
    public void contextInitialized(ServletContextEvent sce){
        System.out.println("File System Aggregator Initializer: Starting File System Aggregator...");
        //Get the servlet context from the context event, set an attribute to represent
        //the reference to the aggregator, and initialize an aggregator and inject it
        sce.getServletContext().setAttribute("aggregator", new FSAggregator());
    }
    
    /**
     * Called when the Tomcat server is shutting down, will inform the aggregator
     * that it must finish all tasks and shut down gracefully
     * 
     * @param sce The context that all Tomcat Servlets of this web application use
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce){
        //extract the aggregator reference from the Tomcat Servlet Context
        FSAggregator aggregator = (FSAggregator)sce.getServletContext().getAttribute("aggregator");
        System.out.println("File System Aggregator Initializer: Preparing to Shutdown File System Aggregator...");
        //Begin shutdown of the aggregator
        aggregator.shutdown();
    }
}
