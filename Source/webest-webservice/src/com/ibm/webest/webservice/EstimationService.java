package com.ibm.webest.webservice;

import java.util.List;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebService;

import com.ibm.webest.persistence.model.Estimate;
import com.ibm.webest.persistence.model.Solution;
import com.ibm.webest.persistence.service.EntityNotFoundException;
import com.ibm.webest.processing.administration.AuthenticationServiceLocal;
import com.ibm.webest.processing.administration.EstimationManagerServiceLocal;
import com.ibm.webest.processing.administration.LoggingServiceLocal;
import com.ibm.webest.processing.model.Report;

/**
 * EstimationService: Provides the webservice interfaces to EstimationManager,
 * to connect external systems
 * 
 * @author Andre Munzinger
 * @version 1.0
 */
@DeclareRoles({ AuthenticationServiceLocal.ROLE_ADMINISTRATOR,
		AuthenticationServiceLocal.ROLE_ESTIMATOR,
		AuthenticationServiceLocal.ROLE_MANAGER })
@RolesAllowed({ AuthenticationServiceLocal.ROLE_ADMINISTRATOR,
		AuthenticationServiceLocal.ROLE_ESTIMATOR,
		AuthenticationServiceLocal.ROLE_MANAGER })
@WebService(serviceName = "WebEstWebService")
@Stateless
public class EstimationService implements EstimationServiceLocal,
		EstimationServiceRemote {

	@EJB
	private EstimationManagerServiceLocal estMgr;

	@EJB
	private LoggingServiceLocal logger;

	@Override
	@WebMethod
	public List<Estimate> getAllEstimates() throws EstimationServiceException {
		try {
			logger.info(this, "Web service access to getAllEstimates.");
			return estMgr.getAllEstimates();
		} catch (Exception e) {
			this.logger
					.error(this,
							"getAllEstimates: Error occurred while returning all estimates",
							e);
			throw new EstimationServiceException(
					"Error occurred while returning all estimates", e);
		}
	}

	@Override
	@WebMethod
	public Report generateReport(int solutionId)
			throws EstimationServiceException, EntityNotFoundException {
		Solution sol;
		try {
			logger.info(this, "Web service access to generateReport.");
			sol = estMgr.getSolutionById(solutionId);
			return estMgr.generateReport(sol);
		} catch (Exception e) {
			this.logger
					.error(this,
							"generateReport: An error occurred while trying to generate report.",
							e);
			throw new EstimationServiceException(
					"An Error occurred while trying to generate report.", e);
		}

	}

}