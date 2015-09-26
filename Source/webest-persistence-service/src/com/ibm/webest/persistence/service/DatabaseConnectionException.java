package com.ibm.webest.persistence.service;

/**
 * Exception thrown if an error occurs while connection to the database.
 * 
 * @author Gregor Schumm
 * 
 */
public class DatabaseConnectionException extends PersistenceException {
	private static final long serialVersionUID = -649504132446816562L;
	protected static final String DEFAULT_MESSAGE = "An error occurred while connecting to the database.";

	public DatabaseConnectionException() {
		super();
	}

	public DatabaseConnectionException(String message, Throwable cause) {
		super(message, cause);
	}

	public DatabaseConnectionException(String message) {
		super(message);
	}

	public DatabaseConnectionException(Throwable cause) {
		this(DEFAULT_MESSAGE, cause);
	}

}
