package com.ibm.webest.processing.administration;

import com.ibm.webest.processing.ServiceException;

/**
 * Exception for errors occurring in GuiService.
 * 
 * @author Gregor Schumm
 * 
 */
public class GuiServiceException extends ServiceException {
	private static final long serialVersionUID = 8493341725843787729L;

	public GuiServiceException() {
		super();
	}

	public GuiServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public GuiServiceException(String message) {
		super(message);
	}

	public GuiServiceException(Throwable cause) {
		super(cause);
	}
}
