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
import com.ibm.webest.processing.calculation.COCOMOCalcException;
import com.ibm.webest.processing.calculation.COCOMOCalculationServiceRemote;
import com.ibm.webest.processing.calculation.PutnamCalcException;
import com.ibm.webest.processing.calculation.PutnamCalculationServiceRemote;
import com.ibm.webest.processing.calculation.RayleighCalculationException;
import com.ibm.webest.processing.calculation.RayleighCalculationServiceRemote;
import com.ibm.webest.processing.model.COCOMOResult;
import com.ibm.webest.processing.model.PutnamResult;
import com.ibm.webest.processing.model.RayleighResult;

/**
 * TestRayeighCalculation: Makes sure, that the rayleigh model implementation computes 
 * vaild values 
 * 
 * @author David Dornseifer
 *
 */

public class TestRayleighCalculationService {

	private static COCOMOCalculationServiceRemote cocomoCalc;
	private static PutnamCalculationServiceRemote putnamCalc;
	private static RayleighCalculationServiceRemote rayleighCalc;

	@BeforeClass
	public static void beforeClass() throws FileNotFoundException,
			SQLException, ClassNotFoundException, NamingException {

		JdbcUtils.createUser();
		cocomoCalc = EjbUtils.getEjb(COCOMOCalculationServiceRemote.class,
				TestProperties.getEjbUsername(),
				TestProperties.getEjbPassword());
		putnamCalc = EjbUtils.getEjb(PutnamCalculationServiceRemote.class,
				TestProperties.getEjbUsername(),
				TestProperties.getEjbPassword());
		rayleighCalc = EjbUtils.getEjb(RayleighCalculationServiceRemote.class,
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
	public void testRayleighCalculationValues1()
			throws RayleighCalculationException, EntityNotFoundException,
			DatabaseConnectionException, PutnamCalcException,
			COCOMOCalcException {

		// manually calculated values
		float sloc = 31250;
		float pp = 22219.025f;
		float delta = 0.1f;

		// manually calculated test values
		float maxVal = 13.29f;
		int listNr1 = 10;

		float testVal = 1.07f;
		int listNr2 = 29;

		// Instantiation of all needed calculation functions
		COCOMOResult cocomoTmp = cocomoCalc.calc(sloc);
		PutnamResult putnamTmp = putnamCalc.calc(sloc, pp, cocomoTmp);

		// calc Rayleigh values
		RayleighResult result = rayleighCalc.calc(putnamTmp.getEffort(),
				cocomoTmp.getTime());

		// check if calculated values are in range
		assertEquals(maxVal, result.getyValues().get(listNr1).get(0), delta);
		assertEquals(testVal, result.getyValues().get(listNr2).get(0), delta);

	}

	@Test
	public void testRayleighCalculationValues2() throws COCOMOCalcException,
			EntityNotFoundException, DatabaseConnectionException,
			PutnamCalcException, RayleighCalculationException {

		// manually calculated values
		float sloc = 50000;
		float pp = 22219.025f;
		float delta = 0.1f;

		// manually calculated test values
		float maxVal = 28.39f;
		int listNr1 = 11;

		// Instantiation of all needed calculation functions
		COCOMOResult cocomoTmp = cocomoCalc.calc(sloc);
		PutnamResult putnamTmp = putnamCalc.calc(sloc, pp, cocomoTmp);

		// calc Rayleigh values
		RayleighResult result = rayleighCalc.calc(putnamTmp.getEffort(),
				cocomoTmp.getTime());

		// check if calculated values are in range
		assertEquals(maxVal, result.getyValues().get(listNr1).get(0), delta);

	}

	@Test
	public void testRayleighCalculationMinValues() throws COCOMOCalcException,
			EntityNotFoundException, DatabaseConnectionException,
			PutnamCalcException, RayleighCalculationException {

		// manually calculated values
		float sloc = 125;
		float pp = 764;
		float delta = 0.1f;

		// expected values
		float maxVal = 100.85f;
		int listNr1 = 1;

		// Instantiation of all needed calculation functions
		COCOMOResult cocomoTmp = cocomoCalc.calc(sloc);
		PutnamResult putnamTmp = putnamCalc.calc(sloc, pp, cocomoTmp);

		// calc Rayleigh values
		RayleighResult result = rayleighCalc.calc(putnamTmp.getEffort(),
				cocomoTmp.getTime());

		// check if the values are like expected
		assertEquals(maxVal, result.getyValues().get(listNr1).get(0), delta);

	}

	@Test(expected = RayleighCalculationException.class)
	public void testRayleighCalculationZero() throws COCOMOCalcException,
			EntityNotFoundException, DatabaseConnectionException,
			PutnamCalcException, RayleighCalculationException {

		// manually set up test values
		float effort = 0;
		float td = 0;

		// check if right exception is thrown
		rayleighCalc.calc(effort, td);
	}

}
