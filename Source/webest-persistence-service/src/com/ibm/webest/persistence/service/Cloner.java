package com.ibm.webest.persistence.service;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.ibm.webest.persistence.model.Constraint;
import com.ibm.webest.persistence.model.Estimate;
import com.ibm.webest.persistence.model.ProjectEnvironment;
import com.ibm.webest.persistence.model.Solution;
import com.ibm.webest.persistence.model.SolutionStaffingShape;
import com.ibm.webest.persistence.model.UseCase;
import com.ibm.webest.persistence.model.UseCasePack;

/**
 * The Cloner class provides functionality for cloning Estimates and Solutions.
 * 
 * @author Wail Shakir
 * 
 */
public class Cloner {

	/**
	 * Clones an estimate
	 * 
	 * @param estimate
	 *            clone this estimate
	 * @return the clone of the Estimate
	 * @see Estimate
	 */
	public static Estimate cloneEstimate(Estimate estimate) {
		Estimate estimateClone = new Estimate();
		estimateClone.setName(estimate.getName());
		estimateClone.setComment(estimate.getComment());
		estimateClone.setCreationDate(GregorianCalendar.getInstance());
		estimateClone.setModifyDate(GregorianCalendar.getInstance());
		estimateClone.setEstimator(estimate.getEstimator());
		estimateClone.setDivision(estimate.getDivision());
		estimateClone.setProjectEnvironment(cloneProjectEnvironment(
				estimate.getProjectEnvironment(), estimateClone));
		List<Solution> solutions = new LinkedList<Solution>();
		for (int i = 0; i < estimate.getSolutions().size(); i++) {
			solutions.add(cloneSolution(estimate.getSolutions().get(i), estimateClone));
		}
		estimateClone.setSolutions(solutions);
		return estimateClone;
	}

	/**
	 * clones a solution
	 * 
	 * @param solution
	 *            clone this solution
	 * @return the clone of the solution
	 * @see Solution
	 */
	public static Solution cloneSolution(Solution solution, Estimate estimate) {
		Solution result = new Solution();
		result.setCreationDate(GregorianCalendar.getInstance());
		result.setName(solution.getName());
		result.setGranularity(solution.getGranularity());
		result.setGearingFactor(solution.getGearingFactor());
		result.setModifyDate(GregorianCalendar.getInstance());
		result.setProjectStartDate((Calendar) solution.getProjectStartDate().clone());
		result.setPi(solution.getPi());
		result.setEstimate(estimate);
		result.setConstraints(cloneConstraints(solution.getConstraints(),
				result));
		result.setGranularityQuestions(solution.getGranularityQuestions());
		result.setSolutionStaffingShapes(cloneSolutionStaffingShape(
				solution.getSolutionStaffingShapes(), result));
		result.setUseCasePacks(cloneUseCasePack(solution.getUseCasePacks(),
				result));
		return result;
	}

	/**
	 * clones a SolutionStaffingShape
	 * 
	 * @param solutionStaffingShapes
	 *            clone this list of SolutionStaffingShape
	 * @param result
	 *            the solution where the clone of the list of
	 *            SolutionStaffingShape goes to.
	 * @return a cloned SolutionStaffingShape
	 * @see SolutionStaffingShape
	 */
	protected static List<SolutionStaffingShape> cloneSolutionStaffingShape(
			List<SolutionStaffingShape> solutionStaffingShapes, Solution result) {
		List<SolutionStaffingShape> solutionStaffingShapesClone = new LinkedList<SolutionStaffingShape>();
		for (Iterator<SolutionStaffingShape> iterator = solutionStaffingShapes
				.iterator(); iterator.hasNext();) {
			SolutionStaffingShape solutionStaffingShape = iterator.next();
			SolutionStaffingShape solutionStaffingShapeClone = new SolutionStaffingShape();
			solutionStaffingShapeClone.setPhase(solutionStaffingShape
					.getPhase());
			solutionStaffingShapeClone.setSelected(solutionStaffingShape
					.isSelected());
			solutionStaffingShapeClone.setSolution(result);
			solutionStaffingShapeClone.setStaffingShape(solutionStaffingShape
					.getStaffingShape());
			solutionStaffingShapesClone.add(solutionStaffingShapeClone);
		}
		return solutionStaffingShapesClone;
	}

	/**
	 * clones a list of Constraint
	 * 
	 * @param constraints
	 *            clone this list of constraint
	 * @param result
	 *            the solution where the clone of the list of Constraint goes
	 *            to.
	 * @return the clone of the list of Constraint
	 * @see Constraint
	 */
	protected static List<Constraint> cloneConstraints(
			List<Constraint> constraints, Solution result) {
		List<Constraint> constraintsClone = new LinkedList<Constraint>();

		for (Iterator<Constraint> iterator = constraints.iterator(); iterator
				.hasNext();) {
			Constraint constraint = iterator.next();

			Constraint constraintClone = new Constraint();
			constraintClone.setType(constraint.getType());
			constraintClone.setTarget(constraint.getTarget());
			constraintClone.setTargetProbability(constraint
					.getTargetProbability());
			constraintClone.setSolution(result);
			constraintsClone.add(constraintClone);
		}
		return constraintsClone;

	}

	/**
	 * clones a list of UseCasePack
	 * 
	 * @param useCasePacks
	 *            clone this list of UseCasePack
	 * @param result
	 *            the solution where the clone of the list of UseCasePack goes
	 *            to.
	 * @return the clone of the list of UseCasePack
	 * @see UseCasePack
	 */
	protected static List<UseCasePack> cloneUseCasePack(
			List<UseCasePack> useCasePacks, Solution result) {

		List<UseCasePack> useCasePacksClone = new LinkedList<UseCasePack>();
		for (Iterator<UseCasePack> iterator = useCasePacks.iterator(); iterator
				.hasNext();) {
			UseCasePack useCasePack = iterator.next();
			UseCasePack useCasePackClone = new UseCasePack();
			useCasePackClone.setName(useCasePack.getName());
			useCasePackClone.setSolution(result);
			useCasePackClone.setUseCases(cloneUseCases(
					useCasePack.getUseCases(), useCasePackClone));
			useCasePacksClone.add(useCasePackClone);
		}
		return useCasePacksClone;
	}

	/**
	 * clones a list of UseCase
	 * 
	 * @param useCases
	 *            clone this list of UseCase
	 * @param useCasePack
	 *            the useCasePack where the clone of the list of UseCase goes
	 *            to.
	 * @return the clone of the list of UseCase
	 * @see UseCase
	 */
	protected static List<UseCase> cloneUseCases(List<UseCase> useCases,
			UseCasePack useCasePack) {
		List<UseCase> useCasesClone = new LinkedList<UseCase>();
		for (Iterator<UseCase> iterator = useCases.iterator(); iterator
				.hasNext();) {
			UseCase useCase = iterator.next();

			UseCase useCaseClone = new UseCase();
			useCaseClone.setAssumptions(useCase.getAssumptions());
			useCaseClone.setCertainty(useCase.getCertainty());
			useCaseClone.setGranularityOverride(useCase
					.getGranularityOverride());
			useCaseClone.setInScope(useCase.isInScope());
			useCaseClone.setMultiplier(useCase.getMultiplier());
			useCaseClone.setName(useCase.getName());
			useCaseClone.setUseCaseComplexity(useCase.getUseCaseComplexity());
			useCaseClone.setUseCasePack(useCasePack);
			useCasesClone.add(useCaseClone);
		}
		return useCasesClone;
	}

	/**
	 * 
	 * @param projectEnvironment
	 *            clone this ProjectEnvironment
	 * @param estimate
	 *            the estimate where the clone of ProjectEnvironment goes to.
	 * @return the clone of ProjectEnvironment
	 * @see ProjectEnvironment
	 */
	public static ProjectEnvironment cloneProjectEnvironment(
			ProjectEnvironment projectEnvironment, Estimate estimate) {

		ProjectEnvironment projectEnvironmentClone = new ProjectEnvironment();
		projectEnvironmentClone.setProjectDescription(projectEnvironment
				.getProjectDescription());
		projectEnvironmentClone.setProjectName(projectEnvironment
				.getProjectName());
		projectEnvironmentClone.setApplicationType(projectEnvironment
				.getApplicationType());
		projectEnvironmentClone.setCountry(projectEnvironment.getCountry());
		projectEnvironmentClone.setEffortUnit(projectEnvironment
				.getEffortUnit());
		projectEnvironmentClone.setEstimate(estimate);
		projectEnvironmentClone.setIndustrySector(projectEnvironment
				.getIndustrySector());
		projectEnvironmentClone.setMonetaryUnit(projectEnvironment
				.getMonetaryUnit());
		projectEnvironmentClone.setMttdTimeUnit(projectEnvironment
				.getMttdTimeUnit());
		projectEnvironmentClone.setTemplate(projectEnvironment.getTemplate());
		TemplateValuesConverter.convert(projectEnvironment,
				projectEnvironmentClone);
		return projectEnvironmentClone;
	}
}
