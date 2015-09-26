package com.ibm.webest.processing.calculation;

/**
 * Exception thrown in case of an error on the ConstraintsServiceClass
 * @author Andre Munzinger
 */
public class ConstraintsException extends Exception {

	private static final long serialVersionUID = 1L;

	public ConstraintsException() {
		super();
	}

	public ConstraintsException(String message, Throwable cause) {
		super(message, cause);
	}

	public ConstraintsException(String message) {
		super(message);
	}

	public ConstraintsException(Throwable cause) {
		super(cause);
	}
}
