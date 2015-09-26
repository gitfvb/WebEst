package com.ibm.webest.gui.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.validation.ConstraintViolation;
import javax.validation.Path.Node;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

/**
 * Utility class to add various faces messages.
 * 
 * @author Gregor Schumm
 * 
 */
public final class MessageUtils {
	private static Configuration fieldMappings;
	private static Logger log = Logger.getLogger(MessageUtils.class);

	/**
	 * Adds a global info message to the current context.
	 * 
	 * @param msg
	 *            the message text
	 */
	public static void addInfoMessage(String msg) {
		addMessage(FacesMessage.SEVERITY_INFO, msg);
	}

	/**
	 * Adds a global warning message to the current context.
	 * 
	 * @param msg
	 *            the message text
	 */
	public static void addWarnMessage(String msg) {
		addMessage(FacesMessage.SEVERITY_WARN, msg);
	}

	/**
	 * Adds a global error message to the current context.
	 * 
	 * @param msg
	 *            the message text
	 */
	public static void addErrorMessage(String msg) {
		addMessage(FacesMessage.SEVERITY_ERROR, msg);
	}

	/**
	 * Returns a info message.
	 * 
	 * @param msg
	 *            the message text
	 */
	public static FacesMessage getInfoMessage(String msg) {
		return getMessage(FacesMessage.SEVERITY_INFO, msg);
	}

	/**
	 * Returns a warn message.
	 * 
	 * @param msg
	 *            the message text
	 */
	public static FacesMessage getWarnMessage(String msg) {
		return getMessage(FacesMessage.SEVERITY_WARN, msg);
	}

	/**
	 * Returns a warn message.
	 * 
	 * @param msg
	 *            the message text
	 */
	public static FacesMessage getErrorMessage(String msg) {
		return getMessage(FacesMessage.SEVERITY_ERROR, msg);
	}

	/**
	 * Returns a message.
	 * 
	 * @param severity
	 *            the severity of the message
	 * @param msg
	 *            the message text
	 */
	public static FacesMessage getMessage(Severity severity, String msg) {
		return new FacesMessage(severity, msg, msg);
	}

	/**
	 * Adds a field specific info message to the current context.
	 * 
	 * @param field
	 *            the field to add the message to or null for a global message
	 * @param msg
	 *            the message text
	 */
	public static void addInfoMessage(String field, String msg) {
		addMessage(FacesMessage.SEVERITY_INFO, field, msg);
	}

	/**
	 * Adds a field specific warning message to the current context.
	 * 
	 * @param field
	 *            the field to add the message to or null for a global message
	 * @param msg
	 *            the message text
	 */
	public static void addWarnMessage(String field, String msg) {
		addMessage(FacesMessage.SEVERITY_WARN, field, msg);
	}

	/**
	 * Adds a field specific error message to the current context.
	 * 
	 * @param field
	 *            the field to add the message to or null for a global message
	 * @param msg
	 *            the message text
	 */
	public static void addErrorMessage(String field, String msg) {
		addMessage(FacesMessage.SEVERITY_ERROR, field, msg);
	}

	/**
	 * Adds a message to the current context.
	 * 
	 * @param s
	 *            the severity of the message
	 * @param msg
	 *            the message text
	 */
	public static void addMessage(Severity s, String msg) {
		addMessage(s, null, msg);
	}

	/**
	 * Adds a field specific message to the current context.
	 * 
	 * @param s
	 *            the severity of the message
	 * @param field
	 *            the field to add the message to or null for a global message
	 * @param msg
	 *            the message text
	 */
	public static void addMessage(Severity s, String field, String msg) {
		addMessage(field, new FacesMessage(s, msg, msg));
	}

	/**
	 * Adds a field specific message to the current context.
	 * 
	 * @param field
	 *            the field to add the message to or null for a global message
	 * @param msg
	 *            the faces message to add
	 */
	public static void addMessage(String field, FacesMessage msg) {
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(field, msg);
	}

	/**
	 * Adds a list of global messages to the current context.
	 * 
	 * @param messages
	 *            the messages to add
	 */
	public static void addMessages(List<FacesMessage> messages) {
		for (FacesMessage msg : messages)
			addMessage(null, msg);
	}

	/**
	 * Adds a message to the faces context. The field name and the message are
	 * fetched from the given constraint violation.<br>
	 * Class properties of T have to be mapped to the JSF field IDs in
	 * <code>field-mappings.properties</code>.
	 * 
	 * @param <T>
	 *            the type of validated object
	 * @param violation
	 *            the constraint violation
	 */
	public static <T> void addValidationMessage(ConstraintViolation<T> violation) {
		// get the path to the affected property
		PropertyPath<T> propertyPath = new PropertyPath<T>(violation);
		String[] field = getFieldName(propertyPath.getPathString());
		String msg = violation.getMessage();
		String fieldName = null;
		if (field != null) {
			fieldName = propertyPath.resolveFieldName(field[0]);
			msg = field[1] + ": " + msg;
		}
		addErrorMessage(fieldName, msg);
	}

	/**
	 * Gets field name and label of a field defined in 'field-mappings.properties'.
	 * @param pathString the path to the entitiy field
	 * @return two element string array with { &lt;id of the JSF field&gt;, &lt;label of the field&gt; }
	 */
	private static String[] getFieldName(String pathString) {
		if (fieldMappings == null) {
			// read properties file
			try {
				fieldMappings = new PropertiesConfiguration(
						MessageUtils.class
								.getResource("field-mappings.properties"));
			} catch (ConfigurationException e) {
				log.error("Field mappings could not be initialized.", e);
				return null;
			}
		}
		String fieldName[] = null;
		if (fieldMappings.containsKey(pathString)) {
			// if field is defined in properties file return the specified values
			String[] mapping = fieldMappings.getStringArray(pathString);
			if (mapping.length > 0) {
				if (mapping.length > 1) {
					fieldName = mapping;
				} else {
					fieldName = new String[] { mapping[0], mapping[0] };
				}
			}
		}
		return fieldName;
	}

	/**
	 * Data structure representing a path to the property in an entity.<br>
	 * Used to find affected properties after a validation constraint violation was detected.
	 * @author Gregor Schumm
	 *
	 * @param <T> the type of the property which caused the violation
	 */
	private static class PropertyPath<T> {
		private ConstraintViolation<T> violation;
		private final Map<Character, Integer> indices = new HashMap<Character, Integer>();
		private String pathString;

		/**
		 * Creates the property path with the property contained in the given violation.
		 * @param violation
		 */
		public PropertyPath(ConstraintViolation<T> violation) {
			super();
			this.violation = violation;
			createPropertyPath();
		}

		/**
		 * Creates a string representation of the property path.
		 */
		private void createPropertyPath() {
			// get class name of root entity
			String prefix = violation.getRootBean().getClass().getSimpleName();
			StringBuffer propertyPathKey = new StringBuffer(prefix);
			indices.clear();
			// first level iterator replacement char is i
			char indexVar = 'i';
			for (Node node : violation.getPropertyPath()) {
				// create the path string separated by .
				propertyPathKey.append('.');
				propertyPathKey.append(node.getName());
				
				// check if we are in a list, then insert iterator replacement char
				if (node.getIndex() != null) {
					propertyPathKey.append("[{");
					propertyPathKey.append(indexVar);
					propertyPathKey.append("}]");
					// save index replacement char mapping for the replacement in the field id
					indices.put(indexVar++, node.getIndex());
				}
			}
			pathString = propertyPathKey.toString();
		}

		public String getPathString() {
			return pathString;
		}

		/**
		 * Resolves the real field id of a JSF field by replacing the iterator replacement char with the concrete index.
		 * @param abstractName the field if with replacement char
		 * @return a field id without a replacement char
		 */
		public String resolveFieldName(String abstractName) {
			for (Entry<Character, Integer> e : indices.entrySet()) {
				abstractName = abstractName.replace("{" + e.getKey() + "}", e
						.getValue().toString());
			}
			return abstractName;
		}
	}
}
