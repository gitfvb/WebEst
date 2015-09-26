package com.ibm.webest.webservice;

import javax.ejb.Local;

/**
 * EstimationServiceLocal: Provides the webservice interfaces to
 * EstimationManager, to connect external systems.
 * 
 * @author David Dornseifer
 * 
 */
@Local
public interface EstimationServiceLocal extends EstimationServiceRemote {

}
