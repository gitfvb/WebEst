package com.ibm.webest.test.processing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ibm.webest.persistence.model.Certainty;
import com.ibm.webest.persistence.model.GranularityLevel;
import com.ibm.webest.persistence.model.GranularityQuestion;
import com.ibm.webest.persistence.model.Solution;
import com.ibm.webest.persistence.model.UseCase;
import com.ibm.webest.persistence.model.UseCaseComplexity;
import com.ibm.webest.persistence.model.UseCasePack;
import com.ibm.webest.persistence.service.DatabaseConnectionException;
import com.ibm.webest.persistence.service.EntityNotFoundException;
import com.ibm.webest.persistence.service.SolutionFacadeRemote;
import com.ibm.webest.processing.administration.GuiServiceException;
import com.ibm.webest.processing.administration.GuiServiceRemote;
import com.ibm.webest.processing.calculation.SizingCalculatorException;
import com.ibm.webest.processing.calculation.SizingCalculatorServiceRemote;
import com.ibm.webest.test.processing.tools.JdbcUtils;

/**
 * Testclass for SizingCalculatorService
 * @author Andre Munzinger
 * @author Wail Shakir
 */
public class TestSizingCalculatorService {

	private static SizingCalculatorServiceRemote sizingService;
	private static GuiServiceRemote guiService;
	private static SolutionFacadeRemote solFac;
	int[] ucp = { 5, 5, 5, 5, 5, 10, 10, 14 };
	int[] totalUcp = { 5, 25, 35, 40, 75, 100, 200, 420 };
	int ucpSum = 59;
	int totalUcpSum = 900;

	/**
	 * Assures a clean an consistent testenvironment
	 * @throws FileNotFoundException If the given file couldn't be found
	 * @throws SQLException If there was a invalid sqlsintruction
	 * @throws ClassNotFoundException If the given class couldn't be found
	 * @throws NamingException If the Naming is wrong
	 */
	@BeforeClass
	public static void beforeClass() throws FileNotFoundException, SQLException, ClassNotFoundException, NamingException {
		// Create new Testuser in DB
		JdbcUtils.createUser();
		
		// Set needed beans
		sizingService = EjbUtils.getEjb(SizingCalculatorServiceRemote.class,
				TestProperties.getEjbUsername(),
				TestProperties.getEjbPassword());
		guiService = EjbUtils.getEjb(GuiServiceRemote.class,
				TestProperties.getEjbUsername(),
				TestProperties.getEjbPassword());
		solFac = EjbUtils.getEjb(SolutionFacadeRemote.class,
				TestProperties.getEjbUsername(),
				TestProperties.getEjbPassword());
	}

	/**
	 * Cleans up the environment by executing sqlscripts
	 * @throws FileNotFoundException If the given file couldn't be found
	 * @throws SQLException If there was a invalid sqlsintruction
	 * @throws ClassNotFoundException If the given class couldn't be found
	 */
	@AfterClass
	public static void cleanUpClass() throws FileNotFoundException, SQLException, ClassNotFoundException {
		JdbcUtils.executeScript("del_test_data_Szenario.sql");
		JdbcUtils.executeScript("test_data_Szenario.sql");
	}

	/**
	 * Test for calcUseCaseGranularity which tests a null value
	 * @throws SizingCalculatorException
	 */
	@Test(expected = SizingCalculatorException.class)
	public void testCalcUseCaseGranularityException() throws SizingCalculatorException {
		sizingService.calcUseCaseGranularity(null);
		assertTrue(false);
	}

	/**
	 * Test for calcUseCaseGranularity which tests a corrupt value
	 * @throws SizingCalculatorException
	 */
	@Test(expected = SizingCalculatorException.class)
	public void calcUseCaseGranularityException2() throws SizingCalculatorException {

		List<GranularityQuestion> questions = new ArrayList<GranularityQuestion>();

		sizingService.calcUseCaseGranularity(questions);
		assertTrue(false);
	}

	/**
	 * Test for calcUseCaseGranularity which tests a correct decomposed value
	 * @throws SizingCalculatorException
	 * @throws DatabaseConnectionException If there is an error in the DB
	 * @throws GuiServiceException If there was a problem in getting the granularity questions
	 */
	@Test
	public void testCalcUseCaseGranularityDecomposed()
			throws SizingCalculatorException, DatabaseConnectionException,
			GuiServiceException {

		Map<String, Map<GranularityLevel, GranularityQuestion>> questions = guiService
				.getAllGranularityQuestions();
		List<GranularityQuestion> q = new ArrayList<GranularityQuestion>();
		List<GranularityLevel> levels = guiService.getAllGranularityLevels();

		q.add(questions.get("basic_flows").get(levels.get(0)));
		q.add(questions.get("arch_express").get(levels.get(0)));
		q.add(questions.get("actor_gen").get(levels.get(0)));
		q.add(questions.get("indep_ui").get(levels.get(0)));
		q.add(questions.get("ext_excl").get(levels.get(0)));
		q.add(questions.get("altern_flows").get(levels.get(0)));
		q.add(questions.get("crud").get(levels.get(0)));
		q.add(questions.get("scenarios").get(levels.get(1)));
		q.add(questions.get("testing_appl").get(levels.get(2)));

		GranularityLevel result = sizingService.calcUseCaseGranularity(q);
		assertEquals(3, result.getId());
	}

	
	/**
	 * Test for calcUseCaseGranularity which tests a correct medium value
	 * @throws SizingCalculatorException
	 * @throws DatabaseConnectionException If there is an error in the DB
	 * @throws GuiServiceException If there was a problem in getting the granularity questions
	 */
	@Test
	public void testCalcUseCaseGranularityMedium()
			throws SizingCalculatorException, DatabaseConnectionException,
			GuiServiceException {

		Map<String, Map<GranularityLevel, GranularityQuestion>> questions = guiService
				.getAllGranularityQuestions();
		List<GranularityQuestion> q = new ArrayList<GranularityQuestion>();
		List<GranularityLevel> levels = guiService.getAllGranularityLevels();

		q.add(questions.get("basic_flows").get(levels.get(1)));
		q.add(questions.get("arch_express").get(levels.get(1)));
		q.add(questions.get("actor_gen").get(levels.get(1)));
		q.add(questions.get("indep_ui").get(levels.get(1)));
		q.add(questions.get("ext_excl").get(levels.get(1)));
		q.add(questions.get("altern_flows").get(levels.get(1)));
		q.add(questions.get("crud").get(levels.get(1)));
		q.add(questions.get("scenarios").get(levels.get(0)));
		q.add(questions.get("testing_appl").get(levels.get(2)));

		GranularityLevel result = sizingService.calcUseCaseGranularity(q);
		assertEquals(2, result.getId());
	}

	
	/**
	 * Test for calcUseCaseGranularity which tests a correct essential value
	 * @throws SizingCalculatorException
	 * @throws DatabaseConnectionException If there is an error in the DB
	 * @throws GuiServiceException If there was a problem in getting the granularity questions
	 */
	@Test
	public void testCalcUseCaseGranularityEssetial()
			throws SizingCalculatorException, DatabaseConnectionException,
			GuiServiceException {

		Map<String, Map<GranularityLevel, GranularityQuestion>> questions = guiService
				.getAllGranularityQuestions();
		List<GranularityQuestion> q = new ArrayList<GranularityQuestion>();
		List<GranularityLevel> levels = guiService.getAllGranularityLevels();

		q.add(questions.get("basic_flows").get(levels.get(2)));
		q.add(questions.get("arch_express").get(levels.get(2)));
		q.add(questions.get("actor_gen").get(levels.get(2)));
		q.add(questions.get("indep_ui").get(levels.get(2)));
		q.add(questions.get("ext_excl").get(levels.get(2)));
		q.add(questions.get("altern_flows").get(levels.get(2)));
		q.add(questions.get("crud").get(levels.get(2)));
		q.add(questions.get("scenarios").get(levels.get(0)));
		q.add(questions.get("testing_appl").get(levels.get(1)));

		GranularityLevel result = sizingService.calcUseCaseGranularity(q);
		assertEquals(1, result.getId());
	}

	
	/**
	 * Test for calcOverallCertanty which tests a null value
	 * @throws SizingCalculatorException
	 */
	@Test(expected = SizingCalculatorException.class)
	public void testCalcOverallCertantyException() throws SizingCalculatorException {
		sizingService.calcOverallCertanty(null);
		assertTrue(false);
	}

	
	/**
	 * Tests calcOverallCertanty with a valid value
	 * @throws SizingCalculatorException If something went wrong
	 * @throws DatabaseConnectionException If there is an error in the DB
	 */
	@Test
	public void testCalcOverallCertanty() throws SizingCalculatorException,
			DatabaseConnectionException {

		// manually calculated values
		float delta = 0.001f;
		float result = 0.6f;
		float oCertanty;

		// needed test infrastructure
		Solution sol = new Solution();
		UseCasePack pack = new UseCasePack();
		List<UseCasePack> useCasePackList = new ArrayList<UseCasePack>();
		List<UseCase> useCaseList = new ArrayList<UseCase>();

		List<Certainty> certaintiesList = solFac.getUseCaseCertainties();

		// set up use case configuration
		Certainty cert1 = new Certainty();
		Certainty cert2 = new Certainty();

		cert1.setId("M");
		cert2.setId("L");
		cert1 = certaintiesList.get(certaintiesList.indexOf(cert1));
		cert2 = certaintiesList.get(certaintiesList.indexOf(cert2));

		// define usecases
		UseCase case1, case2, case3, case4, case5;

		case1 = new UseCase();
		case1.setCertainty(cert1);
		case1.setInScope(true);

		case2 = new UseCase();
		case2.setCertainty(cert1);
		case2.setInScope(true);

		case3 = new UseCase();
		case3.setCertainty(cert2);
		case3.setInScope(true);

		case4 = new UseCase();
		case4.setCertainty(cert2);
		case4.setInScope(true);

		case5 = new UseCase();
		case5.setCertainty(cert2);
		case5.setInScope(false);

		useCaseList.add(case1);
		useCaseList.add(case2);
		useCaseList.add(case3);
		useCaseList.add(case4);

		pack.setUseCases(useCaseList);
		useCasePackList.add(pack);
		sol.setUseCasePacks(useCasePackList);
		oCertanty = sizingService.calcOverallCertanty(sol);

		assertEquals(result, oCertanty, delta);
	}

	/**
	 * Test for calcUncertantyRange which tests a null value
	 * @throws SizingCalculatorException
	 */
	@Test(expected = SizingCalculatorException.class)
	public void testCalcUncertantyRangeException() throws SizingCalculatorException {
		sizingService.calcUncertantyRange(null);
		assertTrue(false);
	}

	
	/**
	 * Test for calcUncertantyRange which tests a valid value
	 * @throws SizingCalculatorException
	 * @throws DatabaseConnectionException If there is an error in the DB
	 */
	@Test
	public void testCalcUncertantyRange() throws SizingCalculatorException,
			DatabaseConnectionException {

		// manually calculated values
		float delta = 0.001f;
		float result = 0.7f;
		float uncertantyRange = 1 - result;
		float oCertanty;

		// needed test infrastructure
		Solution sol = new Solution();
		UseCasePack pack = new UseCasePack();
		List<UseCasePack> useCasePackList = new ArrayList<UseCasePack>();
		List<UseCase> useCaseList = new ArrayList<UseCase>();
		List<Certainty> certaintiesList = solFac.getUseCaseCertainties();

		// set up use case configuration
		Certainty cert1 = new Certainty();
		Certainty cert2 = new Certainty();

		cert1.setId("H");
		cert2.setId("L");
		cert1 = certaintiesList.get(certaintiesList.indexOf(cert1));
		cert2 = certaintiesList.get(certaintiesList.indexOf(cert2));

		// define use cases
		UseCase case1, case2, case3, case4;

		case1 = new UseCase();
		case1.setCertainty(cert1);
		case1.setInScope(true);

		case2 = new UseCase();
		case2.setCertainty(cert1);
		case2.setInScope(true);

		case3 = new UseCase();
		case3.setCertainty(cert2);
		case3.setInScope(true);

		case4 = new UseCase();
		case4.setCertainty(cert2);
		case4.setInScope(true);

		useCaseList.add(case1);
		useCaseList.add(case2);
		useCaseList.add(case3);
		useCaseList.add(case4);

		pack.setUseCases(useCaseList);
		useCasePackList.add(pack);
		sol.setUseCasePacks(useCasePackList);
		oCertanty = 1 - sizingService.calcOverallCertanty(sol);

		assertEquals(uncertantyRange, oCertanty, delta);

	}

	
	/**
	 * Test for calculateTotalUCP which tests a null value
	 * @throws SizingCalculatorException
	 */
	@Test(expected = SizingCalculatorException.class)
	public void testCalculateTotalUCPException() throws SizingCalculatorException {
		sizingService.calculateTotalUCP(null);
		assertTrue(false);
	}

	
	/**
	 * Test for calculateTotalUCP which tests a valid value
	 * @throws SizingCalculatorException
	 */
	@Test
	public void testCalculateTotalUCP() throws SizingCalculatorException {

		try {
			Solution solution = solFac.loadFullSolution(solFac
					.loadSolutionById(4));
			ArrayList<ArrayList<Integer>> arrayList = sizingService
					.calculateTotalUCP(solution);
			List<Integer> ucp = new ArrayList<Integer>();
			List<Integer> totalUcp = new ArrayList<Integer>();
			for (int i = 0; i < arrayList.size(); i++) {
				ucp.add(arrayList.get(i).get(0));
				totalUcp.add(arrayList.get(i).get(1));
			}
			for (int i = 0; i < 2; i++) {
				Collections.sort((i == 0) ? ucp : totalUcp,
						new Comparator<Integer>() {

							@Override
							public int compare(Integer object1, Integer object2) {
								return object1 - object2;
							}
						});
			}

			int ucpSum = 0;
			int totalUcpSum = 0;
			for (int i = 0; i < arrayList.size(); i++) {
				assertTrue(ucp.get(i) == this.ucp[i]);
				assertTrue(totalUcp.get(i) == this.totalUcp[i]);
				ucpSum += ucp.get(i);
				totalUcpSum += totalUcp.get(i);
			}

			assertTrue(ucpSum == this.ucpSum);
			assertTrue(totalUcpSum == this.totalUcpSum);
		} catch (EntityNotFoundException e) {
			e.printStackTrace();
		} catch (DatabaseConnectionException e) {
			e.printStackTrace();
		}

	}

	
	/**
	 * Test for calcUCP which tests a null value
	 * @throws SizingCalculatorException
	 */
	@Test(expected = SizingCalculatorException.class)
	public void testCalcUCPException() throws SizingCalculatorException {
		sizingService.calcUCP(null, null);
		assertTrue(false);
	}

	
	/**
	 * Test for calcUCP which tests a valid value
	 * @throws SizingCalculatorException
	 * @throws DatabaseConnectionException If there was an error in DB
	 */
	@Test
	public void testCalcUCP() throws SizingCalculatorException,
			DatabaseConnectionException {

		// manually calculated values
		float result = 20;
		float delta = 0.001f;
		float ucp;

		// needed test infrastructure
		UseCaseComplexity complexity = new UseCaseComplexity();
		Byte medium = 2;
		List<UseCaseComplexity> useCaseCompList = solFac
				.getUseCaseComplexities();

		GranularityLevel gLevel;
		gLevel = new GranularityLevel();
		gLevel.setValue(medium);

		// define use case complexity
		complexity.setId("H");
		complexity = useCaseCompList.get(useCaseCompList.indexOf(complexity));

		// define test use case
		UseCase case1;
		case1 = new UseCase();
		case1.setInScope(true);
		case1.setUseCaseComplexity(complexity);

		ucp = sizingService.calcUCP(case1, gLevel);
		assertEquals(result, ucp, delta);
	}
}
