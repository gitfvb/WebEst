package com.ibm.webest.persistence.service;

import java.util.List;

import javax.ejb.Remote;

import com.ibm.webest.persistence.model.Certainty;
import com.ibm.webest.persistence.model.Division;
import com.ibm.webest.persistence.model.Estimate;
import com.ibm.webest.persistence.model.GranularityLevel;
import com.ibm.webest.persistence.model.GranularityQuestion;
import com.ibm.webest.persistence.model.PiHistoryCategory;
import com.ibm.webest.persistence.model.Solution;
import com.ibm.webest.persistence.model.StaffingShape;
import com.ibm.webest.persistence.model.UseCaseComplexity;
import com.ibm.webest.persistence.model.User;

/**
 * facade remote interface for solution <br>
 * SolutionFacadeRemote includes the query to Solution.
 * 
 * @author Wail Shakir
 * 
 */
@Remote
public interface SolutionFacadeRemote {
	/**
	 * get the granularity levels
	 * 
	 * @return a list of granularity level
	 * @throws DatabaseConnectionException
	 *             the database is diconnected.
	 */
	public List<GranularityLevel> getGranularityLevels()
			throws DatabaseConnectionException;

	/**
	 * Retrieves a granularity level by its id.
	 * 
	 * @param id
	 *            the id of the granularity level to retrieve
	 * @return the corresponding granularity level object
	 * @throws DatabaseConnectionException
	 *             an error occurred while connection to the database
	 * @throws EntityNotFoundException
	 *             granularity level with given id not found
	 */
	public GranularityLevel getGranularityLevelById(byte id)
			throws DatabaseConnectionException, EntityNotFoundException;

	/**
	 * get the granularity questions
	 * 
	 * @return a list of granularity questions
	 * @throws DatabaseConnectionException
	 *             the database is diconnected.
	 */
	public List<GranularityQuestion> getGranularityQuestions()
			throws DatabaseConnectionException;

	/**
	 * get use case complexities
	 * 
	 * @return a list of use case complexities
	 * @throws DatabaseConnectionException
	 *             the database is diconnected.
	 */
	public List<UseCaseComplexity> getUseCaseComplexities()
			throws DatabaseConnectionException;

	/**
	 * get use case certainties
	 * 
	 * @return a list of use case certainties
	 * @throws DatabaseConnectionException
	 *             the database is diconnected.
	 */
	public List<Certainty> getUseCaseCertainties()
			throws DatabaseConnectionException;

	/**
	 * get only the last "limit" modified solution by estimate.
	 * 
	 * @param estimate
	 *            which includes in solution
	 * @param limit
	 *            the amount of Solutions, which are to be returned.
	 * @return a list of solutions order by modify date
	 * @throws QueryException
	 *             the limit isn't >= 0
	 * @throws EntityNotFoundException
	 *             Entities are null.
	 * @throws DatabaseConnectionException
	 *             the database is diconnected.
	 */
	public List<Solution> getSolutions(Estimate estimate, int limit)
			throws EntityNotFoundException, DatabaseConnectionException,
			QueryException;

	/**
	 * get the pi history categories
	 * 
	 * @return a list of pi history categories
	 * @throws DatabaseConnectionException
	 *             the database is diconnected.
	 * 
	 */
	public List<PiHistoryCategory> getPiHistoryCategories()
			throws DatabaseConnectionException;

	/**
	 * clones a solution
	 * 
	 * @param solution
	 *            clone this solution
	 * @return a clone of the solution
	 * @throws EntityNotFoundException
	 *             Entities are null.
	 * @throws DatabaseConnectionException
	 *             the database is diconnected.
	 * @throws CloneException
	 */
	public Solution cloneSolution(Solution solution)
			throws EntityNotFoundException, DatabaseConnectionException,
			CloneException;

	/**
	 * removes a Solution
	 * 
	 * @param solutionId
	 *            remove this Solution
	 * @throws EntityNotFoundException
	 *             Entities are null.
	 * @throws DatabaseConnectionException
	 *             the database is diconnected.
	 * 
	 */
	public void deleteSolution(long solutionId)
			throws DatabaseConnectionException;

	/**
	 * loads a Solution by ID
	 * 
	 * @param solutionId
	 *            id of a solution
	 * @throws DatabaseConnectionException
	 *             the database is diconnected.
	 * @throws EntityNotFoundException 
	 */
	public Solution loadSolutionById(long solutionId)
			throws DatabaseConnectionException, EntityNotFoundException;

	/**
	 * saves a Solution by ID
	 * 
	 * @param solution
	 *            solution
	 * @return the saved solution
	 * @throws EntityNotFoundException
	 *             Entities are null.
	 * @throws DatabaseConnectionException
	 *             the database is diconnected.
	 */
	public Solution saveSolution(Solution solution)
			throws EntityNotFoundException, DatabaseConnectionException;

	/**
	 * 
	 * Reloads the solution from database with all first level associations
	 * loaded immediately (no lazy loading).
	 * 
	 * @param solution
	 *            the solution to reload
	 * @return the loaded solution
	 * @throws EntityNotFoundException
	 *             solution does not longer exist in database
	 * @throws DatabaseConnectionException
	 *             an error occurred while connecting to the database
	 */
	public Solution loadFullSolution(Solution solution)
			throws EntityNotFoundException, DatabaseConnectionException;

	/**
	 * @return all available staffing shapes
	 * @throws DatabaseConnectionException
	 *             an error occurred while retrieving entities
	 */
	public List<StaffingShape> getStaffingShapes()
			throws DatabaseConnectionException;

	/**
	 * @param div the division
	 * @param limit max. number of entities to retrieve
	 * @return all <code>limit</code> solutions of the given solution, ordered by modify date
	 * @throws EntityNotFoundException
	 * @throws DatabaseConnectionException
	 * @throws QueryException
	 */
	List<Solution> getSolutionsByDivision(Division div, int limit)
			throws EntityNotFoundException, DatabaseConnectionException,
			QueryException;

	/**
	 * @param user the user
	 * @param limit max. number of entities to retrieve
	 * @return all <code>limit</code> solutions of the given user, ordered by modify date
	 * @throws EntityNotFoundException
	 * @throws DatabaseConnectionException
	 * @throws QueryException
	 */
	List<Solution> getSolutionsByUser(User user, int limit)
			throws EntityNotFoundException, DatabaseConnectionException,
			QueryException;
}
