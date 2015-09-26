package com.ibm.webest.processing.calculation;

/**
 * Exception thrown in case of an error on the EstimationManagementprocess
 * @author Andre Munzinger, David Dornseifer, Oliver Kreis
 */
public class COCOMOCalcException extends Exception {

	private static final long serialVersionUID = 1L;

	public COCOMOCalcException() {
		super();
	}

	public COCOMOCalcException(String message, Throwable cause) {
		super(message, cause);
	}

	public COCOMOCalcException(String message) {
		super(message);
	}

	public COCOMOCalcException(Throwable cause) {
		super(cause);
	}
}
