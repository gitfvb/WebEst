package com.ibm.webest.test.persistence.facades;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.List;

import javax.naming.NamingException;



import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ibm.webest.persistence.model.Certainty;
import com.ibm.webest.persistence.model.Estimate;
import com.ibm.webest.persistence.model.GranularityLevel;
import com.ibm.webest.persistence.model.GranularityQuestion;
import com.ibm.webest.persistence.model.PiHistoryCategory;
import com.ibm.webest.persistence.model.Solution;
import com.ibm.webest.persistence.model.UseCaseComplexity;
import com.ibm.webest.persistence.model.User;
import com.ibm.webest.persistence.service.CloneException;
import com.ibm.webest.persistence.service.DatabaseConnectionException;
import com.ibm.webest.persistence.service.EntityNotFoundException;
import com.ibm.webest.persistence.service.EstimateFacadeRemote;
import com.ibm.webest.persistence.service.QueryException;
import com.ibm.webest.persistence.service.SolutionFacadeRemote;
import com.ibm.webest.persistence.service.UserFacadeRemote;
import com.ibm.webest.test.persistence.EjbUtils;
import com.ibm.webest.test.persistence.JdbcUtils;
import com.ibm.webest.test.persistence.TestProperties;

/**
 * JUnit Test for SolutionFacade
 * 
 * @author Wail Shakir
 * @author Gregor Schumm
 * @author Dirk Kohlweyer
 */
public class SolutionFacadeTest {
	private static SolutionFacadeRemote solutionFacade;
	private static UserFacadeRemote userFacade;
	private static EstimateFacadeRemote estimateFacade;

	String[][] levels = { { "1", "Decomposed", "1" },
			{ "3", "Essential", "4" }, { "2", "Medium", "2" } };

	String[][] questions = {
			{ "1", "actor_gen", "0.05",
					"Actors in the use case model have been well generalized." },
			{
					"2",
					"actor_gen",
					"0.05",
					"Actors in the use case model are mostly generalized, but some concrete users may remain." },
			{ "3", "actor_gen", "0.05",
					"Use cases are tied to specific concrete users, not abstract actors." },

			{ "1", "altern_flows", "0.1",
					"Use cases contain many alternate flows." },
			{ "2", "altern_flows", "0.1",
					"Use cases contain few alternate flows." },
			{ "3", "altern_flows", "0.1",
					"Use cases usually contain 0-2 alternate flows." },
			{ "1", "arch_express", "0.1",
					"Use cases are not associated with system or architectural components." },
			{ "2", "arch_express", "0.1",
					"Use case occasionally span system or architectural components." },
			{
					"3",
					"arch_express",
					"0.1",
					"Use cases are completely locked to specific architectural or system components." },
			{
					"1",
					"basic_flows",
					"0.2",
					"All use case's basic flows complete a major interaction or transaction with the system." },
			{
					"2",
					"basic_flows",
					"0.2",
					"Some use case's basic flows complete a major interaction or transaction with the system; most interactions are broken up over more than one use case." },
			{
					"3",
					"basic_flows",
					"0.2",
					"No use case's basic flows complete a major interaction or transaction, but instead describe functions such as Search for a Client." },

			{ "1", "crud", "0.1",
					"CRUD functionality is a portion of an use case." },
			{
					"2",
					"crud",
					"0.1",
					"CRUD functionality is the totality of an use case, and sometimes two use cases." },
			{ "3", "crud", "0.1",
					"Each element of CRUD is dealt with in a separate use case." },
			{ "1", "ext_excl", "0.05",
					"Use of extends and includes statements is 20% or less of the use case model." },
			{ "2", "ext_excl", "0.05",
					"20-50% of the use case model are extensions or inclusions." },
			{ "3", "ext_excl", "0.05",
					"More than half of the use cases are extensions or inclusions." },
			{ "1", "indep_ui", "0.1",
					"Use cases are completely independent of user interface implementations." },
			{ "2", "indep_ui", "0.1",
					"Use cases assume specific user interface paradigms or implementations." },
			{
					"3",
					"indep_ui",
					"0.1",
					"Use cases are tied to specific screens or users functions, and contain specific UI design statements." },

			{ "1", "scenarios", "0.2",
					"Use cases represent many concrete examples of interaction, e.g. scenarios." },
			{
					"2",
					"scenarios",
					"0.2",
					"Use cases represent few scenarios or multiple use cases must be invoked to make a single scenario." },
			{
					"3",
					"scenarios",
					"0.2",
					"Scenarios almost always have to invoke several use cases to complete a useful example." },
			{ "1", "testing_appl", "0.1",
					"Use cases are useful for usability and system testing." },
			{ "2", "testing_appl", "0.1",
					"Use cases are useful for system and unit testing." },
			{ "3", "testing_appl", "0.1",
					"Use cases are useful for unit testing." } };

	String[][] complexities = { { "H", "High", "10.08" },
			{ "HR", "High Reuse", "3.6" }, { "L", "Low", "2.88" },
			{ "LR", "Low Reuse", "0.72" }, { "M", "Medium", "5.76" },
			{ "MR", "Medium Reuse", "2.16" }, { "VL", "Very Low", "0.576" },
			{ "VLR", "Very Low Reuse", "0.144" } };

	String[][] certainties = { { "H", "High", "1.0" }, { "L", "Low", "0.4" },
			{ "M", "Medium", "0.8" } };

	private static User user1;
	private static Estimate estimate1;

	@BeforeClass
	public static void init() throws NamingException, FileNotFoundException,
			SQLException, ClassNotFoundException, EntityNotFoundException,
			DatabaseConnectionException, QueryException {
		cleanup();
		JdbcUtils.createUser();
		solutionFacade = EjbUtils.getEjb(SolutionFacadeRemote.class,
				TestProperties.getEjbUsername(),
				TestProperties.getEjbPassword());
		userFacade = EjbUtils.getEjb(UserFacadeRemote.class,
				TestProperties.getEjbUsername(),
				TestProperties.getEjbPassword());
		estimateFacade = EjbUtils.getEjb(EstimateFacadeRemote.class,
				TestProperties.getEjbUsername(),
				TestProperties.getEjbPassword());
		JdbcUtils.executeScript("test_data_pi_history.sql");
		JdbcUtils.executeScript("test_data_phases.sql");
		user1 = userFacade.getUserById("test1");
		List<Estimate> est = estimateFacade.getEstimatesByUser(user1);
		if (est.size() > 0)
			estimate1 = est.get(0);
	}

	@Before
	public void resetTestData() throws SQLException, ClassNotFoundException,
			FileNotFoundException, EntityNotFoundException,
			DatabaseConnectionException, QueryException {
		cleanup();
		JdbcUtils.executeScript("test_data_pi_history.sql");
		JdbcUtils.executeScript("test_data_phases.sql");
		List<Estimate> est = estimateFacade.getEstimatesByUser(user1);
		if (est.size() > 0)
			estimate1 = est.get(0);
	}

	@Test
	public void getGranularityLevels() throws DatabaseConnectionException {
		List<GranularityLevel> granularityLevels = solutionFacade
				.getGranularityLevels();
		Collections.sort(granularityLevels, new Comparator<GranularityLevel>() {

			@Override
			public int compare(GranularityLevel object1,
					GranularityLevel object2) {
				return object1.getName().compareTo(object2.getName());
			}
		});

		assertEquals(granularityLevels.size(), levels.length);
		for (int i = 0; i < levels.length; i++) {

			assertEquals(Integer.parseInt(levels[i][0]),
					(int) granularityLevels.get(i).getId());
			assertEquals(Integer.parseInt(levels[i][2]),
					(int) granularityLevels.get(i).getValue());
			assertEquals(levels[i][1], granularityLevels.get(i).getName());

		}
	}

	@Test
	public void getGranularityQuestions() throws DatabaseConnectionException {
		List<GranularityQuestion> granularityQuestions = solutionFacade
				.getGranularityQuestions();
		Collections.sort(granularityQuestions,
				new Comparator<GranularityQuestion>() {

					@Override
					public int compare(GranularityQuestion object1,
							GranularityQuestion object2) {
						int value = object1.getFactorName().compareTo(
								object2.getFactorName());
						if (value == 0)
							return ("" + object1.getLevel().getId())
									.compareTo(object2.getLevel().getId() + "");
						else
							return value;
					}
				});
		assertEquals(questions.length, granularityQuestions.size());
		for (int i = 0; i < questions.length; i++) {
			assertEquals(questions[i][0], ""
					+ granularityQuestions.get(i).getLevel().getId());
			assertEquals(questions[i][1], granularityQuestions.get(i)
					.getFactorName());
			assertEquals(questions[i][2], ""
					+ granularityQuestions.get(i).getFactor());
			assertEquals(questions[i][3], granularityQuestions.get(i)
					.getQuestion());
		}
	}

	@Test
	public void getUseCaseComplexities() throws DatabaseConnectionException {
		List<UseCaseComplexity> caseComplexities = solutionFacade
				.getUseCaseComplexities();
		Collections.sort(caseComplexities, new Comparator<UseCaseComplexity>() {

			@Override
			public int compare(UseCaseComplexity object1,
					UseCaseComplexity object2) {
				return object1.getId().compareTo(object2.getId());
			}
		});
		for (int i = 0; i < complexities.length; i++) {
			assertEquals(complexities[i][0], caseComplexities.get(i).getId());
			assertEquals(complexities[i][1], caseComplexities.get(i).getName());
			assertEquals(complexities[i][2], ""
					+ caseComplexities.get(i).getFactor());

		}
	}

	@Test
	public void getUseCaseCertainties() throws DatabaseConnectionException {
		List<Certainty> useCaseCertainties = solutionFacade
				.getUseCaseCertainties();
		Collections.sort(useCaseCertainties, new Comparator<Certainty>() {

			@Override
			public int compare(Certainty object1, Certainty object2) {
				return object1.getId().compareTo(object2.getId());
			}
		});
		for (int i = 0; i < certainties.length; i++) {
			assertEquals(certainties[i][0], useCaseCertainties.get(i).getId());
			assertEquals(certainties[i][1], useCaseCertainties.get(i).getName());
			assertEquals(certainties[i][2], ""
					+ useCaseCertainties.get(i).getFactor());
		}
	}

	@Test
	public void getPiHistoryCategories() throws FileNotFoundException,
			SQLException, ClassNotFoundException, DatabaseConnectionException {
		// get list of pi history categories
		List<PiHistoryCategory> result = solutionFacade
				.getPiHistoryCategories();

		List<String> names = new ArrayList<String>();

		for (PiHistoryCategory p : result)
			names.add(p.getName());

		// verify that result contains all categories.
		assertTrue(names.contains("Kategorie 1"));
		assertTrue(names.contains("Kategorie 2"));
		assertTrue(names.contains("Kategorie 3"));

		// verify that result does not contain any other categories
		assertEquals(names.size(), 3);
	}

	@Test(expected = EntityNotFoundException.class)
	public void cloneSolutionException() throws EntityNotFoundException,
			DatabaseConnectionException, CloneException {
		solutionFacade.cloneSolution(null);
	}

	@Test
	public void getSolutions() throws EntityNotFoundException,
			DatabaseConnectionException, QueryException {
		List<Solution> result = solutionFacade.getSolutions(estimate1, 2);

		ArrayList<Long> ids = new ArrayList<Long>();

		for (Solution s : result)
			ids.add(s.getId());

		// verify that result contains the two most recently modified solutions
		// that belong to "Estimate1"
		assertTrue(ids.contains(new Long(2)));
		assertTrue(ids.contains(new Long(3)));

		// verify that result does not contain any other solutions
		assertEquals(2, result.size());
	}

	@Test
	public void deleteSolution() throws EntityNotFoundException,
			DatabaseConnectionException, QueryException {
		List<Solution> result = solutionFacade.getSolutions(estimate1, 3);

		// delete the most recently modified solution (id=3)
		solutionFacade.deleteSolution(result.get(0).getId());

		result = solutionFacade.getSolutions(estimate1, 3);

		ArrayList<Long> ids = new ArrayList<Long>();

		for (Solution s : result)
			ids.add(s.getId());

		// verify that result does not contain "TEST_ESTIMATE 6"
		assertFalse(ids.contains(new Long(3)));

		// verify that all other estimates are still existing
		assertEquals(2, result.size());
	}

	@Test
	public void loadSolutionById() throws DatabaseConnectionException,
			EntityNotFoundException {

		Solution solution = solutionFacade.loadSolutionById(1);
		assertTrue(solution.getEstimate().getId() == 1);
		assertTrue(solution.getLastEditor().getId().equals("test1"));
	}

	@Test
	public void saveSolution() throws EntityNotFoundException,
			DatabaseConnectionException, QueryException {
		List<Estimate> estimates = estimateFacade.getAllEstimates();
		Solution solution = solutionFacade.getSolutions(estimates.get(0), 1)
				.get(0);
		int pi = solution.getPi();

		Calendar modifyDate = (Calendar) solution.getModifyDate().clone();
		Calendar startDate = (Calendar) solution.getProjectStartDate().clone();
		solution.setPi(4);
		solution.setModifyDate(GregorianCalendar.getInstance());
		solution.setProjectStartDate(GregorianCalendar.getInstance());
		solutionFacade.saveSolution(solution);
		Solution solution2 = solutionFacade.loadSolutionById(solution.getId());
		assertTrue(solution.getId() == solution2.getId());
		assertFalse(pi == solution2.getPi());
		assertTrue(solution2.getPi() == 4);
		assertTrue(solution2.getModifyDate().compareTo(modifyDate) > 0);
		assertTrue(solution2.getProjectStartDate().compareTo(startDate) > 0);
	}

	@AfterClass
	public static void cleanup() throws SQLException, ClassNotFoundException {
		JdbcUtils.getJdbcConnection().createStatement()
				.execute("DELETE FROM PIHISTORYCATEGORY");
		// delete test-data
		JdbcUtils.getJdbcConnection().createStatement()
				.execute("DELETE FROM \"ESTIMATE\"");
		JdbcUtils.getJdbcConnection().createStatement()
				.execute("DELETE FROM \"USER\" WHERE id <> 'junit'");
		JdbcUtils.getJdbcConnection().createStatement()
				.execute("DELETE FROM \"DIVISION\"");
		JdbcUtils.getJdbcConnection().createStatement()
				.execute("DELETE FROM \"ORGANIZATION\"");
		JdbcUtils.getJdbcConnection().createStatement()
				.execute("DELETE FROM \"APPLICATIONTYPEGROUP\"");
		JdbcUtils.getJdbcConnection().createStatement()
				.execute("DELETE FROM \"APPLICATIONTYPE\"");
		JdbcUtils.getJdbcConnection().createStatement()
				.execute("DELETE FROM \"INDUSTRYSECTORGROUP\"");
		JdbcUtils.getJdbcConnection().createStatement()
				.execute("DELETE FROM \"INDUSTRYSECTOR\"");
		JdbcUtils.getJdbcConnection().createStatement()
				.execute("DELETE FROM \"PHASE\"");
		JdbcUtils.getJdbcConnection().createStatement()
				.execute("DELETE FROM \"MILESTONE\"");
		JdbcUtils.getJdbcConnection().createStatement()
				.execute("DELETE FROM \"DEFECTCATEGORY\"");
		JdbcUtils.getJdbcConnection().createStatement()
				.execute("DELETE FROM \"TEMPLATE\"");
	}
}
