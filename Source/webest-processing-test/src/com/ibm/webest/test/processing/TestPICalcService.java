package com.ibm.webest.test.processing;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.sql.SQLException;

import javax.naming.NamingException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ibm.webest.test.processing.tools.JdbcUtils;

import com.ibm.webest.processing.calculation.PICalculatorException;
import com.ibm.webest.processing.calculation.PICalculatorServiceRemote;

/**
 * testPICalculationServic: Make sure, the the pi/pp lookup works correctly
 * 
 * @author David Dornseifer
 *
 */
public class TestPICalcService {

	private static PICalculatorServiceRemote piCalcService;

	@BeforeClass
	public static void beforeClass() throws FileNotFoundException,
			SQLException, ClassNotFoundException, NamingException {

		JdbcUtils.createUser();

		piCalcService = EjbUtils.getEjb(PICalculatorServiceRemote.class,
				TestProperties.getEjbUsername(),
				TestProperties.getEjbPassword());
	}

	@AfterClass
	public static void cleanUpClass() throws FileNotFoundException,
			SQLException, ClassNotFoundException {
		CleanUp();
		JdbcUtils.executeScript("test_data_Szenario.sql");
	}

	//clean up the test data files from db
	private static void CleanUp() throws SQLException, ClassNotFoundException,
			FileNotFoundException {

		JdbcUtils.executeScript("del_test_data_Szenario.sql");
	}

	@Test(expected = PICalculatorException.class)
	public void testcalculatePPException() throws PICalculatorException {
		//zero pi does not exist 
		piCalcService.calculatePP(0);
	}

	@Test(expected = PICalculatorException.class)
	public void testcalculatePPException2() throws PICalculatorException {
		//there is no negative pi
		piCalcService.calculatePP(-12);
	}

	@Test(expected = PICalculatorException.class)
	public void testcalculatePPException3() throws PICalculatorException {
		//the actual pi range is 1-24
		piCalcService.calculatePP(99999);
	}

	@Test
	public void testcalculatePP() throws PICalculatorException {
		//check valid lookup
		assertEquals(21892, piCalcService.calculatePP(15));
	}

}
