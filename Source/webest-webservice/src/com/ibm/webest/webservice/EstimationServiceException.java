package com.ibm.webest.webservice;

import com.ibm.webest.processing.ServiceException;

/**
 * Exception is thrown in case, a webservice error occurs 
 * @author David Dornseifer
 */
public class EstimationServiceException extends ServiceException {

	private static final long serialVersionUID = 1L;

	public EstimationServiceException() {
		super();
	}

	public EstimationServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public EstimationServiceException(String message) {
		super(message);
	}

	public EstimationServiceException(Throwable cause) {
		super(cause);
	}
}




