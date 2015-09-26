package com.ibm.webest.test.processing;

import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ibm.webest.persistence.model.Constraint;
import com.ibm.webest.persistence.model.Solution;
import com.ibm.webest.persistence.service.DatabaseConnectionException;
import com.ibm.webest.persistence.service.EntityNotFoundException;
import com.ibm.webest.processing.calculation.COCOMOCalcException;
import com.ibm.webest.processing.calculation.COCOMOCalculationServiceRemote;
import com.ibm.webest.processing.calculation.ConstraintsException;
import com.ibm.webest.processing.calculation.ConstraintsServiceRemote;
import com.ibm.webest.processing.calculation.PutnamCalcException;
import com.ibm.webest.processing.calculation.PutnamCalculationServiceRemote;
import com.ibm.webest.processing.helpers.DefaultValues;
import com.ibm.webest.processing.model.COCOMOResult;
import com.ibm.webest.processing.model.EstimationResult;
import com.ibm.webest.processing.model.PutnamResult;
import com.ibm.webest.test.processing.tools.JdbcUtils;

/**
 * Testcases for ConstraintsService
 * @author Andre Munzinger
 */
public class TestConstraintsService {

	private static ConstraintsServiceRemote constraintService;
	private static COCOMOCalculationServiceRemote cocomoCalc;
	private static PutnamCalculationServiceRemote putnamCalc;

	@BeforeClass
	public static void beforeClass() throws FileNotFoundException,
			SQLException, ClassNotFoundException, NamingException {

		JdbcUtils.createUser();

		constraintService = EjbUtils.getEjb(ConstraintsServiceRemote.class,
				TestProperties.getEjbUsername(),
				TestProperties.getEjbPassword());

		cocomoCalc = EjbUtils.getEjb(COCOMOCalculationServiceRemote.class,
				TestProperties.getEjbUsername(),
				TestProperties.getEjbPassword());

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

	private static void CleanUp() throws SQLException, ClassNotFoundException,
			FileNotFoundException {

		JdbcUtils.executeScript("del_test_data_Szenario.sql");
	}

	@Test(expected = ConstraintsException.class)
	public void testCheckResult() throws ConstraintsException {
		constraintService.checkResult(null);
	}

	@Test
	public void testCheckMinRequirements() throws ConstraintsException,
			COCOMOCalcException, EntityNotFoundException,
			DatabaseConnectionException, PutnamCalcException {

		// manually chosen values
		float sloc = 31250;
		float pp = 22215.03f;
		float target;

		// add infrastructure components to test case
		List<Constraint> conList = new ArrayList<Constraint>();
		Constraint constraints = new Constraint();

		// generate new COCOMOResult
		COCOMOResult cResult = cocomoCalc.calc(sloc);

		// set up project duration target
		target = 0.8f * cResult.getTime();

		// generate new Putnam Result
		PutnamResult putResult = putnamCalc.calc(sloc, pp, cResult);

		constraints.setTarget(target);
		constraints
				.setType(DefaultValues.getString("duration_constraint_type"));
		conList.add(constraints);

		// add new solution
		Solution sol = new Solution();
		sol.setConstraints(conList);

		// add new estimation as object store
		EstimationResult estResult = new EstimationResult();
		estResult.setPutnamResult(putResult);
		estResult.setSolution(sol);

		assertTrue(constraintService.checkResult(estResult));
	}

	@Test(expected = ConstraintsException.class)
	public void testCheckMinRequirementsException() throws COCOMOCalcException,
			EntityNotFoundException, DatabaseConnectionException,
			PutnamCalcException, ConstraintsException {

		// manually chosen values
		float sloc = 31250;
		float pp = 22215.03f;
		float target;

		// add infrastructure components to test case
		List<Constraint> conList = new ArrayList<Constraint>();
		Constraint constraints = new Constraint();

		// generate new COCOMOResult
		COCOMOResult cResult = cocomoCalc.calc(sloc);

		// set up project duration target
		target = 0.3f * cResult.getTime();

		// generate new Putnam Result
		PutnamResult putResult = putnamCalc.calc(sloc, pp, cResult);

		constraints.setTarget(target);
		constraints
				.setType(DefaultValues.getString("duration_constraint_type"));
		conList.add(constraints);

		// add new solution
		Solution sol = new Solution();
		sol.setConstraints(conList);

		// add new estimation as object store
		EstimationResult estResult = new EstimationResult();
		estResult.setPutnamResult(putResult);
		estResult.setSolution(sol);

		constraintService.checkResult(estResult);

	}

	@Test(expected = ConstraintsException.class)
	public void testCheckMaxRequirementsTDException()
			throws COCOMOCalcException, EntityNotFoundException,
			DatabaseConnectionException, PutnamCalcException,
			ConstraintsException {

		// manually chosen values
		float sloc = 6000000;
		float pp = 5000.03f;
		float target;

		// add infrastructure components to test case
		List<Constraint> conList = new ArrayList<Constraint>();
		Constraint constraints = new Constraint();

		// generate new COCOMOResult
		COCOMOResult cResult = cocomoCalc.calc(sloc);

		// set up project duration target
		target = 0.8f * cResult.getTime();

		// generate new Putnam Result
		PutnamResult putResult = putnamCalc.calc(sloc, pp, cResult);

		constraints.setTarget(target);
		constraints
				.setType(DefaultValues.getString("duration_constraint_type"));
		conList.add(constraints);

		// add new solution
		Solution sol = new Solution();
		sol.setConstraints(conList);

		// add new estimation as object store
		EstimationResult estResult = new EstimationResult();
		estResult.setPutnamResult(putResult);
		estResult.setSolution(sol);

		constraintService.checkResult(estResult);
	}

	@Test(expected = ConstraintsException.class)
	public void testCheckMaxRequirementsWTException()
			throws ConstraintsException, EntityNotFoundException,
			DatabaseConnectionException, PutnamCalcException,
			COCOMOCalcException {

		// manually chosen values
		float sloc = 60000;
		float pp = 5000.03f;
		float target;

		// add infrastructure components to test case
		List<Constraint> conList = new ArrayList<Constraint>();
		Constraint constraints = new Constraint();

		// generate new COCOMOResult
		COCOMOResult cResult = cocomoCalc.calc(sloc);

		// set up project duration target
		target = 48;

		// generate new Putnam Result
		PutnamResult putResult = putnamCalc.calc(sloc, pp, cResult);

		constraints.setTarget(target);
		constraints
				.setType(DefaultValues.getString("duration_constraint_type"));
		conList.add(constraints);

		// add new solution
		Solution sol = new Solution();
		sol.setConstraints(conList);

		// add new estimation as object store
		EstimationResult estResult = new EstimationResult();
		estResult.setPutnamResult(putResult);
		estResult.setSolution(sol);

		constraintService.checkResult(estResult);
	}

	@Test
	public void testCheckMaxRequirementsTD() throws EntityNotFoundException,
			DatabaseConnectionException, PutnamCalcException,
			COCOMOCalcException, ConstraintsException {

		// manually chosen values
		float sloc = 1200000;
		float pp = 5000.03f;
		float target;

		// add infrastructure components to test case
		List<Constraint> conList = new ArrayList<Constraint>();
		Constraint constraints = new Constraint();

		// generate new COCOMOResult
		COCOMOResult cResult = cocomoCalc.calc(sloc);

		// set up project duration target
		target = 0.8f * cResult.getTime();

		// generate new Putnam Result
		PutnamResult putResult = putnamCalc.calc(sloc, pp, cResult);

		constraints.setTarget(target);
		constraints
				.setType(DefaultValues.getString("duration_constraint_type"));
		conList.add(constraints);

		// add new solution
		Solution sol = new Solution();
		sol.setConstraints(conList);

		// add new estimation as object store
		EstimationResult estResult = new EstimationResult();
		estResult.setPutnamResult(putResult);
		estResult.setSolution(sol);

		assertTrue(constraintService.checkResult(estResult));

	}

	@Test
	public void testCheckMaxRequirementsWT() throws COCOMOCalcException,
			EntityNotFoundException, DatabaseConnectionException,
			PutnamCalcException, ConstraintsException {

		// manually chosen values
		float sloc = 400000;
		float pp = 5000.03f;
		float target;

		// add infrastructure components to test case
		List<Constraint> conList = new ArrayList<Constraint>();
		Constraint constraints = new Constraint();

		// generate new COCOMOResult
		COCOMOResult cResult = cocomoCalc.calc(sloc);

		// set up project duration target
		target = 35.9f;

		// generate new Putnam Result
		PutnamResult putResult = putnamCalc.calc(sloc, pp, cResult);

		constraints.setTarget(target);
		constraints
				.setType(DefaultValues.getString("duration_constraint_type"));
		conList.add(constraints);

		// add new solution
		Solution sol = new Solution();
		sol.setConstraints(conList);

		// add new estimation as object store
		EstimationResult estResult = new EstimationResult();
		estResult.setPutnamResult(putResult);
		estResult.setSolution(sol);

		assertTrue(constraintService.checkResult(estResult));
	}

}
