package com.ibm.webest.test.persistence;

import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

/**
 * JUnit Test for EstimateFacade
 * 
 * @author Wail Shakir
 * @author Gregor Schumm
 * @author Dirk Kohlweyer
 */
public class InsertTestData {

	@Before
	public void resetTestData() throws SQLException, ClassNotFoundException,
			FileNotFoundException {
		JdbcUtils.executeScript("test_data_Szenario.sql");
	}

	@Test
	public void testTheTest() throws SQLException, ClassNotFoundException {
		assertTrue(true);
	}

}
