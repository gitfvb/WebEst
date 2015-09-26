package com.ibm.webest.processing.administration;

import javax.ejb.Local;

import org.apache.log4j.Priority;

/**
 * Service for logging. Logs with the default log4j logger, but adds information
 * about the current user to the message.
 */
@Local
public interface LoggingServiceLocal {

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
	public void trace(Object caller, Object message, Throwable t);

	/**
	 * Log a message at the TRACE level for the given caller.
	 * 
	 * @param caller
	 *            the caller (used to retrieve the appropriate logger)
	 * @param message
	 *            the message to log
	 */
	public void trace(Object caller, Object message);

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
	public void debug(Object caller, Object message, Throwable t);

	/**
	 * Log a message at the DEBUG level for the given caller.
	 * 
	 * @param caller
	 *            the caller (used to retrieve the appropriate logger)
	 * @param message
	 *            the message to log
	 */
	public void debug(Object caller, Object message);

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
	public void error(Object caller, Object message, Throwable t);

	/**
	 * Log a message at the ERROR level for the given caller.
	 * 
	 * @param caller
	 *            the caller (used to retrieve the appropriate logger)
	 * @param message
	 *            the message to log
	 */
	public void error(Object caller, Object message);

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
	public void fatal(Object caller, Object message, Throwable t);

	/**
	 * Log a message at the FATAL level for the given caller.
	 * 
	 * @param caller
	 *            the caller (used to retrieve the appropriate logger)
	 * @param message
	 *            the message to log
	 */
	public void fatal(Object caller, Object message);

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
	public void info(Object caller, Object message, Throwable t);

	/**
	 * Log a message at the INFO level for the given caller.
	 * 
	 * @param caller
	 *            the caller (used to retrieve the appropriate logger)
	 * @param message
	 *            the message to log
	 */
	public void info(Object caller, Object message);

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
			Throwable t);

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
	public void log(Object caller, Priority priority, Object message);

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
			Throwable t);

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
	public void warn(Object caller, Object message, Throwable t);

	/**
	 * Log a message at the WARN level for the given caller.
	 * 
	 * @param caller
	 *            the caller (used to retrieve the appropriate logger)
	 * @param message
	 *            the message to log
	 */
	public void warn(Object caller, Object message);

}
