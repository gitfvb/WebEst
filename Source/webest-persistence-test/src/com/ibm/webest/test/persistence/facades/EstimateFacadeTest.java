package com.ibm.webest.test.persistence.facades;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


import com.ibm.webest.persistence.model.Division;
import com.ibm.webest.persistence.model.Estimate;

import com.ibm.webest.persistence.model.ProjectEnvironment;
import com.ibm.webest.persistence.model.User;
import com.ibm.webest.persistence.service.Cloner;
import com.ibm.webest.persistence.service.DatabaseConnectionException;
import com.ibm.webest.persistence.service.EntityNotFoundException;
import com.ibm.webest.persistence.service.EstimateFacadeRemote;
import com.ibm.webest.persistence.service.QueryException;
import com.ibm.webest.persistence.service.UserFacadeRemote;
import com.ibm.webest.test.persistence.EjbUtils;
import com.ibm.webest.test.persistence.JdbcUtils;
import com.ibm.webest.test.persistence.TestProperties;

/**
 * JUnit Test for EstimateFacade
 * 
 * @author Wail Shakir
 * @author Gregor Schumm
 * @author Dirk Kohlweyer
 */
public class EstimateFacadeTest {
	private static EstimateFacadeRemote estimateFacade;
	private static UserFacadeRemote userFacade;

	private static User user1;
	private static User user2;
	private static User user3;
	private static Division division1;
	private static Division division3;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// delete all entries from tables USER, DIVISION, ORGANIZATION and
		// ESTIMATE
		cleanup();

		// insert test-data
		JdbcUtils.executeScript("test_data_phases.sql");

		JdbcUtils.createUser();
		estimateFacade = EjbUtils.getEjb(EstimateFacadeRemote.class,
				TestProperties.getEjbUsername(),
				TestProperties.getEjbPassword());
		userFacade = EjbUtils.getEjb(UserFacadeRemote.class,
				TestProperties.getEjbUsername(),
				TestProperties.getEjbPassword());

		// get user "test1"
		user1 = userFacade.getUserById("test1");
		// get user "test2"
		user2 = userFacade.getUserById("test2");
		// get user "test3"
		user3 = userFacade.getUserById("test3");
		// get division "TestDivision1"
		division1 = user1.getDivision();
		// get division "TestDivision3"
		division3 = user3.getDivision();
	}

	@Before
	public void resetTestData() throws SQLException, ClassNotFoundException,
			FileNotFoundException {
		// delete all entries from tables USER, DIVISION, ORGANIZATION and
		// ESTIMATE
		cleanup();

		// insert test-data
		JdbcUtils.executeScript("test_data_phases.sql");

	}

	@Test(expected = EntityNotFoundException.class)
	public void getEstimatesByDivisionEntityException()
			throws EntityNotFoundException, DatabaseConnectionException,
			QueryException {
		estimateFacade.getEstimatesByDivision(null);
	}

	@Test(expected = EntityNotFoundException.class)
	public void getEstimatesByDivisionWithLimitEntityException()
			throws EntityNotFoundException, DatabaseConnectionException,
			QueryException {
		estimateFacade.getEstimatesByDivision(null, 5);
	}

	@Test(expected = EntityNotFoundException.class)
	public void getEstimatesByUserEntityException()
			throws EntityNotFoundException, DatabaseConnectionException,
			QueryException {

		estimateFacade.getEstimatesByUser(null);
	}

	@Test(expected = EntityNotFoundException.class)
	public void getEstimatesByUserWithLimitEntityException()
			throws EntityNotFoundException, DatabaseConnectionException,
			QueryException {

		estimateFacade.getEstimatesByUser(null, 5);
	}

	@Test(expected = EntityNotFoundException.class)
	public void getEstimateByUserOrDivisionEntityException()
			throws EntityNotFoundException, DatabaseConnectionException,
			QueryException {

		estimateFacade.getEstimateByUserOrDivision(null, null);
	}

	@Test(expected = QueryException.class)
	public void getEstimateByUserOrDivisionaQueryException()
			throws EntityNotFoundException, DatabaseConnectionException,
			QueryException {

		estimateFacade.getEstimateByUserOrDivision(new User(), new Division(),
				-11);
	}

	@Test
	public void getAllEstimates() throws DatabaseConnectionException,
			QueryException {
		List<Estimate> result = estimateFacade.getAllEstimates();
		List<String> names = new ArrayList<String>();

		for (Estimate e : result)
			names.add(e.getName());

		// verify that result contains all six estimates.
		assertTrue(names.contains("Test_Estimate 1"));
		assertTrue(names.contains("Test_Estimate 2"));
		assertTrue(names.contains("Test_Estimate 3"));
		assertTrue(names.contains("Test_Estimate 4"));
		assertTrue(names.contains("Test_Estimate 5"));
		assertTrue(names.contains("Test_Estimate 6"));

		// verify that result does not contain any other estimates.
		assertEquals(names.size(), 6);
	}

	@Test
	public void getAllEstimatesLimit() throws DatabaseConnectionException,
			QueryException {
		List<Estimate> result = estimateFacade.getAllEstimates(3);

		List<String> names = new ArrayList<String>();

		for (Estimate e : result)
			names.add(e.getName());

		// verify that result contains the three most recently modified
		// estimates.
		assertTrue(names.contains("Test_Estimate 4"));
		assertTrue(names.contains("Test_Estimate 5"));
		assertTrue(names.contains("Test_Estimate 6"));

		// verify that result does not contain any other estimates.
		assertEquals(names.size(), 3);
	}

	@Test
	public void getAllEstimatesLimitZero() throws DatabaseConnectionException,
			QueryException {
		List<Estimate> result = estimateFacade.getAllEstimates(0);

		// verify that result is empty.
		assertEquals(result.size(), 0);
	}

	@Test
	public void getEstimatesByDivision() throws EntityNotFoundException,
			DatabaseConnectionException, QueryException {
		List<Estimate> result = estimateFacade
				.getEstimatesByDivision(division1);

		List<String> names = new ArrayList<String>();

		for (Estimate e : result)
			names.add(e.getName());

		// verify that result contains all estimates belonging to division
		// "TestDivision1".
		assertTrue(names.contains("Test_Estimate 1"));
		assertTrue(names.contains("Test_Estimate 2"));

		// verify that result does not contain any other estimates.
		assertEquals(names.size(), 2);
	}

	@Test
	public void getEstimatesByDivisionLimit() throws EntityNotFoundException,
			DatabaseConnectionException, QueryException {
		List<Estimate> result = estimateFacade.getEstimatesByDivision(
				division1, 1);

		List<String> names = new ArrayList<String>();

		for (Estimate e : result)
			names.add(e.getName());

		// verify that result contains the most recently modified estimate
		// belonging to division "TestDivision1".
		assertTrue(names.contains("Test_Estimate 2"));

		// verify that result does not contain any other estimates.
		assertEquals(names.size(), 1);
	}

	@Test
	public void getEstimatesByDivisionLimitZero()
			throws EntityNotFoundException, DatabaseConnectionException,
			QueryException {
		List<Estimate> result = estimateFacade.getEstimatesByDivision(
				division1, 0);

		// verify that result is empty.
		assertEquals(result.size(), 0);
	}

	@Test
	public void getEstimateByUser() throws EntityNotFoundException,
			DatabaseConnectionException, QueryException {
		List<Estimate> result = estimateFacade.getEstimatesByUser(user1);

		List<String> names = new ArrayList<String>();

		for (Estimate e : result)
			names.add(e.getName());

		// verify that result contains all estimates belonging to user "test1".
		assertTrue(names.contains("Test_Estimate 1"));
		assertTrue(names.contains("Test_Estimate 2"));
		assertTrue(names.contains("Test_Estimate 6"));

		// verify that result does not contain any other estimates.
		assertEquals(names.size(), 3);

	}

	@Test
	public void getEstimateByUserLimit() throws EntityNotFoundException,
			DatabaseConnectionException, QueryException {
		List<Estimate> result = estimateFacade.getEstimatesByUser(user1, 2);

		List<String> names = new ArrayList<String>();

		for (Estimate e : result)
			names.add(e.getName());

		// verify that result contains the two most recently modified estimates
		// belonging to user "test1".
		assertTrue(names.contains("Test_Estimate 2"));
		assertTrue(names.contains("Test_Estimate 6"));

		// verify that result does not contain any other estimates.
		assertEquals(names.size(), 2);
	}

	@Test
	public void getEstimateByUserLimitZero() throws EntityNotFoundException,
			DatabaseConnectionException, QueryException {
		List<Estimate> result = estimateFacade.getEstimatesByUser(user1, 0);

		assertEquals(result.size(), 0);
	}

	@Test
	public void getEstimateByUserOrDivision() throws EntityNotFoundException,
			DatabaseConnectionException, QueryException {
		List<Estimate> result = estimateFacade.getEstimateByUserOrDivision(
				user1, division3);

		List<String> names = new ArrayList<String>();

		for (Estimate e : result)
			names.add(e.getName());

		// verify that result contains all estimates belonging to user "test1"
		// or division "TestDivision3".
		assertTrue(names.contains("Test_Estimate 1"));
		assertTrue(names.contains("Test_Estimate 2"));
		assertTrue(names.contains("Test_Estimate 5"));
		assertTrue(names.contains("Test_Estimate 6"));

		// verify that result does not contain any other estimates.
		assertEquals(names.size(), 4);

	}

	@Test
	public void getEstimateByUserOrDivisionLimit()
			throws EntityNotFoundException, DatabaseConnectionException,
			QueryException {
		List<Estimate> result = estimateFacade.getEstimateByUserOrDivision(
				user1, division3, 2);

		List<String> names = new ArrayList<String>();

		for (Estimate e : result)
			names.add(e.getName());

		// verify that result contains the two most recently modified estimates
		// belonging to user "test1" or division "TestDivision2".
		assertTrue(names.contains("Test_Estimate 5"));
		assertTrue(names.contains("Test_Estimate 6"));

		// verify that result does not contain any other estimates.
		assertEquals(names.size(), 2);

	}

	@Test
	public void getEstimateByUserOrDivisionLimitZero()
			throws EntityNotFoundException, DatabaseConnectionException,
			QueryException {
		List<Estimate> result = estimateFacade.getEstimateByUserOrDivision(
				user1, division3, 0);

		// verify that result is empty.
		assertEquals(result.size(), 0);
	}

	@Test
	public void saveEstimate() throws EntityNotFoundException,
			DatabaseConnectionException, QueryException {
		// create a new Estimate-Object
		Estimate estimate = new Estimate();
		estimate.setEstimator(user2);
		estimate.setLastEditor(user2);
		estimate.setDivision(division3);
		estimate.setName("NEW_ESTIMATE");
		estimate.setCreationDate(Calendar.getInstance());
		estimate.setModifyDate(Calendar.getInstance());
		ProjectEnvironment environment = Cloner
				.cloneProjectEnvironment(estimateFacade.getAllEstimates()
						.get(0).getProjectEnvironment(), estimate);
		estimate.setProjectEnvironment(environment);
		estimateFacade.deleteEstimate(estimateFacade.getAllEstimates().get(0));

		// save Estimate
		estimateFacade.saveEstimate(estimate);

		// verify that estimate has been saved
		List<Estimate> result = estimateFacade.getEstimatesByUser(user2, 1);
		assertTrue(result.get(0).getName().equals("NEW_ESTIMATE"));
	}

	@Test
	public void deleteEstimate() throws QueryException,
			DatabaseConnectionException, EntityNotFoundException {
		// get estimate "TEST_ESTIMATE 6";
		List<Estimate> result = estimateFacade.getAllEstimates(2);
		Estimate estimate6 = result.get(0);

		// delete estimate "TEST_ESTIMATE 6";
		estimateFacade.deleteEstimate(estimate6);

		// get list of all estimates
		result = estimateFacade.getAllEstimates();

		List<String> names = new ArrayList<String>();

		for (Estimate e : result) {
			names.add(e.getName());
		}

		// verify that result does not contain "TEST_ESTIMATE 6"
		assertFalse(names.contains("TEST_ESTIMATE 6"));

		// verify that all other estimates are still existing
		assertEquals(5, result.size());
	}

	@AfterClass
	public static void cleanup() throws SQLException, ClassNotFoundException {
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
