package com.ibm.webest.processing.impl.administration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.ibm.webest.persistence.model.ApplicationTypeGroup;
import com.ibm.webest.persistence.model.Certainty;
import com.ibm.webest.persistence.model.Country;
import com.ibm.webest.persistence.model.EffortUnit;
import com.ibm.webest.persistence.model.GranularityLevel;
import com.ibm.webest.persistence.model.GranularityQuestion;
import com.ibm.webest.persistence.model.IndustrySectorGroup;
import com.ibm.webest.persistence.model.MTTDTimeUnit;
import com.ibm.webest.persistence.model.MonetaryUnit;
import com.ibm.webest.persistence.model.Organization;
import com.ibm.webest.persistence.model.PiHistoryCategory;
import com.ibm.webest.persistence.model.StaffingShape;
import com.ibm.webest.persistence.model.Template;
import com.ibm.webest.persistence.model.UseCaseComplexity;
import com.ibm.webest.persistence.model.User;
import com.ibm.webest.persistence.service.DatabaseConnectionException;
import com.ibm.webest.persistence.service.ProjectEnvironmentFacadeLocal;
import com.ibm.webest.persistence.service.SolutionFacadeLocal;
import com.ibm.webest.persistence.service.UserFacadeLocal;
import com.ibm.webest.processing.administration.AuthenticationServiceLocal;
import com.ibm.webest.processing.administration.GuiServiceException;
import com.ibm.webest.processing.administration.GuiServiceLocal;
import com.ibm.webest.processing.administration.GuiServiceRemote;
import com.ibm.webest.processing.administration.LoggingServiceLocal;

/**
 * Data interface for GUI layer.<br>
 * Provides data retrieved from database for the managed beans of the GUI layer. 
 * 
 * @author Gregor Schumm
 */
@DeclareRoles({ AuthenticationServiceLocal.ROLE_ADMINISTRATOR,
	AuthenticationServiceLocal.ROLE_ESTIMATOR,
	AuthenticationServiceLocal.ROLE_MANAGER })
@RolesAllowed({ AuthenticationServiceLocal.ROLE_ADMINISTRATOR,
			AuthenticationServiceLocal.ROLE_ESTIMATOR,
			AuthenticationServiceLocal.ROLE_MANAGER })
@Stateless
public class GuiService implements GuiServiceLocal, GuiServiceRemote {

	@EJB
	private SolutionFacadeLocal solutionFacade;

	@EJB
	private ProjectEnvironmentFacadeLocal projectEnvFacade;
	
	@EJB
	private UserFacadeLocal userFacade;
	
	@EJB
	private LoggingServiceLocal logger;
	
	@Override
	public List<Template> getAllTemplates() throws GuiServiceException {
		try {
			return projectEnvFacade.getTemplates();
		} catch (DatabaseConnectionException e) {
			logger.error(this, "An error occured while fetching templates.", e);
			throw new GuiServiceException("An error occured while fetching templates.", e);
		}
	}


	@Override
	public List<Organization> getAllOrganizations() throws GuiServiceException{
		try {
			return projectEnvFacade.getOrganizations();
		} catch (DatabaseConnectionException e) {
			logger.error(this, "An error occured while fetching organizations.", e);
			throw new GuiServiceException("An error occured while fetching organizations.", e);
		}
	}


	@Override
	public List<User> getAllEstimators() throws GuiServiceException {
		try {
			return userFacade.getUsersByRole(AuthenticationServiceLocal.ROLE_ESTIMATOR, AuthenticationServiceLocal.ROLE_ADMINISTRATOR);
		} catch (Exception e) {
			logger.error(this, "An error occured while fetching estimators.", e);
			throw new GuiServiceException("An error occured while fetching estimators.", e);
		}
	}


	@Override
	public List<Country> getAllCountries()throws GuiServiceException{
		try {
			return projectEnvFacade.getCountries();
		} catch (DatabaseConnectionException e) {
			logger.error(this, "An error occured while fetching countries.", e);
			throw new GuiServiceException("An error occured while fetching countries.", e);
		}
	}


	@Override
	public List<IndustrySectorGroup> getAllIndustrySectorGroups() throws GuiServiceException{
		try {
			return projectEnvFacade.getIndustrySectorGroups();
		} catch (DatabaseConnectionException e) {
			logger.error(this, "An error occured while fetching industry sectors.", e);
			throw new GuiServiceException("An error occured while fetching industry sectors.", e);
		}
	}


	@Override
	public List<ApplicationTypeGroup> getAllApplicationTypeGroups() throws GuiServiceException{
		try {
			return projectEnvFacade.getApplicationTypeGroups();
		} catch (DatabaseConnectionException e) {
			logger.error(this, "An error occured while fetching application types.", e);
			throw new GuiServiceException("An error occured while fetching application types.", e);
		}
	}


	@Override
	public List<MonetaryUnit> getAllMonetaryUnits() throws GuiServiceException{
		try {
			return projectEnvFacade.getMonetaryUnits();
		} catch (DatabaseConnectionException e) {
			logger.error(this, "An error occured while fetching monetary units.", e);
			throw new GuiServiceException("An error occured while fetching monetary units.", e);
		}
	}


	@Override
	public List<MTTDTimeUnit> getAllMttdTimeUnits() throws GuiServiceException{
		try {
			return projectEnvFacade.getMTTDTimeUnits();
		} catch (DatabaseConnectionException e) {
			logger.error(this, "An error occured while fetching MTTD time units.", e);
			throw new GuiServiceException("An error occured while fetching MTTD time units.", e);
		}
	}


	@Override
	public List<EffortUnit> getAllEffortUnits()throws GuiServiceException{
		try {
			return projectEnvFacade.getEffortUnits();
		} catch (DatabaseConnectionException e) {
			logger.error(this, "An error occured while fetching effort units.", e);
			throw new GuiServiceException("An error occured while fetching effort units.", e);
		}
	}

	@Override
	public List<StaffingShape> getAllStaffingShapes() throws GuiServiceException {
		try {
			return solutionFacade.getStaffingShapes();
		} catch (Exception e) {
			logger.error(this, e.getMessage(), e);
			throw new GuiServiceException(e.getMessage(), e);
		} 
	}


	@Override
	public Map<String, Map<GranularityLevel, GranularityQuestion>> getAllGranularityQuestions() throws GuiServiceException {
		Map<String, Map<GranularityLevel, GranularityQuestion>> map = new HashMap<String, Map<GranularityLevel,GranularityQuestion>>();
		try {
			List<GranularityQuestion> lst = solutionFacade.getGranularityQuestions();
			for (GranularityQuestion q : lst) {
				if (map.containsKey(q.getFactorName())){
					map.get(q.getFactorName()).put(q.getLevel(), q);
				} else {
					Map<GranularityLevel, GranularityQuestion> subMap = new HashMap<GranularityLevel, GranularityQuestion>();
					subMap.put(q.getLevel(), q);
					map.put(q.getFactorName(), subMap);
				}
			}
			return map;
		} catch (Exception e) {
			logger.error(this, e.getMessage(), e);
			throw new GuiServiceException(e.getMessage(), e);
		}
	}


	@Override
	public List<UseCaseComplexity> getAllUseCaseComplexities() throws GuiServiceException {
		try {
			return solutionFacade.getUseCaseComplexities();
		} catch (DatabaseConnectionException e) {
			logger.error(this, e.getMessage(), e);
			throw new GuiServiceException(e.getMessage(), e);
		}
	}
	
	@Override
	public List<Certainty> getAllCertainties() throws GuiServiceException {
		try {
			return solutionFacade.getUseCaseCertainties();
		} catch (DatabaseConnectionException e) {
			logger.error(this, e.getMessage(), e);
			throw new GuiServiceException(e.getMessage(), e);
		}
	}
	
	@Override
	public List<GranularityLevel> getAllGranularityLevels() throws GuiServiceException {
		try {
			return solutionFacade.getGranularityLevels();
		} catch (DatabaseConnectionException e) {
			logger.error(this, e.getMessage(), e);
			throw new GuiServiceException(e.getMessage(), e);
		}
	}
	
	@Override
	public List<PiHistoryCategory> getAllPiHistoryCategories() throws GuiServiceException {
		try {
			return solutionFacade.getPiHistoryCategories();
		} catch (DatabaseConnectionException e) {
			logger.error(this, e.getMessage(), e);
			throw new GuiServiceException(e.getMessage(), e);
		}
	}
	
}
