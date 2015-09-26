package com.ibm.webest.processing.calculation;

/**
 * Exception thrown in case of an error on the EstimationManagementprocess
 * 
 * @author Andre Munzinger, David Dornseifer, Oliver Kreis
 */
public class SizingCalculatorException extends Exception {

	private static final long serialVersionUID = 1L;

	public SizingCalculatorException() {
		super();
	}

	public SizingCalculatorException(String message, Throwable cause) {
		super(message, cause);
	}

	public SizingCalculatorException(String message) {
		super(message);
	}

	public SizingCalculatorException(Throwable cause) {
		super(cause);
	}
}
