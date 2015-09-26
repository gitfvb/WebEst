package com.ibm.webest.processing.helpers;

import com.ibm.webest.processing.ServiceException;

/**
 * Exception thrown in case of an error on the conversion process
 * 
 * @author Wail Shakir
 * 
 */
public class ConversionException extends ServiceException {

	private static final long serialVersionUID = 1L;

	public ConversionException() {
		super();
	}

	public ConversionException(String message, Throwable cause) {
		super(message, cause);
	}

	public ConversionException(String message) {
		super(message);
	}

	public ConversionException(Throwable cause) {
		super(cause);
	}

}
