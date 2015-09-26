package com.ibm.webest.gui.utils;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Logger;

/**
 * Listens on session event like session created or destroyed.
 * @author Gregor Schumm
 *
 */
public class SessionListener implements HttpSessionListener {

	private static Logger log = Logger.getLogger(SessionListener.class);
	
	public SessionListener() {
	}

	@Override
	public void sessionCreated(HttpSessionEvent arg0) {
		log.info("Session "+arg0.getSession().getId()+" opened.");
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent arg0) {
		log.info("Session "+arg0.getSession().getId()+" closed.");
	}

}
