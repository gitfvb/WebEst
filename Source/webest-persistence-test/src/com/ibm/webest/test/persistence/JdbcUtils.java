package com.ibm.webest.test.persistence;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public final class JdbcUtils {
	private static final String HEXES = "0123456789ABCDEF";

	public static Connection getJdbcConnection() throws SQLException,
			ClassNotFoundException {
		Class.forName("org.apache.derby.jdbc.ClientDriver");
		return DriverManager.getConnection(
				TestProperties.getJdbcConnectionString(),
				TestProperties.getJdbcUsername(),
				TestProperties.getJdbcPassword());
	}

	public static void executeScript(String name) throws FileNotFoundException,
			SQLException, ClassNotFoundException {
		Connection connection = getJdbcConnection();
		JdbcScriptRunner scriptRunner = new JdbcScriptRunner(connection);
		scriptRunner.runScript(new FileReader(JdbcUtils.class.getResource(name)
				.getFile()));
		connection.commit();
	}

	public static void createUser(String id, String pwHash, String fName,
			String lName, String role) throws SQLException,
			ClassNotFoundException {
		String deleteSql = "DELETE FROM \"USER\" WHERE id = '" + id + "'";
		String insertSql = "INSERT INTO \"USER\" (id, password, firstName, lastName, role) VALUES ('"
				+ id
				+ "', '"
				+ pwHash
				+ "', '"
				+ fName
				+ "', '"
				+ lName
				+ "', '" + role + "')";
		Statement statement = getJdbcConnection().createStatement();
		statement.execute(deleteSql);
		statement.execute(insertSql);
		statement.close();
	}

	public static void createUser() throws SQLException, ClassNotFoundException {
		createUser(TestProperties.getEjbUsername(),
				md5(TestProperties.getEjbPassword()),
				TestProperties.getEjbUsername(), TestProperties.getEjbRole(),
				TestProperties.getEjbRole());
	}

	public static String md5(String in) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return new String(getHex(md5.digest(in.getBytes())));
	}

	public static String getHex(byte[] raw) {
		if (raw == null) {
			return null;
		}
		final StringBuilder hex = new StringBuilder(2 * raw.length);
		for (final byte b : raw) {
			hex.append(HEXES.charAt((b & 0xF0) >> 4)).append(
					HEXES.charAt((b & 0x0F)));
		}
		return hex.toString();
	}
}
