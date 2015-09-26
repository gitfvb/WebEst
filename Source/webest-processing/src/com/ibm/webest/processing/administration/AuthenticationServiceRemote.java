package com.ibm.webest.processing.administration;

import javax.ejb.Remote;

import com.ibm.webest.persistence.model.Estimate;
import com.ibm.webest.persistence.model.User;
import com.ibm.webest.persistence.service.EntityNotFoundException;
import com.ibm.webest.processing.ServiceException;

/**
 * Service for providing informations of the authentication mechanism and the
 * logged in user.
 * 
 * @author Gregor Schumm
 */
@Remote
public interface AuthenticationServiceRemote {
	/**
	 * Name of administrator role.
	 */
	public static final String ROLE_ADMINISTRATOR = "admin";
	/**
	 * Name of manager role.
	 */
	public static final String ROLE_MANAGER = "manager";
	/**
	 * Name of estimator role.
	 */
	public static final String ROLE_ESTIMATOR = "estimator";

	/**
	 * @return a user entity of the current login user
	 * @throws ServiceException
	 *             if an internal error occurred
	 * @throws EntityNotFoundException
	 *             if the login user is not in the database
	 */
	public User getCurrentUser() throws ServiceException,
			EntityNotFoundException;

	/**
	 * @return true if the login user has the estimator role
	 */
	public boolean isEstimator();

	/**
	 * @return true if the login user has the manager role
	 */
	public boolean isManager();

	/**
	 * @return true if the login user has the administrator role
	 */
	public boolean isAdministrator();

	/**
	 * Checks if the current user is allowed to edit the given estimate.
	 * 
	 * @param est
	 *            the estimate the user wants to edit
	 * @return true if the user is allowed to edit
	 */
	public boolean canEditEstimate(Estimate est);

	/**
	 * @return the username of currently logged in user
	 */
	public String getLoginUserName();

	/**
	 * Checks if the current user is allowed to view the given estimate.
	 * 
	 * @param estimate
	 *            the estimate the user wants to view
	 * @return true if the user is allowed to view
	 */
	public boolean canViewEstimate(Estimate estimate);
}
