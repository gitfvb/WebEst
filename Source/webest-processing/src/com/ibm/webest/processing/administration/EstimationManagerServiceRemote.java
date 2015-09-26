package com.ibm.webest.processing.administration;

import java.util.List;

import javax.ejb.Remote;

import com.ibm.webest.persistence.model.EffortUnit;
import com.ibm.webest.persistence.model.Estimate;
import com.ibm.webest.persistence.model.ProjectEnvironment;
import com.ibm.webest.persistence.model.Solution;
import com.ibm.webest.persistence.model.Template;
import com.ibm.webest.persistence.service.EntityNotFoundException;
import com.ibm.webest.processing.model.Report;

/**
 * Service for tasks related to the estimates and the calculation process.
 * 
 * @author Andre Munzinger
 * @author Gregor Schumm
 * @author Wail Shakir
 */
@Remote
public interface EstimationManagerServiceRemote {

	/**
	 * Generate a report for the given solution.
	 * 
	 * @return the generated report
	 * @throws EstimationManagerException
	 */
	public Report generateReport(Solution solution)
			throws EstimationManagerException;

	/**
	 * saves an estimation
	 * 
	 * @param estimate
	 *            given estimation to save
	 * @return the saved estimate
	 * @throws GuiServiceException
	 */
	public Estimate saveEstimate(Estimate estimate)
			throws EstimationManagerException;

	/**
	 * deletes an estimation
	 * 
	 * @param estimate
	 *            estimate to delete
	 * @throws GuiServiceException
	 */
	public void deleteEstimate(Estimate estimate)
			throws EstimationManagerException;

	/**
	 * reloads an estimate from database with all first level associations
	 * 
	 * @param estimate
	 *            the estimate to reload
	 * @return the reloaded estimate
	 * @throws GuiServiceException
	 */
	public Estimate reloadEstimate(Estimate estimate)
			throws EstimationManagerException;

	/**
	 * reloads a solution from database with all first level associations
	 * 
	 * @param solution
	 *            the solution to reload
	 * @return the reloaded solution
	 * @throws GuiServiceException
	 */
	public Solution reloadSolution(Solution solution)
			throws EstimationManagerException;

	/**
	 * Retrieves all estimates, the current user is allowed to see.
	 * 
	 * @return a list, which contains the estimates.
	 * @throws GuiServiceException
	 *             if an error occurred
	 */
	public List<Estimate> getAllEstimates() throws EstimationManagerException;

	/**
	 * Create an estimate and fill its project environment with the given
	 * template values.
	 * 
	 * @return the created estimate
	 * @throws EstimationManagerException
	 *             internal error occurred
	 */
	public Estimate createEstimate(Template tpl)
			throws EstimationManagerException;

	/**
	 * Creates an empty estimate.
	 * 
	 * @return the created estimate
	 * @throws EstimationManagerException
	 */
	public Estimate createEstimate() throws EstimationManagerException;

	/**
	 * Clone the given estimate. Primary keys are set to their default values.
	 * 
	 * @param est
	 *            the estimation to clone
	 * @return the cloned estimation
	 * @throws EstimationManagerException
	 */
	public Estimate cloneEstimate(Estimate est)
			throws EstimationManagerException;

	/**
	 * Clone the given solution and add it to the associated estimate. Primary
	 * keys are set to their default values.
	 * 
	 * @param sol
	 *            the solution to clone
	 * @return the cloned solution
	 * @throws EstimationManagerException
	 */
	public Solution cloneSolutionForEstimate(Solution sol)
			throws EstimationManagerException;

	/**
	 * Deletes the given solution from the associated estimate.
	 * 
	 * @param sol
	 *            the solution to delete
	 * @throws EstimationManagerException
	 *             internal error occurred
	 */
	public void deleteSolution(Solution sol) throws EstimationManagerException;

	/**
	 * Creates an empty solution add it to the given estimate.
	 * 
	 * @return the created estimate
	 * @throws EstimationManagerException
	 */
	public Solution createSolution(Estimate est)
			throws EstimationManagerException;

	/**
	 * Restores the template values for the given estimate.
	 * 
	 * @param est
	 * @throws EstimationManagerException
	 */
	public void restoreTemplateDefaults(Estimate est)
			throws EstimationManagerException;

	/**
	 * Saves the solution and the corresponding estimate.
	 * 
	 * @param sol
	 *            the solution to save
	 * @throws EstimationManagerException
	 */
	public Solution saveSolution(Solution sol)
			throws EstimationManagerException;

	/**
	 * Saves the given project environment as new template with the given name
	 * 
	 * @param name
	 *            the name of the new template
	 * @param description
	 *            the discription of the new template
	 * @param projectEnvironment
	 *            the project environment with the values to copy
	 * @throws EstimationManagerException
	 */
	public void saveAsTemplate(String name, String description,
			ProjectEnvironment projectEnvironment)
			throws EstimationManagerException;

	/**
	 * Retrieves the recently modified estimates of the current user.
	 * 
	 * @param limit
	 *            max. numbers of entities to retrieve
	 * @return the found list of entities
	 * @throws EstimationManagerException
	 */
	public List<Estimate> getRecentUserEstimates(int limit)
			throws EstimationManagerException;

	/**
	 * Retrieves the recently modified estimates of the current user's division.
	 * 
	 * @param limit
	 *            max. numbers of entities to retrieve
	 * @return the found list of entities
	 * @throws EstimationManagerException
	 */
	public List<Estimate> getRecentDivisionEstimates(int limit)
			throws EstimationManagerException;

	/**
	 * Retrieves the recently modified solutions of the current user.
	 * 
	 * @param limit
	 *            max. numbers of entities to retrieve
	 * @return the found list of entities
	 * @throws EstimationManagerException
	 */
	public List<Solution> getRecentUserSolutions(int limit)
			throws EstimationManagerException;

	/**
	 * Retrieves the recently modified solutions of the current user's division.
	 * 
	 * @param limit
	 *            max. numbers of entities to retrieve
	 * @return the found list of entities
	 * @throws EstimationManagerException
	 */
	public List<Solution> getRecentDivisionSolutions(int limit)
			throws EstimationManagerException;

	/**
	 * Method can be called on application startup to initialize resources.
	 */
	public void startup();

	/**
	 * Fetches a solution with the given id from the database
	 * 
	 * @param solutionId
	 *            the id of the solution
	 * @return the solution with the given id
	 * @throws EstimationManagerException
	 * @throws EntityNotFoundException
	 */
	public Solution getSolutionById(int solutionId)
			throws EstimationManagerException, EntityNotFoundException;

	/**
	 * @return the reference effort unit (should be the unit of the values in
	 *         the report)
	 * @throws EstimationManagerException
	 */
	public EffortUnit getReferenceEffortUnit()
			throws EstimationManagerException;
}