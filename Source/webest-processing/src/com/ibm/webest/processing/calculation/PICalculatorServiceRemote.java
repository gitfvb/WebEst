package com.ibm.webest.processing.calculation;

import javax.ejb.Remote;

/**
 * PICalculatorServiceRemote
 * Interface for PI-related calculations
 * 
 * @author Andre Munzinger
 * @version 1.0
 */
@Remote
public interface PICalculatorServiceRemote {
	
	
	/**
	 * calculatePP Calculates the PP
	 * 
	 * @param pi
	 * @return pp
	 * @throws PICalculatorException 
	 * Thrown if PI is zero or less
	 */
	public int calculatePP(int pi) throws PICalculatorException;
	
	
}
