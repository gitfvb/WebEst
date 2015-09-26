package com.ibm.webest.persistence.service;

/**
 * Thrown if an entity is not found in database.
 * @author Gregor Schumm
 */
public class EntityNotFoundException extends PersistenceException {
	private static final long serialVersionUID = 3539241714646031681L;

	public EntityNotFoundException() {
		super();
	}

	public EntityNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public EntityNotFoundException(String message) {
		super(message);
	}

	public EntityNotFoundException(Throwable cause) {
		super(cause);
	}

}
