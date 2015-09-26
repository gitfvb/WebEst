package com.ibm.webest.processing.calculation;

import javax.ejb.Local;

/**
 * Calculates the Rayleighcurve 
 * Local interface
 * @see RayleighCalculationServiceRemote
 * @author Andre Munzinger
 */
@Local
public interface RayleighCalculationServiceLocal extends RayleighCalculationServiceRemote{}