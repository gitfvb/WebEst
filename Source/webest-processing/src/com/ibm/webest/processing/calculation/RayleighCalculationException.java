package com.ibm.webest.processing.calculation;

/**
 * Exception thrown in case of an error on the EstimationManagementprocess
 * @author Andre Munzinger, David Dornseifer, Oliver Kreis
 */
public class RayleighCalculationException extends Exception {

	private static final long serialVersionUID = 1L;

	public RayleighCalculationException() {
		super();
	}

	public RayleighCalculationException(String message, Throwable cause) {
		super(message, cause);
	}

	public RayleighCalculationException(String message) {
		super(message);
	}

	public RayleighCalculationException(Throwable cause) {
		super(cause);
	}
}
