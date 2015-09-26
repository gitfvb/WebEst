package com.ibm.webest.processing.calculation;

import javax.ejb.Remote;

import com.ibm.webest.persistence.service.DatabaseConnectionException;
import com.ibm.webest.persistence.service.EntityNotFoundException;
import com.ibm.webest.processing.model.COCOMOResult;
import com.ibm.webest.processing.model.PutnamResult;

/**
 * PutnamCalculationServiceRemote Interface for calculations within the Putnam
 * model
 * 
 * @author Andre Munzinger, David Dornseifer
 * @version 1.0
 */
@Remote
public interface PutnamCalculationServiceRemote {

	/**
	 * calc Mainprocess of Putnamcalcprocess This method calls all other
	 * functions who calculate the estimates
	 * 
	 * @return Endresult of the Putnam-Calculation
	 * @throws PutnamCalcException
	 *             Thrown if - Sourcelines of code are zero or less - PP is zero
	 *             or less - COCOMOResult is null
	 * @throws DatabaseConnectionException
	 *             If there was a problem in the DB
	 * @throws EntityNotFoundException
	 *             If there was anexception in the calculationFacade
	 */
	public PutnamResult calc(float sloc, float pp, COCOMOResult cocomoResults)
			throws PutnamCalcException, EntityNotFoundException,
			DatabaseConnectionException;

	/**
	 * getE Helps to get the right E-Value from DB
	 * 
	 * @param sloc
	 *            Sourcelines of code
	 * @return the parameter e
	 * @throws PutnamCalcException
	 *             Thrown if - Sourcelines of code are zero or less - Exception
	 *             in the calculationFacade
	 */
	public float getE(int sloc) throws PutnamCalcException;
}
