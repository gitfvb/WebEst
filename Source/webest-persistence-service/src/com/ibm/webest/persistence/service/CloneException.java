package com.ibm.webest.persistence.service;

/**
 * This Exception is thrown if an object could not be cloned.
 * 
 * @author Wail Shakir
 * 
 */
public class CloneException extends PersistenceException {

	private static final long serialVersionUID = -4974733324520318947L;

	public CloneException() {
		super();
	}

	public CloneException(String message, Throwable cause) {
		super(message, cause);
	}

	public CloneException(String message) {
		super(message);
	}

	public CloneException(Throwable cause) {
		super(cause);
	}

}
