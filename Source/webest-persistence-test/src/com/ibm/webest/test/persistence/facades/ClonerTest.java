package com.ibm.webest.test.persistence.facades;

import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.junit.Test;
import static org.junit.Assert.*;

import com.ibm.webest.persistence.model.ApplicationType;
import com.ibm.webest.persistence.model.Certainty;
import com.ibm.webest.persistence.model.Constraint;
import com.ibm.webest.persistence.model.Country;
import com.ibm.webest.persistence.model.Division;
import com.ibm.webest.persistence.model.EffortUnit;
import com.ibm.webest.persistence.model.Estimate;
import com.ibm.webest.persistence.model.GranularityLevel;
import com.ibm.webest.persistence.model.GranularityQuestion;
import com.ibm.webest.persistence.model.IndustrySector;
import com.ibm.webest.persistence.model.MTTDTimeUnit;
import com.ibm.webest.persistence.model.Milestone;
import com.ibm.webest.persistence.model.MonetaryUnit;
import com.ibm.webest.persistence.model.Phase;
import com.ibm.webest.persistence.model.ProjectEnvironment;
import com.ibm.webest.persistence.model.Solution;
import com.ibm.webest.persistence.model.SolutionStaffingShape;
import com.ibm.webest.persistence.model.StaffingShape;
import com.ibm.webest.persistence.model.Template;
import com.ibm.webest.persistence.model.UseCase;
import com.ibm.webest.persistence.model.UseCaseComplexity;
import com.ibm.webest.persistence.model.UseCasePack;
import com.ibm.webest.persistence.model.User;
import com.ibm.webest.persistence.service.Cloner;

/**
 * This class tests the Cloner class
 * 
 * @author Wail Shakir
 * 
 */
public class ClonerTest extends Cloner {
	private static final double DELTA = 0.00001;

	@Test
	public void cloneUseCases() {
		UseCasePack useCasePack = new UseCasePack();
		useCasesTest(useCase(useCasePack, 0), new UseCasePack());
	}

	@Test
	public void cloneUseCasePacks() {

		useCasePackTest(useCasePack(new Solution(), 0), new Solution());
	}

	@Test
	public void cloneConstraints() {

		constraintsTest(constraints(new Solution()), new Solution());
	}

	@Test
	public void cloneSolutionStaffingStape() {

		solutionStaffingShapeTest(solutionStaffingShape(new Solution()),
				new Solution());
	}

	@Test
	public void cloneSolution() {
		Estimate estimate = new Estimate();
		Solution solution = solution(estimate);
		solutionTest(solution, cloneSolution(solution, estimate));
	}

	@Test
	public void cloneProjectEnvironment() {

		projectEnvironmentTest(projectEnvironment(new Estimate()),
				new Estimate());
	}

	@Test
	public void estimateTest() {
		estimateTest(estimate());
	}

	private Estimate estimate() {
		Estimate estimate = new Estimate();
		estimate.setName("tested estimate name");
		estimate.setComment("tested estimate comment");
		estimate.setCreationDate(GregorianCalendar.getInstance());
		estimate.setModifyDate(GregorianCalendar.getInstance());
		estimate.setEstimator(new User());
		estimate.setDivision(new Division());
		estimate.setProjectEnvironment(projectEnvironment(estimate));
		List<Solution> solutions = new LinkedList<Solution>();
		for (int i = 0; i < 4; i++) {
			solutions.add(solution(estimate));
		}
		estimate.setSolutions(solutions);
		return estimate;
	}

	private void estimateTest(Estimate estimate) {
		Estimate estimateClone = cloneEstimate(estimate);
		assertNotSame(estimate, estimateClone);
		assertEquals(estimate.getName(), estimateClone.getName());
		assertEquals(estimate.getComment(), estimateClone.getComment());
		assertFalse(estimate.getCreationDate() == estimateClone
				.getCreationDate());
		assertFalse(estimate.getModifyDate() == estimateClone.getModifyDate());
		assertSame(estimate.getEstimator(), estimateClone.getEstimator());
		assertSame(estimate.getDivision(), estimateClone.getDivision());
		projectEnvironmentTest(estimate.getProjectEnvironment(), estimateClone);
		for (int i = 0; i < estimate.getSolutions().size(); i++) {
			assertNotSame(estimate.getSolutions().get(i), estimateClone
					.getSolutions().get(i));
			solutionTest(estimate.getSolutions().get(i), estimateClone
					.getSolutions().get(i));
		}

	}

	private ProjectEnvironment projectEnvironment(Estimate estimate) {

		ProjectEnvironment projectEnvironment = new ProjectEnvironment();
		projectEnvironment.setDaysPerWeek((byte) 6);
		projectEnvironment.setHoursPerDay((byte) 9);
		projectEnvironment
				.setProjectDescription("tested the project description");
		projectEnvironment.setProjectName("tested the project name");
		projectEnvironment.setApplicationType(new ApplicationType());
		projectEnvironment.setCountry(new Country());
		projectEnvironment.setEffortUnit(new EffortUnit());
		projectEnvironment.setEstimate(estimate);
		projectEnvironment.setIndustrySector(new IndustrySector());
		projectEnvironment.setMonetaryUnit(new MonetaryUnit());
		projectEnvironment.setMttdTimeUnit(new MTTDTimeUnit());
		projectEnvironment.setTemplate(new Template());
		List<Phase> phases = new LinkedList<Phase>();
		for (int i = 1; i < 5; i++) {
			Phase phase = new Phase();
			phase.setAcronym("s" + i);
			phase.setNumber((byte) i);

			List<Milestone> milestones = new LinkedList<Milestone>();
			for (int j = 0; j < 5; j++) {
				Milestone milestone = new Milestone();
				milestone.setName("m" + i + " " + j);

			}
			phase.setMilestones(milestones);
			phases.add(phase);
		}
		projectEnvironment.setPhases(phases);

		return projectEnvironment;
	}

	private void projectEnvironmentTest(ProjectEnvironment projectEnvironment,
			Estimate estimateClone) {
		ProjectEnvironment projectEnvironmentClone = cloneProjectEnvironment(
				projectEnvironment, estimateClone);

		assertNotSame(projectEnvironment, projectEnvironmentClone);
		assertTrue(projectEnvironment.getDaysPerWeek() == projectEnvironmentClone
				.getDaysPerWeek());
		assertTrue(projectEnvironment.getHoursPerDay() == projectEnvironmentClone
				.getHoursPerDay());
		assertEquals(projectEnvironment.getProjectDescription(),
				projectEnvironmentClone.getProjectDescription());
		assertEquals(projectEnvironment.getProjectName(),
				projectEnvironmentClone.getProjectName());
		assertSame(projectEnvironment.getApplicationType(),
				projectEnvironmentClone.getApplicationType());

		assertSame(projectEnvironmentClone.getCountry(),
				projectEnvironmentClone.getCountry());
		assertSame(projectEnvironment.getEffortUnit(),
				projectEnvironmentClone.getEffortUnit());
		assertSame(estimateClone, projectEnvironmentClone.getEstimate());
		assertSame(projectEnvironment.getIndustrySector(),
				projectEnvironmentClone.getIndustrySector());
		assertSame(projectEnvironment.getMonetaryUnit(),
				projectEnvironmentClone.getMonetaryUnit());
		assertSame(projectEnvironment.getMttdTimeUnit(),
				projectEnvironmentClone.getMttdTimeUnit());
		assertSame(projectEnvironment.getTemplate(),
				projectEnvironmentClone.getTemplate());

		List<Phase> phasesClone = projectEnvironmentClone.getPhases();
		List<Phase> phases = projectEnvironment.getPhases();
		for (int i = 0; i < phases.size(); i++) {
			assertNotSame(phases.get(0), phasesClone.get(i));
			assertEquals(phases.get(i).getAcronym(), phasesClone.get(i)
					.getAcronym());
			assertTrue(phases.get(i).getNumber() == phasesClone.get(i)
					.getNumber());

			List<Milestone> milestones = phases.get(i).getMilestones();
			List<Milestone> milestonesClone = phasesClone.get(i)
					.getMilestones();
			for (int j = 0; j < milestones.size(); j++) {
				assertNotSame(milestones.get(j), milestonesClone.get(j));
				assertEquals(milestones.get(j).getName(), milestonesClone
						.get(j).getName());

			}
		}

	}

	private List<UseCase> useCase(UseCasePack useCasePack, int nummer) {
		List<UseCase> list = new LinkedList<UseCase>();
		Random random = new Random();
		for (int i = 0; i < 5; i++) {
			UseCase useCase = new UseCase();
			useCase.setAssumptions("Assumptions" + nummer + "-" + i);
			useCase.setCertainty(new Certainty());
			useCase.setGranularityOverride(new GranularityLevel());
			useCase.setInScope(random.nextBoolean());
			useCase.setMultiplier(random.nextDouble());
			useCase.setName("useCase" + nummer + "-" + i);
			useCase.setUseCaseComplexity(new UseCaseComplexity());
			useCase.setUseCasePack(useCasePack);
			list.add(useCase);
		}
		return list;
	}

	private void useCasesTest(List<UseCase> list, UseCasePack useCasePack) {

		List<UseCase> result = cloneUseCases(list, useCasePack);
		assertEquals(result.size(), list.size());
		for (int i = 0; i < list.size(); i++) {

			assertTrue(result.get(i).getName().equals(list.get(i).getName()));
			assertTrue(result.get(i).getAssumptions()
					.equals(list.get(i).getAssumptions()));
			assertEquals(result.get(i).getMultiplier(), list.get(i)
					.getMultiplier(), DELTA);
			assertTrue(result.get(i).isInScope() == list.get(i).isInScope());
			assertTrue(result.get(i).getCertainty() == list.get(i)
					.getCertainty());
			assertTrue(result.get(i).getGranularityOverride() == list.get(i)
					.getGranularityOverride());
			assertTrue(result.get(i).getUseCaseComplexity() == list.get(i)
					.getUseCaseComplexity());
			assertTrue(result.get(i).getUseCasePack() != list.get(i)
					.getUseCasePack());
			assertNotSame("are the same Object", list.get(i), result.get(i));
		}
	}

	private List<UseCasePack> useCasePack(Solution solution, int nummer) {

		List<UseCasePack> useCasePacks = new LinkedList<UseCasePack>();
		for (int i = 0; i < 5; i++) {
			UseCasePack useCasePack = new UseCasePack();
			useCasePack.setName("useCasePack" + nummer + "-" + i);
			useCasePack.setSolution(solution);
			useCasePack.setUseCases(useCase(useCasePack, i));

			useCasePacks.add(useCasePack);
		}

		return useCasePacks;
	}

	private void useCasePackTest(List<UseCasePack> useCasePacks,
			Solution solutionClone) {

		List<UseCasePack> result = cloneUseCasePack(useCasePacks, solutionClone);
		assertTrue(useCasePacks.size() == result.size());
		for (int i = 0; i < useCasePacks.size(); i++) {
			assertTrue(useCasePacks.get(i).getName()
					.equals(result.get(i).getName()));
			useCasesTest(useCasePacks.get(i).getUseCases(), result.get(i));
			assertNotSame("solution and solutionClone are the same Object",
					useCasePacks.get(i).getSolution(), result.get(i)
							.getSolution());
			assertNotSame("are the same Object", useCasePacks.get(i),
					result.get(i));

		}

	}

	private List<Constraint> constraints(Solution solution) {
		List<Constraint> constraints = new LinkedList<Constraint>();
		Random random = new Random();
		for (int i = 0; i < 10; i++) {
			Constraint constraint = new Constraint();
			constraint.setType("type " + i);
			constraint.setTarget(random.nextDouble());
			constraint
					.setTargetProbability(((byte) Math.abs(random.nextInt() % 255)));
			constraint.setSolution(solution);
			constraints.add(constraint);
		}

		return constraints;
	}

	private void sortConstraints(List<Constraint> list) {
		Collections.sort(list, new Comparator<Constraint>() {

			@Override
			public int compare(Constraint object1, Constraint object2) {
				return object1.getType().compareTo(object2.getType());
			}
		});
	}

	private void constraintsTest(List<Constraint> constraints, Solution result) {
		List<Constraint> constraintsList = new LinkedList<Constraint>(
				constraints);
		sortConstraints(constraintsList);
		List<Constraint> cloneConstraints = new LinkedList<Constraint>(
				cloneConstraints(constraints, result));
		sortConstraints(cloneConstraints);
		assertTrue(constraints.size() == cloneConstraints.size());

		for (int i = 0; i < constraintsList.size(); i++) {

			assertTrue(constraintsList.get(i).getType()
					.equals(cloneConstraints.get(i).getType()));

			assertEquals(constraintsList.get(i).getTarget(), cloneConstraints
					.get(i).getTarget(), DELTA);
			assertEquals(constraintsList.get(i).getTargetProbability(), cloneConstraints
					.get(i).getTargetProbability());
			assertNotSame(constraintsList.get(i).getSolution(),
					cloneConstraints.get(i).getSolution());

		}
	}

	private List<SolutionStaffingShape> solutionStaffingShape(Solution solution) {
		List<SolutionStaffingShape> solutionStaffingShapes = new LinkedList<SolutionStaffingShape>();
		Random random = new Random();
		for (int i = 0; i < 4; i++) {
			SolutionStaffingShape solutionStaffingShape = new SolutionStaffingShape();
			solutionStaffingShape.setPhase(new Phase());
			solutionStaffingShape.setSelected(random.nextBoolean());
			solutionStaffingShape.setSolution(solution);

			solutionStaffingShape.setStaffingShape(new StaffingShape());
			solutionStaffingShapes.add(solutionStaffingShape);
		}
		return solutionStaffingShapes;

	}

	private void sortSolutionStaffingShapes(List<SolutionStaffingShape> list) {
		Collections.sort(list, new Comparator<SolutionStaffingShape>() {

			@Override
			public int compare(SolutionStaffingShape object1,
					SolutionStaffingShape object2) {
				return object1.getClass().getName()
						.compareTo(object2.getClass().getName());
			}

		});

	}

	private void solutionStaffingShapeTest(
			List<SolutionStaffingShape> solutionStaffingShapes, Solution result) {
		List<SolutionStaffingShape> solutionStaffingShapesList = new LinkedList<SolutionStaffingShape>(
				solutionStaffingShapes);
		sortSolutionStaffingShapes(solutionStaffingShapesList);
		List<SolutionStaffingShape> solutionStaffingShapesClone = new LinkedList<SolutionStaffingShape>(
				cloneSolutionStaffingShape(solutionStaffingShapes, result));
		sortSolutionStaffingShapes(solutionStaffingShapesClone);
		assertEquals(solutionStaffingShapesList.size(),
				solutionStaffingShapesClone.size());

		for (int i = 0; i < solutionStaffingShapesList.size(); i++) {
			assertSame(solutionStaffingShapesList.get(i).getPhase(),
					solutionStaffingShapesClone.get(i).getPhase());

			assertSame(solutionStaffingShapesList.get(i).getStaffingShape(),
					solutionStaffingShapesClone.get(i).getStaffingShape());

			assertSame(solutionStaffingShapesList.get(i).getStaffingShape(),
					solutionStaffingShapesClone.get(i).getStaffingShape());
			assertNotSame("solution and solutionClone are the same Object",
					solutionStaffingShapesList.get(i).getSolution(),
					solutionStaffingShapesClone.get(i).getSolution());

		}
	}

	private Solution solution(Estimate estimate) {
		Random random = new Random();
		Solution solution = new Solution();
		solution.setName("Solution");
		solution.setGranularity(new GranularityLevel());
		solution.setCreationDate(GregorianCalendar.getInstance());
		solution.setGearingFactor(random.nextInt());
		solution.setModifyDate(GregorianCalendar.getInstance());
		solution.setProjectStartDate(GregorianCalendar.getInstance());
		solution.setPi(random.nextInt());
		solution.setEstimate(estimate);
		solution.setConstraints(constraints(solution));
		List<GranularityQuestion> granularityQuestions = new LinkedList<GranularityQuestion>();
		for (int i = 0; i < 4; i++) {
			GranularityQuestion granularityQuestion = new GranularityQuestion();
			granularityQuestion.setFactor(random.nextDouble());
			granularityQuestion.setLevel(new GranularityLevel());
			granularityQuestion.setQuestion("question" + i);

			granularityQuestions.add(granularityQuestion);
		}
		solution.setGranularityQuestions(granularityQuestions);
		solution.setSolutionStaffingShapes(solutionStaffingShape(solution));
		solution.setUseCasePacks(useCasePack(solution, 0));
		return solution;

	}

	private void solutionTest(Solution solution, Solution solutionClone) {

		assertFalse(solution.getCreationDate() == solutionClone
				.getCreationDate());
		assertTrue(solution.getGearingFactor() == solutionClone
				.getGearingFactor());
		assertEquals(solution.getName(), solutionClone.getName());
		assertSame(solution.getGranularity(), solutionClone.getGranularity());
		assertFalse(solution.getModifyDate() == solutionClone.getModifyDate());
		assertTrue(solution.getProjectStartDate().equals(solutionClone.getProjectStartDate()));
		assertTrue(solution.getPi() == solutionClone.getPi());
		assertEquals("are not the Object", solution.getEstimate(),
				solutionClone.getEstimate());

		constraintsTest(solution.getConstraints(), solutionClone);

		solutionStaffingShapeTest(solution.getSolutionStaffingShapes(),
				solutionClone);
		useCasePackTest(solution.getUseCasePacks(), solutionClone);
		assertSame(solution.getGranularityQuestions(),
				solutionClone.getGranularityQuestions());
		for (int i = 0; i < solutionClone.getGranularityQuestions().size(); i++) {

			assertEquals(solutionClone.getGranularityQuestions().get(i)
					.getFactor(),solution.getGranularityQuestions().get(i)
					.getFactor(), DELTA);
			assertSame(solutionClone.getGranularityQuestions().get(i)
					.getLevel(), solution.getGranularityQuestions().get(i)
					.getLevel());
			assertTrue(solutionClone
					.getGranularityQuestions()
					.get(i)
					.getQuestion()
					.equals(solution.getGranularityQuestions().get(i)
							.getQuestion()));
		}
	}
}
