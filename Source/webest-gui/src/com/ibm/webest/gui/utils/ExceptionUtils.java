package com.ibm.webest.gui.utils;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;
import org.apache.openjpa.util.ExceptionInfo;

/**
 * Utility class for handling exceptions. It provides functionality to search
 * for certain exceptions in the stack trace.
 * 
 * @author Gregor Schumm
 * 
 */
public final class ExceptionUtils {
	private static Configuration config;
	private static Logger log = Logger.getLogger(ExceptionUtils.class);

	/**
	 * Retrieves a exception of the given type from the given stack trace.
	 * 
	 * @param trace
	 *            the exception with the stack trace
	 * @param ex
	 *            the type of the exception to search for
	 * @return the first instance of the found exception in the stack trace or
	 *         null if it was not found
	 */
	public static Throwable getException(Throwable trace,
			Class<? extends Throwable> ex) {
		Throwable sub = trace;
		do {
			if (ex.isInstance(sub)) {
				return sub;
			}
		} while ((sub = sub.getCause()) != null);
		return null;
	}

	/**
	 * Checks if the message of an exception in the stack trace matches the
	 * given regular expression.
	 * 
	 * @param e
	 *            the exception with the stack trace
	 * @param regex
	 *            the regular expression
	 * @return true if the given regular expression matches one message in the
	 *         stack trace
	 */
	public static boolean exceptionMatches(Throwable e, String regex) {
		if (e == null)
			return false;
		do {
			if (e.getMessage() != null
					&& Pattern.matches(regex, e.getMessage()))
				return true;
		} while ((e = e.getCause()) != null);
		return false;
	}

	/**
	 * Gets a user readable message extracted from the given exception.<br>
	 * Patterns and their replacement strings are defined in
	 * 'exceptions.properties'.
	 * 
	 * @param e
	 *            the stack trace to search
	 * @param defaultMsg
	 *            a default message which is used if no matching exception is
	 *            found
	 * @return the message string
	 */
	public static String getMessageFromException(Throwable e, String defaultMsg) {
		if (e == null)
			return defaultMsg;
		// search the whole stack trace for a matching message
		do {
			if (e.getMessage() != null) {
				try {
					// retrieve rules for the current exception class
					List<Rule> rules = getRules(e.getClass());
					for (Rule rule : rules) {
						// if a rule matches, return the defined message
						if (rule.matches(e.getMessage()))
							return rule.getMessage();
					}
					// if e is of type ExceptionInfo it has a nested exception list to search too
					if (e instanceof ExceptionInfo) {
						ExceptionInfo ei = (ExceptionInfo) e;
						for (Throwable t : ei.getNestedThrowables()) {
							rules = getRules(t.getClass());
							for (Rule rule : rules) {
								if (rule.matches(t.getMessage()))
									return rule.getMessage();
							}
						}
					}
				} catch (Exception ex) {
					log.error("Error while getting message from exception.", ex);
				}
			}
		} while ((e = e.getCause()) != null);

		return defaultMsg;
	}

	/**
	 * Retrieves the rules for a given exception type from the 'exceptions.properties' file.
	 * @param exClass the type of the exception
	 * @return a list of rules, defined for the given exception
	 */
	@SuppressWarnings({ "rawtypes" })
	private static <E extends Throwable> List<Rule> getRules(Class<E> exClass) {
		List<Rule> rules = new java.util.LinkedList<ExceptionUtils.Rule>();
		// get all keys starting with exception name
		Iterator keys = getConfig().getKeys(exClass.getName());
		while (keys.hasNext()) {
			String key = (String) keys.next();
			String[] value = getConfig().getStringArray(key);
			if (value.length < 2)
				continue;
			try {
				// add new rule with the regex for the exception message and a replace message
				rules.add(new Rule(Pattern.compile(value[0]), value[1]));
			} catch (PatternSyntaxException e) {
				log.error("Exception pattern '" + value[0] + "' is invalid.", e);
			}
		}
		return rules;
	}

	/**
	 * @param config configuration holding the values of 'exceptions.properties'
	 */
	private static void setConfig(Configuration config) {
		ExceptionUtils.config = config;
	}

	/**
	 * @return configuration holding the values of 'exceptions.properties'
	 */
	private static Configuration getConfig() {
		if (config == null) {
			try {
				setConfig(new PropertiesConfiguration(
						ExceptionUtils.class
								.getResource("exceptions.properties")));
			} catch (ConfigurationException e) {
				log.error("Could not read 'exceptions.properties'.", e);
			}
		}
		return config;
	}

	/**
	 * Container for a exception rule.<br>
	 * Holds the regex, the replace string and the concrete message, if the regex matches. 
	 * @author Gregor Schumm
	 */
	private static class Rule {
		private Pattern regex;
		private String replace;
		private String msg;

		public Rule(Pattern regex, String replace) {
			super();
			this.regex = regex;
			this.replace = replace;
		}

		public String getMessage() {
			return msg;
		}

		/**
		 * Check if given message matches against the regex.<br>
		 * Sets the <code>message</code> property to the replacement message in case of a match.
		 * @param msg the message to test
		 * @return true if message matches against the pattern
		 */
		public boolean matches(String msg) {
			Matcher m = regex.matcher(msg);
			if (m.matches()) {
				this.msg = m.replaceAll(replace);
				return true;
			}
			return false;
		}
	}

}
