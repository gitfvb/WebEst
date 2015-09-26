package com.ibm.webest.processing.calculation;

import javax.ejb.Remote;

import com.ibm.webest.processing.model.RayleighResult;

/**
 * Calculates the Rayleighcurve 
 * Remote interface
 * @author Andre Munzinger
 */
@Remote
public interface RayleighCalculationServiceRemote {

	/**
	 * COCOMOResultService
	 * Organizes the whole COCOMO-Calculationprocess
	 * @return endresult of the COCOMO-Calculation
	 * @throws RayleighCalculationException 
	 */
	public RayleighResult calc(float pEff, float td) throws RayleighCalculationException;
}
