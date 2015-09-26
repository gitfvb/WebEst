package com.ibm.webest.processing;

/**
 * Base exception for errors occurring in service layer.
 * @author Gregor Schumm
 *
 */
public class ServiceException extends Exception {
	private static final long serialVersionUID = 87729187721783610L;

	public ServiceException() {
	}

	public ServiceException(String message) {
		super(message);
	}

	public ServiceException(Throwable cause) {
		super(cause);
	}

	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}

}
