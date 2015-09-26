package com.ibm.webest.processing.calculation;

import javax.ejb.Remote;

import com.ibm.webest.processing.model.COCOMOResult;


/**
 * COCOMOCalculationServiceRemote 
 * Interface for calculations within the COCOMO I model
 * 
 * @author Andre Munzinger
 * @author David Dornseifer
 * @version 1.0
 */
@Remote
public interface COCOMOCalculationServiceRemote {
	
			
	/**
	 * COCOMOResultService
	 * Organizes the whole COCOMO-Calculationprocess
	 * @return endresult of the COCOMO-Calculation
	 * @throws COCOMOCalcException 
	 * Thrown if
	 * - Sourcelines of code are zero or less
	 */
	public COCOMOResult calc(float sloc) throws COCOMOCalcException;
}
