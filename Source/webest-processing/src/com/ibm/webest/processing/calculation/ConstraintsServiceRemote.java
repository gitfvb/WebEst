package com.ibm.webest.processing.calculation;

import javax.ejb.Remote;

import com.ibm.webest.processing.model.EstimationResult;

/**
 * ConstraintsServiceRemote Interface for checking if Estimationresult is valid
 * 
 * @author Andre Munzinger
 * @version 1.0
 */
@Remote
public interface ConstraintsServiceRemote {

	/**
	 * checkResult Main Check-Method
	 * 
	 * @param er
	 *            EstimationResult Object
	 * @return True if result is valid
	 * @throws ConstraintsException
	 *             Thrown if - EstimationResult was null - constraints are
	 *             violated
	 */
	public Boolean checkResult(EstimationResult er) throws ConstraintsException;
}
