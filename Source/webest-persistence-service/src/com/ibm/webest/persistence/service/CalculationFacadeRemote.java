package com.ibm.webest.persistence.service;

import java.util.Map;

import javax.ejb.Remote;

/**
 * Provides functionality to retrieve calculation relevant data from the
 * database.<br>
 * Primarily used for parameter lookup.
 * 
 * @author Gregor Schumm
 * 
 */
@Remote
public interface CalculationFacadeRemote {
	/**
	 * Retrieves the PP for the given PI.
	 * 
	 * @param pi
	 *            the PI to search for
	 * @return the PP value for the given PI
	 * @throws EntityNotFoundException
	 *             given PI was not found in database
	 * @throws DatabaseConnectionException
	 *             their was an error while connecting to the database
	 */
	public int getPpByPi(int pi) throws EntityNotFoundException,
			DatabaseConnectionException;

	/**
	 * Retrieves the calculation parameter value for the given key.
	 * 
	 * @param key
	 *            the alphanumeric key to search for
	 * @return the parameter value for the given key
	 * @throws EntityNotFoundException
	 *             given key was not found in database
	 * @throws DatabaseConnectionException
	 *             their was an error while connecting to the database
	 */
	public double getCalculationParameter(String key)
			throws EntityNotFoundException, DatabaseConnectionException;

	/**
	 * Retrieves all calculation parameter in a key->value map.
	 * 
	 * @return a map with all calculation parameters in a key->value association
	 * @throws DatabaseConnectionException
	 *             their was an error while connecting to the database
	 */
	public Map<String, Double> getCalculationParameters()
			throws DatabaseConnectionException;
}
