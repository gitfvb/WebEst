package com.ibm.webest.gui.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Utility class to create compatible data for JSF select boxes.
 * 
 * @author Gregor Schumm
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public final class SelectBoxUtils {
	private static final Object[] EMPTY_OBJ_ARRAY = new Object[0];
	private static final Class[] EMPTY_CLASS_ARRAY = new Class[0];

	/**
	 * Takes the given list of entity object and creates a map that contains the
	 * invocation of the given methods as string in the given format.
	 * 
	 * @param list
	 *            the list containing the entities
	 * @param format
	 *            the format for the label string (see String.format method for
	 *            the appropriate format e.g. String.format("%s, %s",
	 *            user.getLastName(), user.getFirstName()) for
	 *            "Lastname, Firstname")
	 * @param labelProperties
	 *            the methods to invoke on the given entity object to retrieve
	 *            the values
	 * @return a map with the label as key and the entity object as value
	 */
	public static <T> Map<String, T> createSelectItemsMap(Collection<T> list,
			String format, Method... labelProperties) {
		Map<String, T> map = new LinkedHashMap<String, T>(list.size());
		for (T obj : list) {
			String label;
			label = createLabelString(obj, format, labelProperties);
			map.put(label, obj);
		}
		return map;
	}

	/**
	 * Creates the label string for the given object by invoking the given
	 * methods and applying the format string.
	 * 
	 * @param obj
	 *            the entity object
	 * @param format
	 *            the format string (as uses in String.format)
	 * @param methods
	 *            the methods to invoke to retrieve the values from the object
	 * @return the label string in the given format
	 */
	private static <T> String createLabelString(T obj, String format,
			Method[] methods) {
		try {
			Object[] params = new String[methods.length];
			for (int i = 0; i < params.length; i++) {
				params[i] = methods[i].invoke(obj, EMPTY_OBJ_ARRAY).toString();
			}
			if (format == null)
				format = "%s";
			return String.format(format, params);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Uses reflection to get the Method objects for the given method or property names.
	 * @param type
	 *            the type which owns the methods
	 * @param methodNames
	 *            the names of the methods to retrieve
	 * @return getter methods of the given type for the given method or property names
	 */
	private static Method[] getMethods(Class type, String[] methodNames) {
		try {
			Method[] methods = new Method[methodNames.length];
			for (int i = 0; i < methods.length; i++) {
				String methodName = methodNames[i];
				// append 'get' if only the property name was given
				if (!methodName.startsWith("get"))
					methodName = String.format("get%s%s",
							Character.toUpperCase(methodName.charAt(0)),
							methodName.substring(1));
				methods[i] = type.getMethod(methodName, EMPTY_CLASS_ARRAY);
			}
			return methods;
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Takes the given list of entity object and creates a map that contains the
	 * invocation of the given methods as string in the given format.
	 * 
	 * @param list
	 *            the list containing the entities
	 * @param type
	 *            the type of the entity object in the list
	 * @param format
	 *            the format for the label string (see String.format method for
	 *            the appropriate format e.g. String.format("%s, %s",
	 *            user.getLastName(), user.getFirstName()) for
	 *            "Lastname, Firstname")
	 * @param labelProperties
	 *            the names of the methods to invoke on the given entity object
	 *            to retrieve the values
	 * @return a map with the label as key and the entity object as value
	 */
	public static <T> Map<String, T> createSelectItemsMap(Collection<T> list,
			Class<T> type, String format, String... labelProperties) {

		return createSelectItemsMap(list, format,
				getMethods(type, labelProperties));
	}
}
