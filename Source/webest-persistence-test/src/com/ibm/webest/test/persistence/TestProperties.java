package com.ibm.webest.test.persistence;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Provides common properties used by the JUnit tests.<br>
 * Specify the values in "test.properties" file.
 * @author Gregor Schumm
 *
 */
public class TestProperties {
	private static Properties props = new Properties();
	static {
		try {
			props.load(new FileInputStream(TestProperties.class.getResource("test.properties").getFile()));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * The JDBC connection string for the database connection.
	 * @return the value of property <code>jdbc.connectionString</code>
	 */
	public static String getJdbcConnectionString() {
		return props.getProperty("jdbc.connectionString");
	}
	
	/**
	 * The user name for the database connection.
	 * @return the value of property <code>jdbc.username</code>
	 */
	public static String getJdbcUsername() {
		return props.getProperty("jdbc.username");
	}
	
	/**
	 * The password for the database connection.
	 * @return the value of property <code>jdbc.password</code>
	 */
	public static String getJdbcPassword() {
		return props.getProperty("jdbc.password");
	}
	
	/**
	 * The WASCE security realm for application authentication.
	 * @return the value of property <code>ejb.securityRealm</code>
	 */
	public static String getEjbSecurityRealm() {
		return props.getProperty("ejb.securityRealm");
	}
	
	/**
	 * User name used to authenticate against a session bean. 
	 * @return the value of property <code>ejb.username</code>
	 */
	public static String getEjbUsername() {
		return props.getProperty("ejb.username");
	}
	
	/**
	 * Password used to authenticate against a session bean. 
	 * @return the value of property <code>ejb.password</code>
	 */
	public static String getEjbPassword() {
		return props.getProperty("ejb.password");
	}
	
	/**
	 * Role of the user that is used to authenticate against a session bean. 
	 * @return the value of property <code>ejb.role</code>
	 */
	public static String getEjbRole() {
		return props.getProperty("ejb.role");
	}
}
