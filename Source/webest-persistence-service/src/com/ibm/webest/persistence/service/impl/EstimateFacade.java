package com.ibm.webest.persistence.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.ibm.webest.persistence.model.Division;
import com.ibm.webest.persistence.model.Estimate;
import com.ibm.webest.persistence.model.Milestone;
import com.ibm.webest.persistence.model.Phase;
import com.ibm.webest.persistence.model.ProjectEnvironment;
import com.ibm.webest.persistence.model.Solution;
import com.ibm.webest.persistence.model.SolutionStaffingShape;
import com.ibm.webest.persistence.model.User;
import com.ibm.webest.persistence.service.AuthenticationRoles;
import com.ibm.webest.persistence.service.DatabaseConnectionException;
import com.ibm.webest.persistence.service.EntityNotFoundException;
import com.ibm.webest.persistence.service.EstimateFacadeLocal;
import com.ibm.webest.persistence.service.EstimateFacadeRemote;
import com.ibm.webest.persistence.service.QueryException;
import com.ibm.webest.persistence.service.SolutionFacadeLocal;

/**
 * Facade for entity Estimate.<br>
 * Provides functionality to retrieve available values for the estimate
 * parameters.
 * 
 * @author Wail Shakir
 * 
 */
@DeclareRoles({ AuthenticationRoles.ROLE_ADMINISTRATOR,
		AuthenticationRoles.ROLE_ESTIMATOR, AuthenticationRoles.ROLE_MANAGER })
@RolesAllowed({ AuthenticationRoles.ROLE_ADMINISTRATOR,
		AuthenticationRoles.ROLE_ESTIMATOR, AuthenticationRoles.ROLE_MANAGER })
@SuppressWarnings("unchecked")
@Stateless
public class EstimateFacade implements EstimateFacadeRemote,
		EstimateFacadeLocal {

	@PersistenceContext
	private EntityManager entityManager;

	@EJB
	private SolutionFacadeLocal solutionFacade;

	/**
	 * Default constructor.
	 */
	public EstimateFacade() {

	}

	@Override
	public List<Estimate> getEstimatesByDivision(Division division)
			throws QueryException, DatabaseConnectionException,
			EntityNotFoundException {

		return getEstimatesByDivision(division, -1);
	}

	@Override
	public List<Estimate> getEstimatesByDivision(Division division, int limit)
			throws QueryException, DatabaseConnectionException,
			EntityNotFoundException {

		if (division == null)
			throw new EntityNotFoundException("Division has to be not null.");

		return getEstimateByUserOrDivision(null, division, limit);
	}

	@Override
	public List<Estimate> getAllEstimates() throws QueryException,
			DatabaseConnectionException {
		return getAllEstimates(-1);
	}

	@Override
	public List<Estimate> getAllEstimates(int limit) throws QueryException,
			DatabaseConnectionException {

		return getEstimateByUserOrDivision(null, null, limit);
	}

	@Override
	public List<Estimate> getEstimatesByUser(User user) throws QueryException,
			EntityNotFoundException, DatabaseConnectionException {
		return getEstimatesByUser(user, -1);
	}

	@Override
	public List<Estimate> getEstimatesByUser(User user, int limit)
			throws QueryException, EntityNotFoundException,
			DatabaseConnectionException {

		if (user == null)
			throw new EntityNotFoundException("User has to be not null.");
		return getEstimateByUserOrDivision(user, null, limit);
	}

	@Override
	public List<Estimate> getEstimateByUserOrDivision(User user,
			Division division) throws QueryException, EntityNotFoundException,
			DatabaseConnectionException {
		if (user == null && division == null)
			throw new EntityNotFoundException(
					"User and division have to be not null.");
		return getEstimateByUserOrDivision(user, division, -1);
	}

	@Override
	public List<Estimate> getEstimateByUserOrDivision(User user,
			Division division, int limit) throws QueryException,
			DatabaseConnectionException {

		if (limit < -1)
			throw new QueryException("Limit has to be >= -1");

		try {
			Query query;
			String sqlString;
			if (user != null && division != null)
				sqlString = "select e from Estimate e where e.estimator.id = :user or e.division.id = :division";
			else if (user != null)
				sqlString = "select e from Estimate e where e.estimator.id = :user";
			else if (division != null)
				sqlString = "select e from Estimate e where e.division.id = :division";
			else
				sqlString = "select e from Estimate e";

			if (limit == -1)
				sqlString += " order by e.name";
			else
				sqlString += " order by e.modifyDate desc";

			query = entityManager.createQuery(sqlString);
			if (user != null)
				query.setParameter("user", user.getId());
			if (division != null)
				query.setParameter("division", division.getId());

			if (limit != -1)
				query.setMaxResults(limit);

			return (List<Estimate>) query.getResultList();
		} catch (Exception e) {
			throw new DatabaseConnectionException(e);
		}
	}

	@Override
	public Estimate saveEstimate(Estimate estimate)
			throws EntityNotFoundException, DatabaseConnectionException {

		if (estimate == null)
			throw new EntityNotFoundException("Estimate must not be null.");
		try {
			ProjectEnvironment pe = estimate.getProjectEnvironment();
			estimate.setProjectEnvironment(null);
			List<Solution> solutions = null;
			if (estimate.getId() < 1) {
				solutions = estimate.getSolutions();
				estimate.setSolutions(null);
			}
			Estimate merge = entityManager.merge(estimate);
			pe.setEstimate(merge);
			merge.setProjectEnvironment(entityManager.merge(pe));
			if (solutions != null) {
				List<Solution> newSolutions = new ArrayList<Solution>(
						solutions.size());
				Map<Byte, Phase> phases = new HashMap<Byte, Phase>();
				for (Phase phase : merge.getProjectEnvironment().getPhases()) {
					phases.put(phase.getNumber(), phase);
				}
				for (Solution solution : solutions) {
					for (SolutionStaffingShape sss : solution
							.getSolutionStaffingShapes()) {
						sss.setPhase(phases.get(sss.getPhase().getNumber()));
					}
					solution.setEstimate(merge);
					newSolutions.add(solutionFacade.saveSolution(solution));

				}
				merge.setSolutions(newSolutions);
			}
			entityManager.flush();
			return merge;
		} catch (Exception e) {

			throw new DatabaseConnectionException(e);
		}
	}

	@Override
	public Estimate loadFullEstimate(Estimate est)
			throws EntityNotFoundException, DatabaseConnectionException {
		Estimate estimate;
		try {
			Query q = entityManager
					.createQuery("select e from Estimate e left join fetch e.solutions join fetch e.projectEnvironment where e.id = :id");
			q.setParameter("id", est.getId());
			estimate = (Estimate) q.getSingleResult();
		} catch (NoResultException e) {
			throw new EntityNotFoundException(String.format(
					"The estimate %s does not longer exist in the database.",
					est.getName()));
		} catch (Exception e) {
			throw new DatabaseConnectionException(e);
		}

		return estimate;
	}

	@Override
	public void deleteEstimate(Estimate estimate)
			throws EntityNotFoundException, DatabaseConnectionException {

		estimate = loadFullEstimate(estimate);
		deleteSolutions(estimate.getSolutions());
		deleteProjectEnvironment(estimate.getProjectEnvironment());
		entityManager.remove(estimate);
		entityManager.flush();

	}

	private void deleteProjectEnvironment(ProjectEnvironment pe) {
		if (pe.getMilestones() != null)
			for (Milestone m : pe.getMilestones())
				entityManager.remove(m);
		if (pe.getPhases() != null)
			for (Phase p : pe.getPhases())
				entityManager.remove(p);
		entityManager.remove(pe);
	}

	private void deleteSolutions(List<Solution> sols)
			throws DatabaseConnectionException {
		if (sols != null)
			for (Solution sol : sols)
				solutionFacade.deleteSolution(sol.getId());
	}
}
