package com.ibm.webest.test.processing;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.naming.NamingException;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ibm.webest.persistence.model.Estimate;
import com.ibm.webest.persistence.model.Solution;
import com.ibm.webest.persistence.model.UseCase;
import com.ibm.webest.persistence.model.UseCasePack;
import com.ibm.webest.persistence.service.DatabaseConnectionException;
import com.ibm.webest.persistence.service.EntityNotFoundException;
import com.ibm.webest.persistence.service.EstimateFacadeRemote;
import com.ibm.webest.persistence.service.ProjectEnvironmentFacadeRemote;
import com.ibm.webest.persistence.service.SolutionFacadeRemote;
import com.ibm.webest.persistence.service.UserFacadeRemote;
import com.ibm.webest.processing.administration.EstimationManagerException;
import com.ibm.webest.processing.administration.EstimationManagerServiceRemote;
import com.ibm.webest.test.processing.tools.JdbcUtils;

/**
 * Testclass for EstimationManagerService
 * This class tested the function from EstimateManagerService.
 * @author Wail Shakir
 * @author Andre Munzinger
 */
public class TestEstimationManagerService {

	private static EstimationManagerServiceRemote estMgr;
	private static SolutionFacadeRemote solFac;
	private static EstimateFacadeRemote estFac;
	private static ProjectEnvironmentFacadeRemote projectEnvFac;
	private static UserFacadeRemote userFac;

	@BeforeClass
	public static void beforeClass() throws FileNotFoundException,
			SQLException, ClassNotFoundException, NamingException {
		JdbcUtils.createUser();
		estMgr = EjbUtils.getEjb(EstimationManagerServiceRemote.class,
				TestProperties.getEjbUsername(),
				TestProperties.getEjbPassword());
		solFac = EjbUtils.getEjb(SolutionFacadeRemote.class,
				TestProperties.getEjbUsername(),
				TestProperties.getEjbPassword());
		estFac = EjbUtils.getEjb(EstimateFacadeRemote.class,
				TestProperties.getEjbUsername(),
				TestProperties.getEjbPassword());
		projectEnvFac = EjbUtils.getEjb(ProjectEnvironmentFacadeRemote.class,
				TestProperties.getEjbUsername(),
				TestProperties.getEjbPassword());
		userFac = EjbUtils.getEjb(UserFacadeRemote.class,
				TestProperties.getEjbUsername(),
				TestProperties.getEjbPassword());
	}

	@Before
	public void init() throws FileNotFoundException, SQLException,
			ClassNotFoundException, NamingException {
		CleanUp();
		JdbcUtils.executeScript("test_data_Szenario.sql");
	}

	@AfterClass
	public static void cleanUpClass() throws FileNotFoundException,
			SQLException, ClassNotFoundException {
		CleanUp();
		JdbcUtils.executeScript("test_data_Szenario.sql");
	}

	private static void CleanUp() throws SQLException, ClassNotFoundException,
			FileNotFoundException {

		JdbcUtils.executeScript("del_test_data_Szenario.sql");
	}

	@Test(expected = EntityNotFoundException.class)
	public void deleteEstimate() throws EstimationManagerException,
			EntityNotFoundException, DatabaseConnectionException {
		Estimate est = estMgr.getAllEstimates().get(0);
		estMgr.deleteEstimate(est);
		estFac.loadFullEstimate(est);
	}

	@Test(expected = EstimationManagerException.class)
	public void deleteEstimateException() throws EstimationManagerException {
		estMgr.deleteEstimate(null);
	}

	@Test(expected = EstimationManagerException.class)
	public void deleteSolution() throws EstimationManagerException,
			EntityNotFoundException, DatabaseConnectionException {
		Estimate est = estMgr.getAllEstimates().get(0);
		Solution sol = est.getSolutions().get(0);
		estMgr.deleteSolution(sol);
		estMgr.saveEstimate(est);
		solFac.loadFullSolution(sol);
	}

	@Test(expected = EstimationManagerException.class)
	public void deleteSolutionException() throws EstimationManagerException {
		estMgr.deleteSolution(null);
	}

	@Test
	public void createEstimate() throws EstimationManagerException,
			DatabaseConnectionException, EntityNotFoundException {
		Estimate est = estMgr.createEstimate(projectEnvFac.getTemplates()
				.get(0));

		est.setDivision(userFac.getUserById("test1").getDivision());
		est.setLastEditor(userFac.getUserById("test1"));
		est.setEstimator(userFac.getUserById("test1"));
		est.setComment("Comment");
		est.setName("TestEstimateXXX");
		est.setCreationDate(GregorianCalendar.getInstance());
		est.setModifyDate(GregorianCalendar.getInstance());
		est.getProjectEnvironment().setProjectName("projectName");
		est.getProjectEnvironment().setApplicationType(
				projectEnvFac.getApplicationTypeGroups().get(0)
						.getApplicationTypes().get(0));
		est.getProjectEnvironment().setCountry(
				projectEnvFac.getCountries().get(0));
		est.getProjectEnvironment().setEffortUnit(
				projectEnvFac.getEffortUnits().get(0));
		est.getProjectEnvironment().setIndustrySector(
				projectEnvFac.getIndustrySectorGroups().get(0)
						.getIndustrySectors().get(0));
		est.getProjectEnvironment().setMonetaryUnit(
				projectEnvFac.getMonetaryUnits().get(0));
		est.getProjectEnvironment().setMttdTimeUnit(
				projectEnvFac.getMTTDTimeUnits().get(0));
		Estimate estimate = estMgr.saveEstimate(est);

		assertTrue(estMgr.getAllEstimates().contains(estimate));
	}

	@Test(expected = EstimationManagerException.class)
	public void createEstimateException() throws EstimationManagerException {
		estMgr.createEstimate(null);
	}

	@Test
	public void createSolution() throws EstimationManagerException,
			DatabaseConnectionException, EntityNotFoundException {
		Estimate est = estMgr.getAllEstimates().get(0);

		Solution sol = estMgr.createSolution(estFac.loadFullEstimate(est));
		sol.setName("TestSolution");
		sol.setEstimate(estFac.loadFullEstimate(est));
		sol.setGearingFactor(125);
		sol.setProjectStartDate(GregorianCalendar.getInstance());
		sol.setCreationDate(GregorianCalendar.getInstance());
		sol.setModifyDate(GregorianCalendar.getInstance());
		sol.setLastEditor(userFac.getUserById("test1"));
		sol.setGranularity(solFac.getGranularityLevelById((byte) 1));

		UseCasePack useCasePack = new UseCasePack();
		useCasePack.setName("useCasePack");

		for (int i = 0; i < 3; i++) {
			UseCase useCase = new UseCase();
			useCase.setCertainty(solFac.getUseCaseCertainties().get(0));
			useCase.setUseCaseComplexity(solFac.getUseCaseComplexities().get(0));
			useCase.setMultiplier((i + 1) * 5);
			useCase.setInScope(true);
			useCase.setName("useCase " + i);
			useCase.setUseCasePack(useCasePack);
			useCase.setGranularityOverride(solFac
					.getGranularityLevelById((byte) 1));
		}
		useCasePack.setSolution(sol);
		List<UseCasePack> useCasePacks = new ArrayList<UseCasePack>();
		useCasePacks.add(useCasePack);
		sol.setUseCasePacks(useCasePacks);

		Solution solution = estMgr.saveSolution(sol);

		assertTrue(estMgr.getAllEstimates().get(0).getSolutions()
				.contains(solution));

	}

	@Test
	public void cloneEstimate() throws DatabaseConnectionException,
			EstimationManagerException, EntityNotFoundException {
		Estimate est = estMgr.getAllEstimates().get(0);

		Estimate clone = estMgr.cloneEstimate(estFac.loadFullEstimate(est));

		Estimate estimate = estMgr.saveEstimate(clone);

		assertTrue(estMgr.getAllEstimates().contains(estimate));
	}

	@Test(expected = EstimationManagerException.class)
	public void cloneEstimateException() throws EstimationManagerException {
		estMgr.cloneEstimate(null);
	}

	@Test
	public void cloneSolution() throws DatabaseConnectionException,
			EstimationManagerException {
		Solution sol = estMgr.getAllEstimates().get(0).getSolutions().get(0);

		Solution clone = estMgr.cloneSolutionForEstimate(sol);

		Solution solution = estMgr.saveSolution(clone);

		assertTrue(estMgr.getAllEstimates().get(0).getSolutions()
				.contains(solution));
	}

	@Test(expected = EstimationManagerException.class)
	public void cloneSolutionException() throws EstimationManagerException {
		estMgr.cloneSolutionForEstimate(null);
	}
}