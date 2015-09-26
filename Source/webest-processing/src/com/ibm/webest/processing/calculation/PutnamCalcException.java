package com.ibm.webest.processing.calculation;

/**
 * Exception thrown in case of an error on the EstimationManagementprocess
 * @author Andre Munzinger, David Dornseifer, Oliver Kreis
 */
public class PutnamCalcException extends Exception {

	private static final long serialVersionUID = 1L;

	public PutnamCalcException() {
		super();
	}

	public PutnamCalcException(String message, Throwable cause) {
		super(message, cause);
	}

	public PutnamCalcException(String message) {
		super(message);
	}

	public PutnamCalcException(Throwable cause) {
		super(cause);
	}
}
