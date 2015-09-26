package com.ibm.webest.processing.helpers;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * Utility class for providing default values for sample entities or init values.<br>
 * The values have to be defined in <code>default-values.properties</code> file.
 * @author Gregor Schumm
 *
 */
public final class DefaultValues {
	private static Configuration properties;
	
	private static void init() {
		if (properties!=null)
			return;
		try {
			properties = new PropertiesConfiguration(DefaultValues.class.getResource("default-values.properties"));
		} catch (ConfigurationException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Retrieves a default string value by its string key.
	 * @param key the key defined in <code>default-values.properties</code>
	 * @return the value matching the given key
	 */
	public static String getString(String key) {
		init();
		return properties.getString(key);			
	}
	
	/**
	 * Retrieves a default string array value by its string key.
	 * @param key the key defined in <code>default-values.properties</code>
	 * @return the value matching the given key
	 */
	public static String[] getStringArray(String key) {
		init();
		return properties.getStringArray(key);
	}
	
	/**
	 * Retrieves a default integer value by its string key.
	 * @param key the key defined in <code>default-values.properties</code>
	 * @return the value matching the given key
	 */
	public static int getInt(String key) {
		init();
		return properties.getInt(key);
	}
	
	/**
	 * Retrieves a default double value by its string key.
	 * @param key the key defined in <code>default-values.properties</code>
	 * @return the value matching the given key
	 */
	public static double getDouble(String key) {
		init();
		return properties.getDouble(key);
	}
	
	/**
	 * Retrieves a default boolean value by its string key.
	 * @param key the key defined in <code>default-values.properties</code>
	 * @return the value matching the given key
	 */
	public static boolean getBoolean(String key) {
		init();
		return properties.getBoolean(key);
	}
}
