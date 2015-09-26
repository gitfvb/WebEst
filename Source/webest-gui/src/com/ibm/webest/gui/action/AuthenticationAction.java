package com.ibm.webest.gui.action;

import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.ibm.webest.processing.administration.AuthenticationServiceLocal;
/**
 * Managed bean for authentication tasks.
 * @author Gregor Schumm
 *
 */
public class AuthenticationAction {
	
	@EJB
	private AuthenticationServiceLocal authService;
	private static Logger log = Logger.getLogger(AuthenticationAction.class);
	
	/**
	 * Logs out the current login user, by destroying the current session.
	 * @return "success" after session is destroyed
	 */
	public String logout() {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) context.getExternalContext()
				.getSession(false);
		log.info("Session "+session.getId()+" destroyed. User "+context.getExternalContext().getUserPrincipal().getName()+" logged off.");
		session.invalidate();
		return "success";
	}

	/**
	 * @return true if the login user has the estimator role
	 */
	public boolean isEstimator() {
		return authService.isEstimator();
	}
	/**
	 * @return true if the login user has the manager role
	 */
	public boolean isManager() {
		return authService.isManager();
	}
	/**
	 * @return true if the login user has the administrator role
	 */
	public boolean isAdministrator() {
		return authService.isAdministrator();
	}
	/**
	 * @return true if the login user is allowed to edit estimates
	 */
	public boolean isEstimateEditable() {
		return isEstimator()||isAdministrator();
	}
	
}
