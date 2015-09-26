package com.ibm.webest.processing.administration;

import com.ibm.webest.processing.ServiceException;

/**
 * Exception thrown in case of an error on the EstimationManagementprocess
 * 
 * @author Andre Munzinger
 */
public class EstimationManagerException extends ServiceException {

	private static final long serialVersionUID = 1L;

	public EstimationManagerException() {
		super();
	}

	public EstimationManagerException(String message, Throwable cause) {
		super(message, cause);
	}

	public EstimationManagerException(String message) {
		super(message);
	}

	public EstimationManagerException(Throwable cause) {
		super(cause);
	}
}
