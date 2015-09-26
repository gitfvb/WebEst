package com.ibm.webest.test.persistence.facades;

import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.sql.SQLException;

import javax.naming.NamingException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ibm.webest.persistence.model.User;
import com.ibm.webest.persistence.service.DatabaseConnectionException;
import com.ibm.webest.persistence.service.EntityNotFoundException;
import com.ibm.webest.persistence.service.UserFacadeRemote;
import com.ibm.webest.test.persistence.EjbUtils;
import com.ibm.webest.test.persistence.JdbcUtils;
import com.ibm.webest.test.persistence.TestProperties;

/**
 * JUnit Test for UserFacade
 * 
 * @author Dirk Kohlweyer
 */

public class UserFacadeTest {
	private static UserFacadeRemote userFacade;
	
	@BeforeClass
	public static void init() throws NamingException, FileNotFoundException, SQLException, ClassNotFoundException {
		JdbcUtils.createUser();
		userFacade = EjbUtils.getEjb(UserFacadeRemote.class, TestProperties.getEjbUsername(), TestProperties.getEjbPassword());
		JdbcUtils.executeScript("test_data_User.sql");
	}
	
	@Test
	public void findUser() throws EntityNotFoundException, DatabaseConnectionException{
		//trying to find User 'dk' Dirk Kohlweyer
		User user = userFacade.getUserById("dk");
		assertTrue("first name", user.getFirstName().equals("Dirk"));
		assertTrue("last name", user.getLastName().equals("Kohlweyer"));
	}
	
	@Test
	public void findUser2() throws EntityNotFoundException, DatabaseConnectionException{
		//trying to find User 'proemmel' Hans Jürgen Prömmel
		User user = userFacade.getUserById("proemmel");
		assertTrue("first name", user.getFirstName().equals("Hans Jürgen"));
		assertTrue("last name", user.getLastName().equals("Prömmel"));
	}
	
	@Test(expected = EntityNotFoundException.class)
	public void findNonExistingUser() throws EntityNotFoundException, DatabaseConnectionException {
		//trying to find non existing User 'abc'
		userFacade.getUserById("abc");
	}

	@AfterClass
	public static void cleanup() throws SQLException, ClassNotFoundException {
		JdbcUtils.getJdbcConnection().createStatement().execute("DELETE FROM \"USER\" WHERE id <> 'junit'");
	}
}
