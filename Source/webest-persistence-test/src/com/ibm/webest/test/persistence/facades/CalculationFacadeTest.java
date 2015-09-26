package com.ibm.webest.test.persistence.facades;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import com.ibm.webest.persistence.service.CalculationFacadeRemote;
import com.ibm.webest.persistence.service.DatabaseConnectionException;
import com.ibm.webest.persistence.service.EntityNotFoundException;
import com.ibm.webest.test.persistence.EjbUtils;
import com.ibm.webest.test.persistence.JdbcUtils;
import com.ibm.webest.test.persistence.TestProperties;

/**
 * JUnit Test for CalculationFacade
 * 
 * @author Gregor Schumm
 * @author Dirk Kohlweyer
 * @author Wail Shakir
 */
public class CalculationFacadeTest {
	private static CalculationFacadeRemote facade;
	private static final int[] pps = { 754, 987, 1220, 1597, 1974, 2584, 3194,
			4181, 5186, 6765, 8362, 10946, 13530, 17711, 21892, 28657, 35422,
			46368, 57314, 75025, 92736, 121393, 150050, 196418 };

	// all KEYs from table CALCULATIONPARAMETER
	private static final String[] keys = { "cocomo_complexity_level_simple",
			"cocomo_complexity_level_moderate",
			"cocomo_complexity_level_embedded", "cocomo_a_simple",
			"cocomo_a_moderate", "cocomo_a_embedded", "cocomo_b_simple",
			"cocomo_b_moderate", "cocomo_b_embedded", "cocomo_c_simple",
			"cocomo_c_moderate", "cocomo_c_embedded", "cocomo_d_simple",
			"cocomo_d_moderate", "cocomo_d_embedded", "cocomo_e_5000",
			"cocomo_e_20000", "cocomo_e_30000", "cocomo_e_40000",
			"cocomo_e_50000", "cocomo_e_>50000", "cocomo_sloc_5000",
			"cocomo_sloc_20000", "cocomo_sloc_30000", "cocomo_sloc_40000",
			"cocomo_sloc_50000", "cocomo_sloc_>50000", "max_duration",
			"min_duration_factor", "weeks_per_month" };

	// all VALUEs from table CALCULATIONPARAMETER
	private static final double[] values = { 0.0, 1.0, 2.0, 2.4, 3.0, 3.6,
			1.05, 1.12, 1.2, 2.5, 2.5, 2.5, 0.32, 0.35, 0.38, 0.16, 0.18, 0.28,
			0.34, 0.37, 0.39, 0.0, 1.0, 2.0, 3.0, 4.0, 5.0, 3.0, 0.75,
			4.3333333333333333333333 };

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		JdbcUtils.createUser();
		facade = EjbUtils.getEjb(CalculationFacadeRemote.class,
				TestProperties.getEjbUsername(),
				TestProperties.getEjbPassword());
	}

	@Test
	public void testGetPpByPi() throws EntityNotFoundException,
			DatabaseConnectionException {
		for (int i = 1; i <= pps.length; i++) {
			assertEquals(pps[i - 1], facade.getPpByPi(i));
		}
	}

	@Test(expected = EntityNotFoundException.class)
	public void testGetPpByPiException() throws EntityNotFoundException,
			DatabaseConnectionException {
		facade.getPpByPi(100);
	}

	@Test(expected = EntityNotFoundException.class)
	public void testGetNonExistingCalculationParameter()
			throws EntityNotFoundException, DatabaseConnectionException {
		facade.getCalculationParameter("Blub");
	}

	@Test
	public void testGetCalculationParameter() throws EntityNotFoundException,
			DatabaseConnectionException {
		for (int i = 0; i < keys.length; i++) {
			assertEquals(keys[i], facade.getCalculationParameter(keys[i]),
					values[i], 0.01);
		}
	}

	@Test
	public void testGetCalculationParameters()
			throws DatabaseConnectionException {
		Map<String, Double> params = null;

		params = facade.getCalculationParameters();

		assertEquals(params.size(), keys.length);

		for (int i = 0; i < keys.length; i++) {
			assertEquals(params.get(keys[i]), values[i], 0.01);
		}
	}
}
