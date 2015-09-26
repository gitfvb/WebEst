package com.ibm.webest.persistence.service;

/**
 * Exception thrown in case of an error on the persistence layer.
 * 
 * @author Gregor Schumm
 * 
 */
public class PersistenceException extends Exception {
	private static final long serialVersionUID = 663767449019866278L;

	public PersistenceException() {
		super();
	}

	public PersistenceException(String message, Throwable cause) {
		super(message, cause);
	}

	public PersistenceException(String message) {
		super(message);
	}

	public PersistenceException(Throwable cause) {
		super(cause);
	}

}
