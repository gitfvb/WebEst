package com.ibm.webest.processing.impl.administration;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.AccessLocalException;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.ibm.webest.persistence.model.Constraint;
import com.ibm.webest.persistence.model.DefectCategory;
import com.ibm.webest.persistence.model.EffortUnit;
import com.ibm.webest.persistence.model.Estimate;
import com.ibm.webest.persistence.model.GranularityLevel;
import com.ibm.webest.persistence.model.GranularityQuestion;
import com.ibm.webest.persistence.model.Milestone;
import com.ibm.webest.persistence.model.Phase;
import com.ibm.webest.persistence.model.ProjectEnvironment;
import com.ibm.webest.persistence.model.Solution;
import com.ibm.webest.persistence.model.SolutionStaffingShape;
import com.ibm.webest.persistence.model.StaffingShape;
import com.ibm.webest.persistence.model.Template;
import com.ibm.webest.persistence.model.TemplateValues;
import com.ibm.webest.persistence.model.UseCase;
import com.ibm.webest.persistence.model.UseCasePack;
import com.ibm.webest.persistence.model.User;
import com.ibm.webest.persistence.service.CalculationFacadeLocal;
import com.ibm.webest.persistence.service.Cloner;
import com.ibm.webest.persistence.service.DatabaseConnectionException;
import com.ibm.webest.persistence.service.EntityNotFoundException;
import com.ibm.webest.persistence.service.EstimateFacadeLocal;
import com.ibm.webest.persistence.service.ProjectEnvironmentFacadeLocal;
import com.ibm.webest.persistence.service.QueryException;
import com.ibm.webest.persistence.service.SolutionFacadeLocal;
import com.ibm.webest.persistence.service.TemplateValuesConverter;
import com.ibm.webest.processing.ServiceException;
import com.ibm.webest.processing.administration.AuthenticationServiceLocal;
import com.ibm.webest.processing.administration.EstimationManagerException;
import com.ibm.webest.processing.administration.EstimationManagerServiceLocal;
import com.ibm.webest.processing.administration.EstimationManagerServiceRemote;
import com.ibm.webest.processing.administration.LoggingServiceLocal;
import com.ibm.webest.processing.calculation.COCOMOCalcException;
import com.ibm.webest.processing.calculation.COCOMOCalculationServiceLocal;
import com.ibm.webest.processing.calculation.ConstraintsException;
import com.ibm.webest.processing.calculation.ConstraintsServiceLocal;
import com.ibm.webest.processing.calculation.PICalculatorException;
import com.ibm.webest.processing.calculation.PICalculatorServiceLocal;
import com.ibm.webest.processing.calculation.PutnamCalcException;
import com.ibm.webest.processing.calculation.PutnamCalculationServiceLocal;
import com.ibm.webest.processing.calculation.ReportCalculationServiceLocal;
import com.ibm.webest.processing.calculation.ReportException;
import com.ibm.webest.processing.calculation.SizingCalculatorException;
import com.ibm.webest.processing.calculation.SizingCalculatorServiceLocal;
import com.ibm.webest.processing.helpers.DefaultValues;
import com.ibm.webest.processing.model.COCOMOResult;
import com.ibm.webest.processing.model.EstimationResult;
import com.ibm.webest.processing.model.PutnamResult;
import com.ibm.webest.processing.model.Report;

/**
 * EstimationManager Organizes the data connected to the Estimations and its
 * calculation process.
 * 
 * @author Andre Munzinger
 * @author David Dornseifer
 * @author Gregor Schumm
 * @author Oliver Kreis
 * @author Wail Shakir
 */
@DeclareRoles({ AuthenticationServiceLocal.ROLE_ADMINISTRATOR,
		AuthenticationServiceLocal.ROLE_ESTIMATOR,
		AuthenticationServiceLocal.ROLE_MANAGER })
@RolesAllowed({ AuthenticationServiceLocal.ROLE_ADMINISTRATOR,
		AuthenticationServiceLocal.ROLE_ESTIMATOR,
		AuthenticationServiceLocal.ROLE_MANAGER })
@Stateless
public class EstimationManagerService implements
		EstimationManagerServiceRemote, EstimationManagerServiceLocal {

	@EJB
	private EstimateFacadeLocal estimateFacade;

	@EJB
	private AuthenticationServiceLocal authService;

	@EJB
	private SolutionFacadeLocal solutionFacade;

	@EJB
	private PICalculatorServiceLocal piCalc;

	@EJB
	private SizingCalculatorServiceLocal sizeCalc;

	@EJB
	private COCOMOCalculationServiceLocal cocomoCalc;

	@EJB
	private PutnamCalculationServiceLocal putnamCalc;

	@EJB
	private ConstraintsServiceLocal constServ;

	@EJB
	private ProjectEnvironmentFacadeLocal projectEnvFacade;

	@EJB
	private ReportCalculationServiceLocal repCalc;

	@EJB
	private CalculationFacadeLocal calcFacade;

	@EJB
	private LoggingServiceLocal logger;

	@RolesAllowed({ AuthenticationServiceLocal.ROLE_ADMINISTRATOR,
			AuthenticationServiceLocal.ROLE_ESTIMATOR })
	@Override
	public Estimate createEstimate() throws EstimationManagerException {
		Estimate est = new Estimate();
		if (authService.isEstimator()) {
			try {
				est.setEstimator(authService.getCurrentUser());
			} catch (Exception e) {
				logger.error(this,
						"Current user not found. No estimator was set.", e);
				throw new EstimationManagerException(
						"Current user not found. No estimator was set.", e);
			}
		}
		est.setProjectEnvironment(new ProjectEnvironment());
		est.getProjectEnvironment().setHoursPerDay(
				(byte) DefaultValues.getInt("hours_per_day"));
		est.getProjectEnvironment().setDaysPerWeek(
				(byte) DefaultValues.getInt("days_per_week"));
		est.getProjectEnvironment().setEstimate(est);
		est.getProjectEnvironment().setPhases(
				createPhases(est.getProjectEnvironment()));
		est.getProjectEnvironment().setDefectCategories(
				new ArrayList<DefectCategory>());
		est.getProjectEnvironment().setMilestones(new ArrayList<Milestone>());
		this.logger.info(this, "Estimate has been created without template.");
		return est;
	}

	@RolesAllowed({ AuthenticationServiceLocal.ROLE_ADMINISTRATOR,
			AuthenticationServiceLocal.ROLE_ESTIMATOR })
	@Override
	public Solution createSolution(Estimate est)
			throws EstimationManagerException {

		if (!authService.canEditEstimate(est)) {
			logger.error(
					this,
					String.format(
							"User %s is not allowed to create solutions for estimate %s (%d).",
							authService.getLoginUserName(), est.getName(),
							est.getId()));
			throw new AccessLocalException(
					"User not authorized to create solutions for this estimate.");
		}
		// default values are externalized to helpers/default-values.properties
		StaffingShape ss = null;
		try {
			List<StaffingShape> staffingShapes = solutionFacade
					.getStaffingShapes();
			for (StaffingShape s : staffingShapes) {
				if (s.getName().equals(
						DefaultValues.getString("staffing_shape"))) {
					ss = s;
					break;
				}
			}
		} catch (DatabaseConnectionException e) {
			this.logger.error("createSolution: A db connection error occurred",
					e);
			throw new EstimationManagerException(
					"createSolution: A db connection error occurred", e);
		}

		GranularityLevel gl = null;
		try {
			List<GranularityLevel> granularityLevels = solutionFacade
					.getGranularityLevels();
			for (GranularityLevel g : granularityLevels) {
				if (g.getId() == DefaultValues.getInt("granularity")) {
					gl = g;
					break;
				}
			}
		} catch (DatabaseConnectionException e) {
			this.logger.error("createSolution: A db connection error occurred",
					e);
			throw new EstimationManagerException(
					"createSolution: A db connection error occurred", e);
		}
		// fill Solution with default data
		Solution sol = new Solution();
		List<SolutionStaffingShape> ssss = new ArrayList<SolutionStaffingShape>(
				est.getProjectEnvironment().getPhases().size());
		for (Phase ph : est.getProjectEnvironment().getPhases()) {
			SolutionStaffingShape sss = new SolutionStaffingShape();
			sss.setPhase(ph);
			sss.setSelected(DefaultValues
					.getBoolean("solution_staffing_shape_selected"));
			sss.setSolution(sol);
			sss.setStaffingShape(ss);
			ssss.add(sss);
		}
		sol.setUseCasePacks(getDefaultUseCasePackList(sol));
		sol.setGranularity(gl);
		sol.setPi(DefaultValues.getInt("pi"));
		sol.setGranularityQuestions(new ArrayList<GranularityQuestion>());
		sol.setConstraints(new ArrayList<Constraint>());
		sol.setSolutionStaffingShapes(ssss);
		sol.setGearingFactor(DefaultValues.getInt("gearing_factor"));
		sol.setEstimate(est);
		sol.setCreationDate(GregorianCalendar.getInstance());
		sol.setProjectStartDate(GregorianCalendar.getInstance());
		est.getSolutions().add(sol);

		this.logger.info(this, "createSolution: Solution has been created.");

		return sol;
	}

	/**
	 * Creates a list of default use case packs.
	 * 
	 * @param sol
	 *            the solution wich owns the use cases
	 * @return a list of use case packs
	 */
	private List<UseCasePack> getDefaultUseCasePackList(Solution sol) {
		// default values are externalized to helpers/default-values.properties
		List<UseCase> useCaseList = new ArrayList<UseCase>();
		String[] usecases = DefaultValues.getStringArray("use_cases");
		UseCasePack ucp = new UseCasePack();
		ucp.setSolution(sol);
		ucp.setName(DefaultValues.getString("use_case_pack"));
		for (String s : usecases) {
			UseCase uc = new UseCase();
			uc.setName(s);
			uc.setMultiplier(DefaultValues.getDouble("use_case_multiplier"));
			uc.setUseCasePack(ucp);
			uc.setInScope(DefaultValues.getBoolean("use_case_in_scope"));
			useCaseList.add(uc);
		}
		ucp.setUseCases(useCaseList);
		List<UseCasePack> useCasePackList = new ArrayList<UseCasePack>();
		useCasePackList.add(ucp);
		return useCasePackList;
	}

	@RolesAllowed({ AuthenticationServiceLocal.ROLE_ADMINISTRATOR,
			AuthenticationServiceLocal.ROLE_ESTIMATOR })
	@Override
	public Estimate createEstimate(Template tpl)
			throws EstimationManagerException {
		Estimate est = createEstimate();
		est.setCreationDate(GregorianCalendar.getInstance());
		try {
			User u = authService.getCurrentUser();
			est.setEstimator(u);
			est.setProjectEnvironment(TemplateValuesConverter.convert(tpl,
					ProjectEnvironment.class));
			est.getProjectEnvironment().setEstimate(est);
			est.getProjectEnvironment().setTemplate(tpl);
		} catch (Exception e) {
			throw new EstimationManagerException(e);
		}

		this.logger.info(this, String.format(
				"Estimate has been created, using template %s (%s).",
				tpl.getName(), tpl.getId()));
		return est;
	}

	/**
	 * Creates four phases with default values for a new project environment.
	 * 
	 * @param owner
	 *            the owner of the phases
	 * @return a list of phases
	 */
	private List<Phase> createPhases(TemplateValues owner) {
		// default values are externalized to helpers/default-values.properties
		String[] defaultAcronyms = DefaultValues
				.getStringArray("phase_acronyms");
		String[] defaultNames = DefaultValues.getStringArray("phase_names");
		List<Phase> phases = new ArrayList<Phase>();
		for (byte i = 1; i <= 4; i++) {
			Phase phase = new Phase();
			phase.setNumber(i);
			phase.setAcronym(defaultAcronyms[i - 1]);
			phase.setName(defaultNames[i - 1]);
			phase.setOwner(owner);
			phases.add(phase);
		}
		return phases;
	}

	/**
	 * calculates the solution
	 * 
	 * @param sol
	 * @return
	 */
	private EstimationResult calculateSolution(Solution sol)
			throws EstimationManagerException {

		EstimationResult result = new EstimationResult();
		result.setSolution(sol);

		try {

			// Sum total ucp
			ArrayList<ArrayList<Integer>> ucpList = sizeCalc
					.calculateTotalUCP(sol);

			int ucp = 0;
			for (ArrayList<Integer> u : ucpList) {
				ucp += u.get(1);
			}
			result.setUcp(ucp);
			int pp;
			if (sol.getPp() == null) {
				// Berechnen des PP-Wertes
				pp = piCalc.calculatePP(sol.getPi());
				// sol.setPp(pp);
			} else
				pp = sol.getPp();

			float sloc = ucp * sol.getGearingFactor();
			result.setSloc(sloc);
			COCOMOResult cocomoResult = cocomoCalc.calc(sloc);
			result.setCocomoResult(cocomoResult);
			PutnamResult putnamResult = putnamCalc.calc(sloc, pp, cocomoResult);
			result.setPutnamResult(putnamResult);

		} catch (COCOMOCalcException e) {
			this.logger.error(this,
					"calculateSolution: Error in COCOMO calculation", e);
			throw new EstimationManagerException("Error in COCOMO calculation",
					e);
		} catch (PutnamCalcException e) {
			this.logger.error(this,
					"calculateSolution: Error in putnam calculation", e);
			throw new EstimationManagerException("Error in putnam calculation",
					e);
		} catch (PICalculatorException e) {
			this.logger.error(this,
					"calculateSolution: Error in calculating PP", e);
			throw new EstimationManagerException(
					"Error in PI lookup - Check that your PI value is listed in database, otherwise use a PP value.",
					e);
		} catch (SizingCalculatorException e) {
			this.logger.error(this,
					"calculateSolution: Error in calculating UCP", e);
			throw new EstimationManagerException("Error in calculating UCP.", e);
		} catch (EntityNotFoundException e) {
			this.logger.error(this,
					"calculateSolution: Couldn't find Parammeter", e);
			throw new EstimationManagerException(
					"Couldn't find Entity in database. Please see log for details.",
					e);
		} catch (DatabaseConnectionException e) {
			this.logger.error(this,
					"calculateSolution: Couldn't connect to database", e);
			throw new EstimationManagerException(
					"Couldn't connect to database. Please see log for details.",
					e);
		}

		return result;
	}

	@Override
	public Report generateReport(Solution sol)
			throws EstimationManagerException {
		Estimate estimate = sol.getEstimate();
		try {
			if (!authService.canViewEstimate(estimate)) {
				logger.error(
						this,
						String.format(
								"User %s is not allowed to generate reports for estimate %s (%d).",
								authService.getLoginUserName(),
								estimate.getName(), estimate.getId()));
				throw new AccessLocalException(
						"User not authorized to generate this report.");
			}
		} catch (Exception e) {
			throw new AccessLocalException(
					"User not authorized to generate this report.");
		}
		sol.setEstimate(reloadEstimate(sol.getEstimate()));
		EstimationResult result = calculateSolution(sol);

		// check if the estimated and personal target runtime are in bounds
		try {
			if (constServ.checkResult(result)) {
				Report rep = generateReport(result);
				logger.info(this, String.format(
						"Report for solution %s (%d) generated.",
						sol.getName(), sol.getId()));
				return rep;
			}

		} catch (ConstraintsException e) {
			int td = result.getPutnamResult().getTD().intValue();
			this.logger
					.error(this,
							String.format(
									"There is a problem with the project duration. The estimated duration is %d months, but %s. You are not able to run a projekt in less than 3/4 of the estimated duration or with a target duration greater than 3 years. That means your target duration should be between %d and 36 months.",
									td,
									td > 36 ? "the maximum project duration is fixed to 36 months"
											: "your chosen maximum duration is smaller",
									(int) Math.round(result.getPutnamResult()
											.getTD() * 3.0 / 4.0)));
			throw new EstimationManagerException(

					String.format(
							"There is a problem with the project duration. The estimated duration is %d months, but %s. You are not able to run a projekt in less than 3/4 of the estimated duration or with a target duration greater than 3 years. That means your target duration should be between %d and 36 months.",
							td,
							td > 36 ? "the maximum project duration is fixed to 36 months"
									: "your chosen maximum duration is smaller",
							(int) Math
									.round(result.getPutnamResult().getTD() * 3.0 / 4.0)));
		}
		return null;
	}

	/**
	 * Generates a report based on the given estimation result.
	 * 
	 * @param result
	 * @return the generated report
	 * @throws EstimationManagerException
	 */
	private Report generateReport(EstimationResult result)
			throws EstimationManagerException {

		// check if the estimated and personal target runtime are in bounds
		try {
			if (constServ.checkResult(result)) {
				try {
					return repCalc.createReport(result);
				} catch (ReportException e) {
					this.logger.error(this, e);
					throw new EstimationManagerException(e.getMessage());
				}
			}

		} catch (ConstraintsException e) {
			this.logger
					.error(this,
							String.format(
									"There is a problem with the project duration - the estimated time is %d month - The maximum project duration is fixed  to 36 month, in case, your estimation does not break the limit, you should check your personal target time. Your're not able to run a projekt in less than 3/4 * Estimated_Time or with a target time greater than 3 years. That means your target duration should be between %d and 36 month.",
									result.getPutnamResult().getTD().intValue(),
									(int) Math.round(result.getPutnamResult()
											.getTD() * 3.0 / 4.0)));
			throw new EstimationManagerException(
					String.format(
							"There is a problem with the project duration - the estimated time is %d month - The maximum project duration is fixed  to 36 month, in case, your estimation does not break the limit, you should check your personal target time. Your're not able to run a projekt in less than 3/4 * Estimated_Time or with a target time greater than 3 years. That means your target duration should be between %d and 36 month.",
							result.getPutnamResult().getTD().intValue(),
							(int) Math
									.round(result.getPutnamResult().getTD() * 3.0 / 4.0)));
		}
		return null;

	}

	@RolesAllowed({ AuthenticationServiceLocal.ROLE_ADMINISTRATOR,
			AuthenticationServiceLocal.ROLE_ESTIMATOR })
	@Override
	public void deleteEstimate(Estimate estimate)
			throws EstimationManagerException {

		if (estimate == null) {
			this.logger.error(this, "deleteEstimate: Estimate was null.");
			throw new EstimationManagerException(
					"deleteEstimate: Estimate was null.");
		}

		try {
			if (!authService.canEditEstimate(estimate)) {
				logger.error(this, String.format(
						"User %s is not allowed to delete estimate %s (%d).",
						authService.getLoginUserName(), estimate.getName(),
						estimate.getId()));
				throw new AccessLocalException(
						"User not authorized to delete this estimate.");
			}
			estimateFacade.deleteEstimate(estimate);
			logger.info(
					this,
					String.format("Estimate %s (%d) deleted.",
							estimate.getName(), estimate.getId()));
		} catch (EntityNotFoundException e) {
			this.logger.error(this,
					"deleteEstimate: Given estimate does not exist.", e);
			throw new EstimationManagerException(
					"Given estimate does not exist.", e);
		} catch (DatabaseConnectionException e) {
			this.logger
					.error("deleteEstimate: An error occured while deleting estimate.",
							e);
			throw new EstimationManagerException(
					"An error occured while deleting estimate.", e);
		}
	}

	@RolesAllowed({ AuthenticationServiceLocal.ROLE_ADMINISTRATOR,
			AuthenticationServiceLocal.ROLE_ESTIMATOR })
	@Override
	public void deleteSolution(Solution sol) throws EstimationManagerException {

		if (sol == null) {
			this.logger.error(this, "deleteSolution: Solution was null.");
			throw new EstimationManagerException("Solution was null.");
		}

		if (!authService.canEditEstimate(sol.getEstimate())) {
			logger.error(this, String.format(
					"User %s is not allowed to delete solution %s (%d).",
					authService.getLoginUserName(), sol.getName(), sol.getId()));
			throw new AccessLocalException(
					"User not authorized to delete this solution.");
		}
		Estimate est = sol.getEstimate();
		est.getSolutions().remove(sol);
		try {
			solutionFacade.deleteSolution(sol.getId());
			logger.info(this, String.format("Solution %s (%d) deleted.",
					sol.getName(), sol.getId()));
		} catch (DatabaseConnectionException e) {
			logger.error(this, "Solution " + sol.getId()
					+ " could not be deleted.", e);
			throw new EstimationManagerException(
					"Solution could not be deleted.", e);
		}
	}

	@Override
	public Estimate reloadEstimate(Estimate estimate)
			throws EstimationManagerException {
		if (estimate == null || estimate.getId() < 1)
			return estimate;
		if (!authService.canViewEstimate(estimate)) {
			logger.error(this, String.format(
					"User %s is not allowed to view estimate %s (%d).",
					authService.getLoginUserName(), estimate.getName(),
					estimate.getId()));
			throw new AccessLocalException(
					"User not authorized to view this estimate.");
		}
		try {
			return estimateFacade.loadFullEstimate(estimate);
		} catch (DatabaseConnectionException e) {
			this.logger.error(this,
					"reloadEstimate: Estimate could not be reloaded.", e);
			throw new EstimationManagerException(
					"Estimate could not be reloaded", e);
		} catch (EntityNotFoundException e) {
			this.logger.error(this,
					"reloadEstimate: Estimate could not be reloaded.", e);
			throw new EstimationManagerException(
					"Estimate could not be reloaded", e);

		}

	}

	@Override
	public Solution reloadSolution(Solution solution)
			throws EstimationManagerException {
		if (solution == null || solution.getId() < 1)
			return solution;
		Estimate estimate = solution.getEstimate();
		if (!authService.canViewEstimate(estimate)) {
			logger.error(this, String.format(
					"User %s is not allowed to view estimate %s (%d).",
					authService.getLoginUserName(), estimate.getName(),
					estimate.getId()));
			throw new AccessLocalException(
					"User not authorized to view this estimate.");
		}
		try {
			Solution sol = solutionFacade.loadFullSolution(solution);
			return sol;
		} catch (DatabaseConnectionException e) {
			this.logger.error(this,
					"reloadSolution: Solution could not be reloaded.", e);
			throw new EstimationManagerException(
					"Solution could not be reloaded.", e);
		} catch (EntityNotFoundException e) {
			this.logger.error(this,
					"reloadSolution: Solution could not be reloaded.", e);
			throw new EstimationManagerException(
					"Solution could not be reloaded.", e);
		}
	}

	@Override
	public List<Estimate> getAllEstimates() throws EstimationManagerException {
		try {
			if (authService.isAdministrator()) {
				return estimateFacade.getAllEstimates();
			}

			User currentUser = authService.getCurrentUser();
			if (authService.isEstimator()) {
				return estimateFacade.getEstimateByUserOrDivision(currentUser,
						currentUser.getDivision());
			} else {
				return estimateFacade.getEstimatesByDivision(currentUser
						.getDivision());
			}
		} catch (ServiceException e) {
			this.logger
					.error("getAllEstimates: An error occured while fetching estimates.",
							e);
			throw new EstimationManagerException(
					"An error occured while fetching estimates.", e);
		} catch (DatabaseConnectionException e) {
			this.logger
					.error("getAllEstimates: An error occured while fetching estimates.",
							e);
			throw new EstimationManagerException(
					"An error occured while fetching estimates.", e);
		} catch (EntityNotFoundException e) {
			this.logger
					.error("getAllEstimates: An error occured while fetching estimates.",
							e);
			throw new EstimationManagerException(
					"An error occured while fetching estimates.", e);
		} catch (QueryException e) {
			this.logger
					.error("getAllEstimates: An error occured while fetching estimates.",
							e);
			throw new EstimationManagerException(
					"An error occured while fetching estimates.", e);
		}
	}

	@Override
	public Estimate cloneEstimate(Estimate est)
			throws EstimationManagerException {

		if (est == null) {
			this.logger.error(this, "cloneEstimate: Estimate was null.");
			throw new EstimationManagerException("Estimate was null.");
		}
		Estimate estimate = null;
		try {
			if (authService.canEditEstimate(est)) {

				est = reloadEstimate(est);
				estimate = Cloner.cloneEstimate(est);
				for (int i = 0; i < estimate.getSolutions().size(); i++) {

					estimate.getSolutions().get(i)
							.setLastEditor(authService.getCurrentUser());
				}
				logger.info(
						this,
						String.format("Estimate %s (%d) cloned.",
								estimate.getName(), estimate.getId()));
			} else {
				this.logger
						.error(this,
								"cloneEstimate: User not authorized to clone this estimate.");
				throw new AccessLocalException(
						"User not authorized to clone this estimate.");
			}
		} catch (EntityNotFoundException e) {
			this.logger
					.error("cloneEstimate: Estimate could not be cloned.", e);
			throw new EstimationManagerException(
					"Estimate could not be cloned.", e);
		} catch (EstimationManagerException e) {
			this.logger
					.error("cloneEstimate: Estimate could not be cloned.", e);
			throw new EstimationManagerException(
					"Estimate could not be cloned.", e);
		} catch (ServiceException e) {
			this.logger
					.error("cloneEstimate: Estimate could not be cloned.", e);
			throw new EstimationManagerException(
					"Estimate could not be cloned.", e);
		} catch (AccessLocalException e) {
			this.logger
					.error("cloneEstimate: Estimate could not be cloned.", e);
			throw new EstimationManagerException(
					"Estimate could not be cloned.", e);
		}

		return estimate;
	}

	private Solution cloneSolution(Solution sol)
			throws EstimationManagerException {

		if (sol == null) {
			this.logger.error(this, "cloneSolution: Solution was null.");
			throw new EstimationManagerException("Solution was null.");
		}

		if (authService.canEditEstimate(sol.getEstimate())) {
			sol = reloadSolution(sol);

			Solution nsol = Cloner.cloneSolution(sol, sol.getEstimate());
			logger.info(this, String.format("Solution %s (%d) cloned.",
					sol.getName(), sol.getId()));
			return nsol;
		} else {
			this.logger
					.error(this,
							"cloneSolution: User not authorized to clone this solution.");
			throw new AccessLocalException(
					"User not authorized to clone this solution.");
		}
	}

	@Override
	public Solution cloneSolutionForEstimate(Solution sol)
			throws EstimationManagerException {
		Solution clone = cloneSolution(sol);
		sol.setModifyDate(GregorianCalendar.getInstance());
		sol.setCreationDate(GregorianCalendar.getInstance());
		try {
			sol.setLastEditor(authService.getCurrentUser());
		} catch (EntityNotFoundException e) {
			logger.error(this, e);
			throw new EstimationManagerException(e);
		} catch (ServiceException e) {
			logger.error(this, e);
			throw new EstimationManagerException(e);
		}
		Estimate est = sol.getEstimate();
		est.getSolutions().add(clone);
		return clone;
	}

	@Override
	public void restoreTemplateDefaults(Estimate est)
			throws EstimationManagerException {
		if (!authService.canEditEstimate(est)) {
			logger.error(this, String.format(
					"User %s is not allowed to save templates.",
					authService.getLoginUserName()));
			throw new AccessLocalException(
					"User not authorized to save templates.");
		}

		try {
			TemplateValuesConverter.convert(est.getProjectEnvironment()
					.getTemplate(), est.getProjectEnvironment());
			
		//catch all possible exceptions because there is no specific one
		} catch (Exception e) {
			this.logger.error(this,
					"restoreTemplateDefaults: " + e.getMessage(), e);
			throw new EstimationManagerException(e);
		}
	}

	@RolesAllowed({ AuthenticationServiceLocal.ROLE_ADMINISTRATOR,
			AuthenticationServiceLocal.ROLE_ESTIMATOR })
	@Override
	public Solution saveSolution(Solution sol)
			throws EstimationManagerException {
		if (authService.canEditEstimate(sol.getEstimate())) {
			sol.setModifyDate(GregorianCalendar.getInstance());
			if (sol.getCreationDate() == null)
				sol.setCreationDate(GregorianCalendar.getInstance());

			try {
				sol.setLastEditor(authService.getCurrentUser());
				sol = solutionFacade.saveSolution(sol);
				logger.info(this, String.format("Soltuion %s (%d) saved.",
						sol.getName(), sol.getId()));
				return sol;
			} catch (DatabaseConnectionException e) {
				this.logger
						.error("saveSolution: An error occurred while saving solution.",
								e);
				throw new EstimationManagerException(
						"An error occurred while saving solution.", e);
			} catch (ServiceException e) {
				this.logger
						.error("saveSolution: An error occurred while saving solution.",
								e);
				throw new EstimationManagerException(
						"An error occurred while saving solution.", e);
			} catch (EntityNotFoundException e) {
				this.logger
						.error("saveSolution: An error occurred while saving solution.",
								e);
				throw new EstimationManagerException(
						"An error occurred while saving solution.", e);
			}
		} else {
			this.logger.error(this,
					"saveSolution: User not authorized to save this solution.");
			throw new AccessLocalException(
					"User not authorized to save this solution.");
		}
	}

	@RolesAllowed({ AuthenticationServiceLocal.ROLE_ADMINISTRATOR,
			AuthenticationServiceLocal.ROLE_ESTIMATOR })
	@Override
	public Estimate saveEstimate(Estimate estimate)
			throws EstimationManagerException {
		try {
			if (!authService.canEditEstimate(estimate)) {
				logger.error(this, String.format(
						"User %s is not allowed to save estimate %s (%d).",
						authService.getLoginUserName(), estimate.getName(),
						estimate.getId()));
				this.logger
						.error(this,
								"saveEstimate: User not authorized to save this estimate.");
				throw new AccessLocalException(
						"User not authorized to save this estimate.");
			}
			if (estimate.getCreationDate() == null)
				estimate.setCreationDate(GregorianCalendar.getInstance());
			estimate.setModifyDate(GregorianCalendar.getInstance());
			estimate.setLastEditor(authService.getCurrentUser());

			estimate = estimateFacade.saveEstimate(estimate);

			logger.info(this, String.format("Estimate %s (%d) saved.",
					estimate.getName(), estimate.getId()));

			return estimate;
		} catch (EntityNotFoundException e) {
			this.logger
					.error("saveEstimate: Given estimate does not exist.", e);
			throw new EstimationManagerException(
					"Given estimate does not exist.", e);
		} catch (ServiceException e) {
			this.logger.error(this,
					"saveEstimate: An error occured while saving estimate.", e);
			throw new EstimationManagerException(
					"An error occured while saving estimate.", e);
		} catch (DatabaseConnectionException e) {
			this.logger.error(this,
					"saveEstimate: An error occured while saving estimate.", e);
			throw new EstimationManagerException(
					"An error occured while saving estimate.", e);
		}
	}

	@Override
	public List<Estimate> getRecentUserEstimates(int limit)
			throws EstimationManagerException {
		try {
			User u = authService.getCurrentUser();
			return estimateFacade.getEstimatesByUser(u, limit);
		} catch (DatabaseConnectionException e) {
			this.logger
					.error("getRecentUserEstimates: Recent estimates could not be retrieved.",
							e);
			throw new EstimationManagerException(
					"Recent estimates could not be retrieved.", e);
		} catch (QueryException e) {
			this.logger
					.error("getRecentUserEstimates: Recent estimates could not be retrieved.",
							e);
			throw new EstimationManagerException(
					"Recent estimates could not be retrieved.", e);
		} catch (EntityNotFoundException e) {
			this.logger
					.error("getRecentUserEstimates: Recent estimates could not be retrieved.",
							e);
			throw new EstimationManagerException(
					"Recent estimates could not be retrieved.", e);
		} catch (ServiceException e) {
			this.logger
					.error("getRecentUserEstimates: Recent estimates could not be retrieved.",
							e);
			throw new EstimationManagerException(
					"Recent estimates could not be retrieved.", e);
		}
	}

	@Override
	public List<Estimate> getRecentDivisionEstimates(int limit)
			throws EstimationManagerException {
		try {
			User u = authService.getCurrentUser();
			return estimateFacade
					.getEstimatesByDivision(u.getDivision(), limit);
		} catch (DatabaseConnectionException e) {
			this.logger
					.error("getRecentDivisionEstimates: Recent estimates could not be retrieved.",
							e);
			throw new EstimationManagerException(
					"Recent estimates could not be retrieved.", e);
		} catch (QueryException e) {
			this.logger
					.error("getRecentDivisionEstimates: Recent estimates could not be retrieved.",
							e);
			throw new EstimationManagerException(
					"Recent estimates could not be retrieved.", e);
		} catch (EntityNotFoundException e) {
			this.logger
					.error("getRecentDivisionEstimates: Recent estimates could not be retrieved.",
							e);
			throw new EstimationManagerException(
					"Recent estimates could not be retrieved.", e);
		} catch (ServiceException e) {
			this.logger
					.error("getRecentDivisionEstimates: Recent estimates could not be retrieved.",
							e);
			throw new EstimationManagerException(
					"Recent estimates could not be retrieved.", e);
		}
	}

	@Override
	public List<Solution> getRecentUserSolutions(int limit)
			throws EstimationManagerException {
		try {
			User u = authService.getCurrentUser();
			return solutionFacade.getSolutionsByUser(u, limit);
		} catch (DatabaseConnectionException e) {
			this.logger
					.error("getRecentUserSolutions: Recent estimates could not be retrieved.",
							e);
			throw new EstimationManagerException(
					"Recent estimates could not be retrieved.", e);
		} catch (QueryException e) {
			this.logger
					.error("getRecentUserSolutions: Recent estimates could not be retrieved.",
							e);
			throw new EstimationManagerException(
					"Recent estimates could not be retrieved.", e);
		} catch (EntityNotFoundException e) {
			this.logger
					.error("getRecentUserSolutions: Recent estimates could not be retrieved.",
							e);
			throw new EstimationManagerException(
					"Recent estimates could not be retrieved.", e);
		} catch (ServiceException e) {
			this.logger
					.error("getRecentUserSolutions: Recent estimates could not be retrieved.",
							e);
			throw new EstimationManagerException(
					"Recent estimates could not be retrieved.", e);
		}
	}

	@Override
	public List<Solution> getRecentDivisionSolutions(int limit)
			throws EstimationManagerException {
		try {
			User u = authService.getCurrentUser();
			return solutionFacade
					.getSolutionsByDivision(u.getDivision(), limit);
		} catch (DatabaseConnectionException e) {
			this.logger
					.error("getRecentDivisionSolutions: Recent estimates could not be retrieved.",
							e);
			throw new EstimationManagerException(
					"Recent estimates could not be retrieved.", e);
		} catch (QueryException e) {
			this.logger
					.error("getRecentDivisionSolutions: Recent estimates could not be retrieved.",
							e);
			throw new EstimationManagerException(
					"Recent estimates could not be retrieved.", e);
		} catch (EntityNotFoundException e) {
			this.logger
					.error("getRecentDivisionSolutions: Recent estimates could not be retrieved.",
							e);
			throw new EstimationManagerException(
					"Recent estimates could not be retrieved.", e);
		} catch (ServiceException e) {
			this.logger
					.error("getRecentDivisionSolutions: Recent estimates could not be retrieved.",
							e);
			throw new EstimationManagerException(
					"Recent estimates could not be retrieved.", e);
		}
	}

	@Override
	public void saveAsTemplate(String name, String desc,
			ProjectEnvironment projectEnvironment)
			throws EstimationManagerException {
		if (!authService.canEditEstimate(projectEnvironment.getEstimate())) {
			logger.error(this, String.format(
					"User %s is not allowed to save templates.",
					authService.getLoginUserName()));
			throw new AccessLocalException(
					"User not authorized to save templates.");
		}
		try {
			Template tpl = TemplateValuesConverter.convert(projectEnvironment,
					Template.class);
			tpl.setName(name);
			tpl.setDescription(desc);
			tpl = projectEnvFacade.saveTemplate(tpl);
			projectEnvironment.setTemplate(tpl);

			logger.info(this, "Template with name " + name + " saved.");
		} catch (DatabaseConnectionException e) {
			logger.error(this, "saveAsTemplate: Template could not be saved.",
					e);
			throw new EstimationManagerException(
					"Template could not be saved.", e);
		} catch (IllegalArgumentException e) {
			logger.error(this, "saveAsTemplate: Template could not be saved.",
					e);
			throw new EstimationManagerException(
					"Template could not be saved.", e);
		} catch (IllegalAccessException e) {
			logger.error(this, "saveAsTemplate: Template could not be saved.",
					e);
			throw new EstimationManagerException(
					"Template could not be saved.", e);
		} catch (InstantiationException e) {
			logger.error(this, "saveAsTemplate: Template could not be saved.",
					e);
			throw new EstimationManagerException(
					"Template could not be saved.", e);
		} catch (InvocationTargetException e) {
			logger.error(this, "saveAsTemplate: Template could not be saved.",
					e);
			throw new EstimationManagerException(
					"Template could not be saved.", e);
		}

	}

	@PermitAll
	@Override
	public void startup() {
		// call a facade method to force jpa framework to start
		try {
			calcFacade.getPpByPi(0);
		} catch (Throwable t) {
			// Exceptions can not be handled here.
		}
	}

	@Override
	public Solution getSolutionById(int solutionId)
			throws EstimationManagerException, EntityNotFoundException {
		Solution sol;
		try {
			sol = solutionFacade.loadSolutionById(solutionId);
		} catch (DatabaseConnectionException e) {
			throw new EstimationManagerException(
					"Error while fetching solution.", e);
		}
		if (!authService.canViewEstimate(sol.getEstimate()))
			throw new AccessLocalException(
					"User is not allowed to view generate this report.");
		return sol;
	}

	@Override
	public EffortUnit getReferenceEffortUnit()
			throws EstimationManagerException {
		try {
			return projectEnvFacade.getReferenceUnit(EffortUnit.class);
		} catch (DatabaseConnectionException e) {
			logger.error(this, "Reference unit could not be fetched.", e);
			throw new EstimationManagerException(
					"Reference unit could not be fetched.", e);
		} catch (EntityNotFoundException e) {
			logger.error(this, "Reference unit could not be fetched.", e);
			throw new EstimationManagerException(
					"Reference unit could not be fetched.", e);
		}
	}
}