package com.ibm.webest.test.processing;

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
	
	public static String getJdbcConnectionString() {
		return props.getProperty("jdbc.connectionString");
	}
	public static String getJdbcUsername() {
		return props.getProperty("jdbc.username");
	}
	public static String getJdbcPassword() {
		return props.getProperty("jdbc.password");
	}
	public static String getEjbSecurityRealm() {
		return props.getProperty("ejb.securityRealm");
	}
	public static String getEjbUsername() {
		return props.getProperty("ejb.username");
	}
	public static String getEjbPassword() {
		return props.getProperty("ejb.password");
	}
	public static String getEjbRole() {
		return props.getProperty("ejb.role");
	}
}
