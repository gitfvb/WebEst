package com.ibm.webest.processing.impl.administration;

import javax.annotation.Resource;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import com.ibm.webest.persistence.model.Estimate;
import com.ibm.webest.persistence.model.User;
import com.ibm.webest.persistence.service.DatabaseConnectionException;
import com.ibm.webest.persistence.service.EntityNotFoundException;
import com.ibm.webest.persistence.service.EstimateFacadeLocal;
import com.ibm.webest.persistence.service.UserFacadeLocal;
import com.ibm.webest.processing.ServiceException;
import com.ibm.webest.processing.administration.AuthenticationServiceLocal;
import com.ibm.webest.processing.administration.AuthenticationServiceRemote;

/**
 * Service for providing informations of the authentication mechanism and the
 * logged in user.
 * 
 * @author Gregor Schumm
 */
@DeclareRoles({ AuthenticationServiceLocal.ROLE_ADMINISTRATOR,
		AuthenticationServiceLocal.ROLE_ESTIMATOR,
		AuthenticationServiceLocal.ROLE_MANAGER })
@RolesAllowed({ AuthenticationServiceLocal.ROLE_ADMINISTRATOR,
		AuthenticationServiceLocal.ROLE_ESTIMATOR,
		AuthenticationServiceLocal.ROLE_MANAGER })
@Stateless
public class AuthenticationService implements AuthenticationServiceRemote,
		AuthenticationServiceLocal {
	@Resource
	private SessionContext session;
	@EJB
	private UserFacadeLocal userFacade;
	
	@EJB
	private EstimateFacadeLocal estimateFacade;
	

	private static Logger log = Logger.getLogger(AuthenticationService.class);

	public AuthenticationService() {
	}

	@Override
	public User getCurrentUser() throws ServiceException,
			EntityNotFoundException {
		String username = getLoginUserName();
		try {
			return userFacade.getUserById(username);
		} catch (EntityNotFoundException e) {
			throw e;
		} catch (DatabaseConnectionException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public boolean isEstimator() {
		return session.isCallerInRole(ROLE_ESTIMATOR);
	}

	@Override
	public boolean isManager() {
		return session.isCallerInRole(ROLE_MANAGER);
	}

	@Override
	public boolean isAdministrator() {
		return session.isCallerInRole(ROLE_ADMINISTRATOR);
	}

	@Override
	public boolean canEditEstimate(Estimate estimate) {
		if (isAdministrator())
			return true;
		if (isManager())
			return false;

		try {
			User u = getCurrentUser();
			if (estimate.getEstimator() == null || estimate.getDivision() == null)
				estimate = estimateFacade.loadFullEstimate(estimate);
			if (estimate.getEstimator().equals(u)
					|| estimate.getDivision().equals(u.getDivision())) {
				return true;
			}
		} catch (Exception e) {
			log.error("Error while checking user rights for estimate.", e);
		}
		return false;
	}
	
	@Override
	public boolean canViewEstimate(Estimate estimate) {
		if (canEditEstimate(estimate))
			return true;
		try {
			User u = getCurrentUser();
			if (estimate.getDivision() == null)
				estimate = estimateFacade.loadFullEstimate(estimate);
			if (estimate.getDivision().equals(u.getDivision())) {
				return true;
			}
		}  catch (Exception e) {
			log.error("Error while checking user rights for estimate.", e);
		}
		return false;
	}
	
	@Override
	public String getLoginUserName() {
		return session.getCallerPrincipal().getName();
	}

}
