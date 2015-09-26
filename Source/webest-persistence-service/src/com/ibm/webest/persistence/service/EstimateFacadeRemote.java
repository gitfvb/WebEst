package com.ibm.webest.persistence.service;

import java.util.List;

import javax.ejb.Remote;
import com.ibm.webest.persistence.model.*;

/**
 * facade remote interface for estimate <br>
 * This class provides the functionality to create, edit, delete, cloning and
 * the access of Estimates.
 * 
 * @author Wail Shakir
 * 
 */
@Remote
public interface EstimateFacadeRemote {

	/**
	 * get all estimates by division.
	 * 
	 * @param division
	 *           the division for which all estimates should be returned.
	 * @return a list of estimates order by name
	 * @throws QueryException
	 *             the limit isn't >= -1
	 * @throws EntityNotFoundException
	 *             division is null.
	 * @throws DatabaseConnectionException
	 *             an error occurred while connecting to the database
	 */
	public List<Estimate> getEstimatesByDivision(Division division)
			throws EntityNotFoundException, DatabaseConnectionException,
			QueryException;

	/**
	 * get only the last "limit" modified estimates by division.
	 * 
	 * @param division
	 *           the division for which all estimates should be returned.
	 * @param limit
	 *            the amount of Estimates, which are to be returned.
	 * @return a list of estimates order by modify date
	 * @throws QueryException
	 *             the limit isn't >= -1
	 * @throws EntityNotFoundException
	 *             division are null.
	 * @throws DatabaseConnectionException
	 *             an error occurred while connecting to the database
	 */
	public List<Estimate> getEstimatesByDivision(Division division, int limit)
			throws QueryException, EntityNotFoundException,
			DatabaseConnectionException;

	/**
	 * get all estimates
	 * 
	 * @return a list of estimates order by name
	 * @throws QueryException
	 *             the limit isn't >= -1
	 * 
	 * @throws DatabaseConnectionException
	 *             an error occurred while connecting to the database
	 */
	public List<Estimate> getAllEstimates() throws DatabaseConnectionException,
			QueryException;

	/**
	 * get only the last "limit" modified estimates.
	 * 
	 * @param limit
	 *            the amount of Estimates, which are to be returned.
	 * @return a list of estimates order by modify date
	 * @throws QueryException
	 *             the limit isn't >= -1
	 * @throws DatabaseConnectionException
	 *             an error occurred while connecting to the database
	 */
	public List<Estimate> getAllEstimates(int limit) throws QueryException,
			DatabaseConnectionException;

	/**
	 * get all estimates by user.
	 * 
	 * @param user
	 *            the estimator,who wants to access his estimates.
	 * @return a list of estimates order by name
	 * @throws QueryException
	 *             the limit isn't >= -1
	 * @throws EntityNotFoundException
	 *             Entities are null.
	 * @throws DatabaseConnectionException
	 *             the database is disconnected.
	 */
	public List<Estimate> getEstimatesByUser(User user)
			throws EntityNotFoundException, DatabaseConnectionException,
			QueryException;

	/**
	 * get only the last "limit" modified estimates by user.
	 * 
	 * @param user
	 *           the estimator,who wants to access his estimates.
	 * @param limit
	 *            the amount of Estimates, which are to be returned.
	 * @return a list of estimates order by modify date
	 * @throws QueryException
	 *             the limit isn't >= -1
	 * @throws EntityNotFoundException
	 *             Entities are null.
	 * @throws DatabaseConnectionException
	 *             an error occurred while connecting to the database
	 */
	public List<Estimate> getEstimatesByUser(User user, int limit)
			throws QueryException, EntityNotFoundException,
			DatabaseConnectionException;

	/**
	 * get all estimate by user or by division.
	 * 
	 * @param user
	 *           the estimator,who wants to access his estimates.
	 * @param division
	 *           the division for which all estimates should be returned.
	 * @return a list of estimates order by name
	 * @throws QueryException
	 *             the limit isn't >= -1
	 * @throws EntityNotFoundException
	 *             Entities are null.
	 * @throws DatabaseConnectionException
	 *             an error occurred while connecting to the database
	 */
	public List<Estimate> getEstimateByUserOrDivision(User user,
			Division division) throws EntityNotFoundException,
			DatabaseConnectionException, QueryException;

	/**
	 * get all the estimate by user
	 * "if(user != null && division == null && limit == -1)".
	 * 
	 * get all the estimate by division
	 * "if(division != null  && user == null && limit == -1)".
	 * 
	 * get only the last "limit" modifies estimates by user.
	 * "if(user != null && division == null && limit >= 0)".
	 * 
	 * get only the last "limit" modifies estimates by user.
	 * "if(division != null  && user == null && limit >= 0)".
	 * 
	 * get all the estimate by user or by division.
	 * "if(user != null && division != null && limit == -1)".
	 * 
	 * get only the last "limit" modifies estimates by user or by division.
	 * "if(user != null && division != null && limit >= 0)".
	 * 
	 * @param user
	 *           the estimator,who wants to access his estimates.
	 * @param division
	 *           the division for which all estimates should be returned.
	 * @param limit
	 *            the amount of Estimates, which are to be returned.
	 * @return a list of estimates order by name "if (limit == -1)" or by modify
	 *         date "if (limit >= 0)"
	 * @throws QueryException
	 *             the limit isn't >= -1
	 * @throws DatabaseConnectionException
	 *             an error occurred while connecting to the database
	 */
	public List<Estimate> getEstimateByUserOrDivision(User user,
			Division division, int limit) throws QueryException,
			DatabaseConnectionException;

	/**
	 * save a modified estimate
	 * 
	 * @param estimate
	 *            save this estimate
	 * @return The saved Estimate
	 * @throws EntityNotFoundException
	 *             estimate is null.
	 * @throws DatabaseConnectionException
	 *             an error occurred while connecting to the database
	 */
	public Estimate saveEstimate(Estimate estimate)
			throws EntityNotFoundException, DatabaseConnectionException;

	/**
	 * Reloads the estimate from database with all first level associations
	 * loaded immediately (no lazy loading).
	 * 
	 * @param est
	 *            the estimate to reload
	 * @return the loaded estimate
	 * 
	 * @throws EntityNotFoundException
	 *             estimate does not longer exist in database
	 * @throws DatabaseConnectionException
	 *             an error occurred while connecting to the database
	 */
	public Estimate loadFullEstimate(Estimate est)
			throws EntityNotFoundException, DatabaseConnectionException;

	/**
	 * remove an estimate
	 * 
	 * @param estimate
	 *            remove this estimate
	 * @throws EntityNotFoundException
	 *             estimate is null.
	 * @throws DatabaseConnectionException
	 *             the database is disconnected.
	 */
	public void deleteEstimate(Estimate estimate)
			throws EntityNotFoundException, DatabaseConnectionException;
}
