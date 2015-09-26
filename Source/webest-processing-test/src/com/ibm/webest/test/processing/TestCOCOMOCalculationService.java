package com.ibm.webest.test.processing;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.sql.SQLException;

import javax.naming.NamingException;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ibm.webest.test.processing.tools.JdbcUtils;

import com.ibm.webest.processing.calculation.COCOMOCalcException;
import com.ibm.webest.processing.calculation.COCOMOCalculationServiceRemote;

/**
 * TestCOCOMOCalculationService: Checks COCOMO Calculation values to make sure that the 
 * implementation works correct 
 * 
 * @author David Dornseifer
 * 
 */
public class TestCOCOMOCalculationService {
	private static COCOMOCalculationServiceRemote cocomoCalc;

	@BeforeClass
	public static void beforeClass() throws FileNotFoundException,
			SQLException, ClassNotFoundException, NamingException {
		JdbcUtils.createUser();
		cocomoCalc = EjbUtils.getEjb(COCOMOCalculationServiceRemote.class,
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
	public void testCocomoCalculation() throws COCOMOCalcException {

		// testvalues calculated manually
		float sloc = 31250;
		float effort = 89.084f;
		float time = 10.52f;
		float delta = 1;

		// check if the calculated values are equal to the manually calculated
		// values
		assertEquals(effort, cocomoCalc.calc(sloc).getEffort().doubleValue(),
				delta);
		assertEquals(time, cocomoCalc.calc(sloc).getTime().doubleValue(), delta);

	}

	@Test(expected = COCOMOCalcException.class)
	public void testMathematicalLimits() throws COCOMOCalcException {

		// testvalues calculated manually
		float sloc = 0;
		double effort = 0;
		double time = 0;
		double delta = 1;

		assertEquals(effort, cocomoCalc.calc(sloc).getEffort().doubleValue(),
				delta);
		assertEquals(time, cocomoCalc.calc(sloc).getTime().doubleValue(), delta);
	}
}