package com.ibm.webest.persistence.service;

/**
 * Exception thrown while trying to execute an invalid query.
 * 
 * @author Gregor Schumm
 * 
 */
public class QueryException extends PersistenceException {
	private static final long serialVersionUID = 3337060831521587889L;

	public QueryException() {
		super();
	}

	public QueryException(String message, Throwable cause) {
		super(message, cause);
	}

	public QueryException(String message) {
		super(message);
	}

	public QueryException(Throwable cause) {
		super(cause);
	}

}
