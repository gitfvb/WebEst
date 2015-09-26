package com.ibm.webest.test.processing;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.sql.SQLException;

import javax.naming.NamingException;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ibm.webest.test.processing.tools.JdbcUtils;

import com.ibm.webest.persistence.service.DatabaseConnectionException;
import com.ibm.webest.persistence.service.EntityNotFoundException;
import com.ibm.webest.processing.calculation.PutnamCalcException;
import com.ibm.webest.processing.calculation.PutnamCalculationServiceRemote;
import com.ibm.webest.processing.model.COCOMOResult;

/**
 * TestPutnamCalculationService: Make sure the the putnam computes valid results
 * 
 * @author David Dornseifer
 *
 */

public class TestPutnamCalculationService {

	private static PutnamCalculationServiceRemote putnamCalc;

	@BeforeClass
	public static void beforeClass() throws FileNotFoundException,
			SQLException, ClassNotFoundException, NamingException {
		JdbcUtils.createUser();
		putnamCalc = EjbUtils.getEjb(PutnamCalculationServiceRemote.class,
				TestProperties.getEjbUsername(),
				TestProperties.getEjbPassword());
	}

	@AfterClass
	public static void cleanUpClass() throws FileNotFoundException,
			SQLException, ClassNotFoundException {
		CleanUp();
		JdbcUtils.executeScript("test_data_Szenario.sql");
	}

	@Before
	public void init() throws FileNotFoundException, SQLException,
			ClassNotFoundException, NamingException {
		CleanUp();
		JdbcUtils.executeScript("test_data_Szenario.sql");
	}

	private static void CleanUp() throws SQLException, ClassNotFoundException,
			FileNotFoundException {

		JdbcUtils.executeScript("del_test_data_Szenario.sql");
	}

	@Test
	public void testGetEValue() throws PutnamCalcException {
		float sloc = 31250;
		float expectedE = 0.34f;

		// load the e value in dependence to the sloc
		float e = putnamCalc.getE((int) sloc);
		float delta = 0.01f;
		assertEquals(expectedE, e, delta);
	}

	@Test
	public void testPutnamCalculationEffort() throws PutnamCalcException,
			EntityNotFoundException, DatabaseConnectionException {

		// manually calculated values
		float sloc = 31250;
		float cocomoEffort = 89.084f;
		float cocomoTime = 10.52f;
		float pp = 22219.025f;

		// expected values
		float putnamEffort = 1.60f;

		// COCOMOResult test object
		COCOMOResult cocomoResults = new COCOMOResult(cocomoEffort, cocomoTime);

		// temporary variables
		float delta;
		float result;

		// check if the putnam effort is right calculated
		delta = 0.1f;
		result = putnamCalc.calc(sloc, pp, cocomoResults).getEffort();
		assertEquals(putnamEffort, result, delta);
	}

	@Test
	public void testPutnamCalculationMBI() throws EntityNotFoundException,
			DatabaseConnectionException, PutnamCalcException {

		// manually calculated values
		float sloc = 31250;
		float cocomoEffort = 89.084f;
		float cocomoTime = 10.52f;
		float pp = 22219.025f;

		// expected values
		float mbi = 2.38f;

		// COCOMOResult test object
		COCOMOResult cocomoResults = new COCOMOResult(cocomoEffort, cocomoTime);

		// temporary variables
		float delta;
		float result;

		// check if the mbi level result is ok
		delta = 0.01f;
		result = putnamCalc.calc(sloc, pp, cocomoResults).getMBI();
		assertEquals(mbi, result, delta);

	}

	@Test
	public void testPutnamCalculationTime() throws EntityNotFoundException,
			DatabaseConnectionException, PutnamCalcException {

		// manually calculated values
		float sloc = 31250;
		float cocomoEffort = 89.084f;
		float cocomoTime = 10.52f;
		float pp = 22219.025f;

		// COCOMOResult test object
		COCOMOResult cocomoResults = new COCOMOResult(cocomoEffort, cocomoTime);

		// temporary variables
		float delta;
		float result;

		// check if the putnam time is set up in the right context
		delta = 0.0f;
		result = putnamCalc.calc(sloc, pp, cocomoResults).getTD();
		assertEquals(cocomoTime, result, delta);

	}

	@Test(expected = PutnamCalcException.class)
	public void testLowMathematicalLimit() throws PutnamCalcException,
			EntityNotFoundException, DatabaseConnectionException {

		// manually calculated values
		float sloc = 0.0f;
		float effort = 89.084f;
		float time = 10.52f;
		float pp = 22219.025f;

		// expected values
		float putnamEffort = 0.0f;

		// COCOMOResult test object
		COCOMOResult cocomoResults = new COCOMOResult(effort, time);

		// temporary variables
		float delta;
		float result;

		// test low mathematical limits
		delta = 0.1f;
		result = putnamCalc.calc(sloc, pp, cocomoResults).getEffort();
		assertEquals(putnamEffort, result, delta);

	}

	@Test(expected = PutnamCalcException.class)
	public void testDivisionByZero() throws EntityNotFoundException,
			DatabaseConnectionException, PutnamCalcException {

		// manually calculated values
		float sloc = 0.0f;
		float effort = 89.084f;
		float time = 0.0f;
		float pp = 0.0f;

		// COCOMOResult test object
		COCOMOResult cocomoResults = new COCOMOResult(effort, time);

		// check if the right exception is thrown
		putnamCalc.calc(sloc, pp, cocomoResults).getEffort();

	}

	@Test(expected = PutnamCalcException.class)
	public void testNullValues() throws EntityNotFoundException,
			DatabaseConnectionException, PutnamCalcException {

		// manually calculated values
		float sloc = 0;
		float pp = 3322.0f;

		// COCOMOResult test object
		COCOMOResult cocomoResults = null;
		putnamCalc.calc(sloc, pp, cocomoResults);

	}
}
