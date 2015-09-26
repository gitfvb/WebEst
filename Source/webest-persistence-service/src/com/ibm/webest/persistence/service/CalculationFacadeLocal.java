package com.ibm.webest.persistence.service;

import javax.ejb.Local;

/**
 * Provides functionality to retrieve calculation relevant data from the
 * database.<br>
 * Primarily used for parameter lookup.
 * 
 * @author Gregor Schumm
 * 
 */
@Local
public interface CalculationFacadeLocal extends CalculationFacadeRemote {
}
