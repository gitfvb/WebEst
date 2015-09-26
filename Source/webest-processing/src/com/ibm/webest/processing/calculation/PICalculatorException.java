package com.ibm.webest.processing.calculation;

/**
 * Exception thrown in case of an error on the EstimationManagementprocess
 * @author Andre Munzinger, David Dornseifer, Oliver Kreis
 */
public class PICalculatorException extends Exception {

	private static final long serialVersionUID = 1L;

	public PICalculatorException() {
		super();
	}

	public PICalculatorException(String message, Throwable cause) {
		super(message, cause);
	}

	public PICalculatorException(String message) {
		super(message);
	}

	public PICalculatorException(Throwable cause) {
		super(cause);
	}
}
