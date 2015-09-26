package com.ibm.webest.processing.calculation;

import com.ibm.webest.processing.ServiceException;

/**
 * Exception thrown in case of an error on the EstimationManagementprocess
 * 
 * @author Wail Shakir
 * 
 */
public class ReportException extends ServiceException {

	private static final long serialVersionUID = 1L;

	public ReportException() {
		super();
	}

	public ReportException(String message, Throwable cause) {
		super(message, cause);
	}

	public ReportException(String message) {
		super(message);
	}

	public ReportException(Throwable cause) {
		super(cause);
	}

}
