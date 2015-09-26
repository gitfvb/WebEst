package com.ibm.webest.webservice;

import java.util.List;

import javax.ejb.Remote;

import com.ibm.webest.persistence.model.Estimate;
import com.ibm.webest.persistence.service.EntityNotFoundException;
import com.ibm.webest.processing.model.Report;

/**
 * EstimationServiceRemote: Provides the webservice interfaces to
 * EstimationManager, to connect external systems.
 * 
 * @author David Dornseifer
 * 
 */

@Remote
public interface EstimationServiceRemote {
	/**
	 * generateReport: Implementation of generateReport_webservice method with
	 * authentication
	 * 
	 * @param solutionId
	 *            : id of the specific solution thats report should be be
	 *            created.
	 */
	public Report generateReport(int solutionId)
			throws EstimationServiceException, EntityNotFoundException;

	/**
	 * getAllEstimates: Implementation of getAllEstimation_webservice method
	 * with authentication
	 */
	public List<Estimate> getAllEstimates() throws EstimationServiceException;

}
