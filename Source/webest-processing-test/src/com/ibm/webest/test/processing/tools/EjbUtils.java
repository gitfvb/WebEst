package com.ibm.webest.test.processing.tools;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Utility class for the connection to remote EJBs
 * for use in JUnit test cases.
 * @author Gregor Schumm
 *
 */
public final class EjbUtils {
	/**
	 * Retrieves an EJB by his remote interface using the default name
	 * (class name).
	 * @param remoteInterface the class of the EJB remote interface
	 * @return instance of the EJB
	 * @throws NamingException EJB not found
	 */
	public static <T> T getEjb(Class<T> remoteInterface) throws NamingException {
		return getEjb(remoteInterface, null, null);
	}
	/**
	 * Retrieves an EJB by his remote interface using the default name
	 * (class name) and the given 
	 * authentication information.
	 * @param remoteInterface the class of the EJB remote interface
	 * @param username the username for authentication
	 * @param password the password for authentication
	 * @return instance of the EJB
	 * @throws NamingException EJB not found
	 */
	public static <T> T getEjb(Class<T> remoteInterface, String username, String password) throws NamingException {
		String beanName = remoteInterface.getSimpleName();
		return getEjb(remoteInterface, beanName, username, password);
	}
	/**
	 * Retrieves an EJB by his remote interface using the given name.
	 * @param remoteInterface the class of the EJB remote interface
	 * @param beanName the name of the session bean
	 * @return instance of the EJB
	 * @throws NamingException EJB not found
	 */
	public static <T> T getEjb(Class<T> remoteInterface, String beanName) throws NamingException {
		return getEjb(remoteInterface, beanName, null, null);
	}
	
	/**
	 * Retrieves an EJB by his remote interface using the given name
	 * and the given authentication information.
	 * @param remoteInterface the class of the EJB remote interface
	 * @param beanName the name of the session bean 
	 * @param username the username for authentication
	 * @param password the password for authentication
	 * @return instance of the EJB
	 * @throws NamingException EJB not found
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getEjb(Class<T> remoteInterface, String beanName, String username, String password) throws NamingException {
		Hashtable<String, String> props = new Hashtable<String, String>();
		if (username != null && password != null) {
			props.put(Context.SECURITY_PRINCIPAL, username);
			props.put(Context.SECURITY_CREDENTIALS, password);
			props.put("openejb.authentication.realmName", TestProperties.getEjbSecurityRealm());
		}
		props.put("java.naming.factory.initial", "org.openejb.client.RemoteInitialContextFactory");
		InitialContext ctx = new InitialContext(props);
		return (T)ctx.lookup(beanName);
	}
	
}
