package com.ibm.webest.gui.utils;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

/**
 * Utility class providing functionality to use JSF managed beans in Java code.
 * @author Gregor Schumm
 *
 */
@SuppressWarnings({"deprecation","unchecked"})
public final class ManagedBeanUtils {
	
	private static Logger log = Logger.getLogger(ManagedBeanUtils.class);
	
	/**
	 * Gets a currently managed bean by its name (defined in faces-config.xml).
	 * @param type the bean class
	 * @param name the name of the bean
	 * @return the current instance of the bean
	 */
	public static <T> T getBean(Class<T> type, String name) {
		return evaluateEl(name);
	}
	
	/**
	 * Destroys a session managed bean by its name.
	 * @param name the name of the bean
	 */
	public static <T> void destroyBean(Class<T> type, String name) {
		FacesContext context = FacesContext.getCurrentInstance();
		try {
			context.getExternalContext().getSessionMap().put("#{"+name+"}", type.newInstance());
		} catch (Exception e) {
			log.error("Bean could not be destroyed.", e);
		}
	}
	
	/**
	 * Evaluates the given EL expression an returns the value.
	 * @param expression the expression string to evaluate
	 * @return the value returned by the expression
	 */
	public static <T> T evaluateEl(String expression) {
		if (expression == null)
			throw new IllegalArgumentException("Expression must not be null.");
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Application application = facesContext.getApplication();
		if (!expression.startsWith("#{"))
			expression = "#{" + expression + "}";
		ValueBinding binding = application
				.createValueBinding(expression);
		return (T) binding.getValue(facesContext);
	}
	
	/**
	 * Evaluates, if the browser is an mobile version of safari
	 * @return true, if a mobile safari is used, false if not
	 */
    public static Boolean isIosBrowser() {
        FacesContext context=FacesContext.getCurrentInstance();
        HttpServletRequest request=
           (HttpServletRequest)context.getExternalContext().
               getRequest();
        String useragent= request.getHeader("user-agent");
        useragent=useragent.toLowerCase();
        
        return (useragent.indexOf("safari")!=-1 && useragent.indexOf("mobile")!=-1);
    }
	
}
