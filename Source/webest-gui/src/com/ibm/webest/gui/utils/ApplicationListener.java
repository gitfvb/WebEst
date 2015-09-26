package com.ibm.webest.gui.utils;

import java.util.Locale;

import javax.ejb.EJB;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletContextEvent;

import org.apache.log4j.Logger;

import com.ibm.webest.processing.administration.EstimationManagerServiceLocal;

/**
 * Class that can act as listener for the startup and shutdown of the web application.<br>
 * Handles startup and shutdown tasks. 
 * @author Gregor Schumm
 */
public class ApplicationListener implements ServletContextListener	{

	private static Logger log = Logger.getLogger(ApplicationListener.class);
	
	@EJB
	private EstimationManagerServiceLocal estimationManager;
    
	@Override
	public void	contextInitialized(ServletContextEvent ce) {
		log.info("WebEst application is starting up.");
		estimationManager.startup();
		Locale.setDefault(Locale.ENGLISH); 
	}

	@Override
	public void	contextDestroyed(ServletContextEvent ce) {
		log.info("WebEst application is shutting down.");
	}
}
