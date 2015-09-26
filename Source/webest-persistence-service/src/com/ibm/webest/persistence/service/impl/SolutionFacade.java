package com.ibm.webest.persistence.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.ibm.webest.persistence.model.Certainty;
import com.ibm.webest.persistence.model.Constraint;
import com.ibm.webest.persistence.model.Division;
import com.ibm.webest.persistence.model.Estimate;
import com.ibm.webest.persistence.model.GranularityLevel;
import com.ibm.webest.persistence.model.GranularityQuestion;
import com.ibm.webest.persistence.model.PiHistoryCategory;
import com.ibm.webest.persistence.model.Solution;
import com.ibm.webest.persistence.model.SolutionStaffingShape;
import com.ibm.webest.persistence.model.StaffingShape;
import com.ibm.webest.persistence.model.UseCase;
import com.ibm.webest.persistence.model.UseCaseComplexity;
import com.ibm.webest.persistence.model.UseCasePack;
import com.ibm.webest.persistence.model.User;
import com.ibm.webest.persistence.service.AuthenticationRoles;
import com.ibm.webest.persistence.service.CloneException;
import com.ibm.webest.persistence.service.Cloner;
import com.ibm.webest.persistence.service.DatabaseConnectionException;
import com.ibm.webest.persistence.service.EntityNotFoundException;
import com.ibm.webest.persistence.service.QueryException;
import com.ibm.webest.persistence.service.SolutionFacadeLocal;
import com.ibm.webest.persistence.service.SolutionFacadeRemote;

/**
 * SolutionFacade includes the SQL query to Solution.
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
public class SolutionFacade implements SolutionFacadeRemote,
		SolutionFacadeLocal {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public GranularityLevel getGranularityLevelById(byte id)
			throws DatabaseConnectionException, EntityNotFoundException {
		GranularityLevel level;
		try {
			level = entityManager.find(GranularityLevel.class, id);
		} catch (Exception e) {
			throw new DatabaseConnectionException(e);
		}
		if (level == null)
			throw new EntityNotFoundException("Granularity level with id " + id
					+ " not found in database.");
		return level;
	}

	@Override
	public List<GranularityLevel> getGranularityLevels()
			throws DatabaseConnectionException {
		try {
			Query query = entityManager
					.createQuery("Select g from GranularityLevel g order by g.id desc");
			return (List<GranularityLevel>) query.getResultList();
		} catch (Exception e) {
			throw new DatabaseConnectionException(e);
		}
	}

	@Override
	public List<GranularityQuestion> getGranularityQuestions()
			throws DatabaseConnectionException {
		try {
			Query query = entityManager
					.createQuery("Select g from GranularityQuestion g");
			return (List<GranularityQuestion>) query.getResultList();
		} catch (Exception e) {
			throw new DatabaseConnectionException(e);
		}
	}

	@Override
	public List<UseCaseComplexity> getUseCaseComplexities()
			throws DatabaseConnectionException {
		try {
			Query query = entityManager
					.createQuery("Select u from UseCaseComplexity u");
			return (List<UseCaseComplexity>) query.getResultList();
		} catch (Exception e) {
			throw new DatabaseConnectionException(e);
		}
	}

	@Override
	public List<Certainty> getUseCaseCertainties()
			throws DatabaseConnectionException {
		try {
			Query query = entityManager
					.createQuery("Select c from Certainty c");
			return (List<Certainty>) query.getResultList();
		} catch (Exception e) {
			throw new DatabaseConnectionException(e);
		}
	}

	@Override
	public List<Solution> getSolutions(Estimate estimate, int limit)
			throws EntityNotFoundException, DatabaseConnectionException,
			QueryException {

		if (estimate == null)
			throw new EntityNotFoundException("Estimate must not be null.");

		if (limit < 0)
			throw new QueryException("Limit has to be >= 0.");
		try {
			Query query = entityManager
					.createQuery("Select s from Solution s where s.estimate.id = :estimate order by s.modifyDate desc");
			query.setParameter("estimate", estimate.getId());
			query.setMaxResults(limit);

			return (List<Solution>) query.getResultList();
		} catch (Exception e) {
			throw new DatabaseConnectionException(e);
		}
	}

	@Override
	public List<Solution> getSolutionsByUser(User user, int limit)
			throws EntityNotFoundException, DatabaseConnectionException,
			QueryException {

		if (user == null)
			throw new EntityNotFoundException("User must not be null.");

		if (limit < 0)
			throw new QueryException("Limit has to be >= 0.");
		try {
			Query query = entityManager
					.createQuery("Select s from Solution s where s.lastEditor.id = :user order by s.modifyDate desc");
			query.setParameter("user", user.getId());
			query.setMaxResults(limit);

			return (List<Solution>) query.getResultList();
		} catch (Exception e) {
			throw new DatabaseConnectionException(e);
		}
	}

	@Override
	public List<Solution> getSolutionsByDivision(Division div, int limit)
			throws EntityNotFoundException, DatabaseConnectionException,
			QueryException {

		if (div == null)
			throw new EntityNotFoundException("Division must not be null.");

		if (limit < 0)
			throw new QueryException("Limit has to be >= 0.");
		try {
			Query query = entityManager
					.createQuery("Select s from Solution s where s.estimate.division.id = :division order by s.modifyDate desc");
			query.setParameter("division", div.getId());
			query.setMaxResults(limit);

			return (List<Solution>) query.getResultList();
		} catch (Exception e) {
			throw new DatabaseConnectionException(e);
		}
	}

	@Override
	public List<PiHistoryCategory> getPiHistoryCategories()
			throws DatabaseConnectionException {
		try {
			Query query = entityManager
					.createQuery("Select p from PiHistoryCategory p");
			return (List<PiHistoryCategory>) query.getResultList();
		} catch (Exception e) {
			throw new DatabaseConnectionException(e);
		}
	}

	@Override
	public Solution cloneSolution(Solution solution)
			throws EntityNotFoundException, CloneException {

		if (solution == null)
			throw new EntityNotFoundException("Solution must not be null.");

		Solution newSolution = Cloner.cloneSolution(solution, null);
		return newSolution;

	}

	@Override
	public void deleteSolution(long solutionId)
			throws DatabaseConnectionException {
		try {
			Solution solution = entityManager.find(Solution.class, solutionId);
			if (solution == null) // solution already deleted
				return;
			deleteSolutionStaffingShapes(solution);
			deleteConstraints(solution);
			deleteUseCases(solution);
			entityManager.remove(solution);
			entityManager.flush();
		} catch (Exception e) {
			throw new DatabaseConnectionException(e);
		}
	}

	private void deleteUseCases(Solution solution) {
		if (solution.getUseCasePacks() != null)
			for (UseCasePack ucp : solution.getUseCasePacks()) {
				if (ucp.getUseCases() != null)
					for (UseCase uc : ucp.getUseCases())
						entityManager.remove(uc);
				entityManager.remove(ucp);
			}
	}

	private void deleteConstraints(Solution solution) {
		if (solution.getConstraints() != null)
			for (Constraint c : solution.getConstraints())
				entityManager.remove(c);
	}

	private void deleteSolutionStaffingShapes(Solution solution) {
		if (solution.getSolutionStaffingShapes() != null)
			for (SolutionStaffingShape sss : solution
					.getSolutionStaffingShapes())
				entityManager.remove(sss);
	}

	@Override
	public Solution loadSolutionById(long solutionId)
			throws DatabaseConnectionException, EntityNotFoundException {
		Solution solution;
		try {
			solution = entityManager.find(Solution.class, solutionId);
		} catch (Exception e) {
			throw new DatabaseConnectionException(e);
		}
		if (solution == null)
			throw new EntityNotFoundException("Solution " + solutionId
					+ " not found in database.");
		return solution;

	}

	@Override
	public Solution saveSolution(Solution solution)
			throws EntityNotFoundException, DatabaseConnectionException {
		if (solution == null)
			throw new EntityNotFoundException("Solution must not be null.");
		try {
			List<UseCasePack> ucps = null;
			List<SolutionStaffingShape> ssss = null;
			List<Constraint> constraints = null;
			if (solution.getId() < 1) {
				ucps = solution.getUseCasePacks();
				ssss = solution.getSolutionStaffingShapes();
				constraints = solution.getConstraints();
				solution.setUseCasePacks(null);
				solution.setSolutionStaffingShapes(null);
				solution.setConstraints(null);
			} else {
				synchronizeUseCases(solution);
			}
			Solution merge = entityManager.merge(solution);
			if (ucps != null) {
				List<UseCasePack> newUcps = new ArrayList<UseCasePack>(ucps.size());
				for (UseCasePack useCasePack : ucps) {
					useCasePack.setSolution(merge);
					List<UseCase> useCases = useCasePack.getUseCases();
					useCasePack.setUseCases(null);
					useCasePack = entityManager.merge(useCasePack);
					if (useCases != null) {
						List<UseCase> newUcs = new ArrayList<UseCase>(useCases.size());
						for (UseCase uc : useCases) {
							uc.setUseCasePack(useCasePack);
							newUcs.add(entityManager.merge(uc));
						}
						useCasePack.setUseCases(newUcs);
					}
					newUcps.add(useCasePack);
				}
				merge.setUseCasePacks(newUcps);
			}
			if (constraints != null) {
				List<Constraint> newConstraints = new ArrayList<Constraint>(constraints.size());
				for (Constraint constraint : constraints) {
					constraint.setSolution(merge);
					newConstraints.add(entityManager.merge(constraint));
				}
				merge.setConstraints(newConstraints);
			}
			if (ssss != null) {
				List<SolutionStaffingShape> newSsss = new ArrayList<SolutionStaffingShape>(ssss.size());
				for (SolutionStaffingShape solutionStaffingShape : ssss) {
					solutionStaffingShape.setSolution(merge);
					newSsss.add(entityManager.merge(solutionStaffingShape));
				}
				merge.setSolutionStaffingShapes(newSsss);
			}
			entityManager.flush();
			return merge;
		} catch (Exception e) {
			throw new DatabaseConnectionException(e);
		}

	}

	/**
	 * Checks for deleted use case packs and removes them from the database.
	 * @param solution the solution to synchronize the use cases from
	 */
	private void synchronizeUseCases(Solution solution) {
		Solution find = entityManager.find(Solution.class, solution.getId());
		Set<UseCasePack> newUcps = new HashSet<UseCasePack>(solution.getUseCasePacks());
		for (UseCasePack ucp : find.getUseCasePacks()) {
			if (!newUcps.contains(ucp)) {
				for (UseCase uc : ucp.getUseCases()) {
					entityManager.remove(uc);
				}
				entityManager.remove(ucp);
			}
		}
	}

	@Override
	public Solution loadFullSolution(Solution solution)
			throws EntityNotFoundException, DatabaseConnectionException {
		try {
			Solution foundSolution = entityManager.find(Solution.class, solution.getId());
			if (foundSolution == null)
				throw new NoResultException();
			// lazy load required entities 
			foundSolution.getGranularityQuestions().size();
			foundSolution.getConstraints().size();
			foundSolution.getSolutionStaffingShapes().size();
			for (UseCasePack ucp : foundSolution.getUseCasePacks()) {
				ucp.getUseCases().size();
			}
			solution = foundSolution;
		} catch (NoResultException e) {
			throw new EntityNotFoundException(
					"The solution does not longer exist in the database.");
		} catch (Exception e) {
			throw new DatabaseConnectionException(e);
		}
		return solution;
	}

	@Override
	public List<StaffingShape> getStaffingShapes()
			throws DatabaseConnectionException {
		try {
			Query query = entityManager
					.createQuery("Select ss from StaffingShape ss order by ss.name");
			return (List<StaffingShape>) query.getResultList();
		} catch (Exception e) {
			throw new DatabaseConnectionException(e);
		}
	}

}
