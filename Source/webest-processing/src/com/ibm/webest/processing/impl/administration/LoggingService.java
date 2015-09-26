package com.ibm.webest.processing.impl.administration;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

import com.ibm.webest.persistence.model.User;
import com.ibm.webest.processing.administration.AuthenticationServiceLocal;
import com.ibm.webest.processing.administration.LoggingServiceLocal;

/**
 * Service for logging. Logs with the default log4j logger, but adds information
 * about the current user to the message.
 */
@Stateless
public class LoggingService implements LoggingServiceLocal {

	@EJB
	private AuthenticationServiceLocal authService;

	public LoggingService() {
	}

	/**
	 * Adds the current user to the given message.<br>
	 * Example: <code>&lt;username, role&gt; message</code>
	 * 
	 * @param message
	 *            the message to add the user to
	 * @return a message in the form <code>&lt;username, role&gt; message</code>
	 */
	private Object addUser(Object message) {
		String username = "none";
		String role = "none";
		try {
			username = authService.getLoginUserName();
			User u = authService.getCurrentUser();
			role = u.getRole().getId();
		} catch (Exception e) {
			Logger.getLogger(LoggingService.class).error(
					"Could not find current user.", e);
		}
		message = String.format("<%s, %s> %s", username, role,
				message == null ? "null" : message.toString());
		return message;
	}

	/**
	 * Log a message at the TRACE level for the given caller.
	 * 
	 * @param caller
	 *            the caller (used to retrieve the appropriate logger)
	 * @param message
	 *            the message to log
	 * @param t
	 *            the corresponding exception
	 */
	public void trace(Object caller, Object message, Throwable t) {
		message = addUser(message);
		Logger.getLogger(caller.getClass()).trace(message, t);
	}

	/**
	 * Log a message at the TRACE level for the given caller.
	 * 
	 * @param caller
	 *            the caller (used to retrieve the appropriate logger)
	 * @param message
	 *            the message to log
	 */
	public void trace(Object caller, Object message) {
		message = addUser(message);
		Logger.getLogger(caller.getClass()).trace(message);
	}

	/**
	 * Log a message at the DEBUG level for the given caller.
	 * 
	 * @param caller
	 *            the caller (used to retrieve the appropriate logger)
	 * @param message
	 *            the message to log
	 * @param t
	 *            the corresponding exception
	 */
	public void debug(Object caller, Object message, Throwable t) {
		message = addUser(message);
		Logger.getLogger(caller.getClass()).debug(message, t);
	}

	/**
	 * Log a message at the DEBUG level for the given caller.
	 * 
	 * @param caller
	 *            the caller (used to retrieve the appropriate logger)
	 * @param message
	 *            the message to log
	 */
	public void debug(Object caller, Object message) {
		message = addUser(message);
		Logger.getLogger(caller.getClass()).debug(message);
	}

	/**
	 * Log a message at the ERROR level for the given caller.
	 * 
	 * @param caller
	 *            the caller (used to retrieve the appropriate logger)
	 * @param message
	 *            the message to log
	 * @param t
	 *            the corresponding exception
	 */
	public void error(Object caller, Object message, Throwable t) {
		message = addUser(message);
		Logger.getLogger(caller.getClass()).error(message, t);
	}

	/**
	 * Log a message at the ERROR level for the given caller.
	 * 
	 * @param caller
	 *            the caller (used to retrieve the appropriate logger)
	 * @param message
	 *            the message to log
	 */
	public void error(Object caller, Object message) {
		message = addUser(message);
		Logger.getLogger(caller.getClass()).error(message);
	}

	/**
	 * Log a message at the FATAL level for the given caller.
	 * 
	 * @param caller
	 *            the caller (used to retrieve the appropriate logger)
	 * @param message
	 *            the message to log
	 * @param t
	 *            the corresponding exception
	 */
	public void fatal(Object caller, Object message, Throwable t) {
		message = addUser(message);
		Logger.getLogger(caller.getClass()).fatal(message, t);
	}

	/**
	 * Log a message at the FATAL level for the given caller.
	 * 
	 * @param caller
	 *            the caller (used to retrieve the appropriate logger)
	 * @param message
	 *            the message to log
	 */
	public void fatal(Object caller, Object message) {
		message = addUser(message);
		Logger.getLogger(caller.getClass()).fatal(message);
	}

	/**
	 * Log a message at the INFO level for the given caller.
	 * 
	 * @param caller
	 *            the caller (used to retrieve the appropriate logger)
	 * @param message
	 *            the message to log
	 * @param t
	 *            the corresponding exception
	 */
	public void info(Object caller, Object message, Throwable t) {
		message = addUser(message);
		Logger.getLogger(caller.getClass()).info(message, t);
	}

	/**
	 * Log a message at the INFO level for the given caller.
	 * 
	 * @param caller
	 *            the caller (used to retrieve the appropriate logger)
	 * @param message
	 *            the message to log
	 */
	public void info(Object caller, Object message) {
		message = addUser(message);
		Logger.getLogger(caller.getClass()).info(message);
	}

	/**
	 * Log a message at the given level for the given caller.
	 * 
	 * @param caller
	 *            the caller (used to retrieve the appropriate logger)
	 * @param priority
	 *            the log level
	 * @param message
	 *            the message to log
	 * @param t
	 *            the corresponding exception
	 */
	public void log(Object caller, Priority priority, Object message,
			Throwable t) {
		message = addUser(message);
		Logger.getLogger(caller.getClass()).log(priority, message, t);
	}

	/**
	 * Log a message at the given level for the given caller.
	 * 
	 * @param caller
	 *            the caller (used to retrieve the appropriate logger)
	 * @param priority
	 *            the log level
	 * @param message
	 *            the message to log
	 */
	public void log(Object caller, Priority priority, Object message) {
		message = addUser(message);
		Logger.getLogger(caller.getClass()).log(priority, message);
	}

	/**
	 * Log a message at the given level for the given caller.
	 * 
	 * @param callerFQCN
	 *            the fully qualified name of the caller (used to retrieve the
	 *            appropriate logger)
	 * @param level
	 *            the log level
	 * @param message
	 *            the message to log
	 * @param t
	 *            the corresponding exception
	 */
	public void log(String callerFQCN, Priority level, Object message,
			Throwable t) {
		message = addUser(message);
		Logger.getLogger(callerFQCN).log(callerFQCN, level, message, t);
	}

	/**
	 * Log a message at the WARN level for the given caller.
	 * 
	 * @param caller
	 *            the caller (used to retrieve the appropriate logger)
	 * @param message
	 *            the message to log
	 * @param t
	 *            the corresponding exception
	 */
	public void warn(Object caller, Object message, Throwable t) {
		message = addUser(message);
		Logger.getLogger(caller.getClass()).warn(message, t);
	}

	/**
	 * Log a message at the WARN level for the given caller.
	 * 
	 * @param caller
	 *            the caller (used to retrieve the appropriate logger)
	 * @param message
	 *            the message to log
	 */
	public void warn(Object caller, Object message) {
		message = addUser(message);
		Logger.getLogger(caller.getClass()).warn(message);
	}

}
