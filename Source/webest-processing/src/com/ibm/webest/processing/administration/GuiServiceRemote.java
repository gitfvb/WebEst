package com.ibm.webest.processing.administration;

import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

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

/**
 * Data interface for GUI layer.<br>
 * Provides data retrieved from database for the managed beans of the GUI layer.
 * 
 * @author Gregor Schumm
 */
@Remote
public interface GuiServiceRemote {

	/**
	 * @return a list of all available templates
	 * @throws GuiServiceException
	 */
	public List<Template> getAllTemplates() throws GuiServiceException;

	/**
	 * @return all available organizations
	 * @throws GuiServiceException
	 */
	public List<Organization> getAllOrganizations() throws GuiServiceException;

	/**
	 * @return all available estimators (users with role estimator)
	 * @throws GuiServiceException
	 */
	public List<User> getAllEstimators() throws GuiServiceException;

	/**
	 * @return all available countries
	 * @throws GuiServiceException
	 */
	public List<Country> getAllCountries() throws GuiServiceException;

	/**
	 * @return all available industry sector groups
	 * @throws GuiServiceException
	 */
	public List<IndustrySectorGroup> getAllIndustrySectorGroups()
			throws GuiServiceException;

	/**
	 * @return all available application type groups
	 * @throws GuiServiceException
	 */
	public List<ApplicationTypeGroup> getAllApplicationTypeGroups()
			throws GuiServiceException;

	/**
	 * @return all available monetary units
	 * @throws GuiServiceException
	 */
	public List<MonetaryUnit> getAllMonetaryUnits() throws GuiServiceException;

	/**
	 * @return all available mttd time units
	 * @throws GuiServiceException
	 */
	public List<MTTDTimeUnit> getAllMttdTimeUnits() throws GuiServiceException;

	/**
	 * @return all available effort units
	 * @throws GuiServiceException
	 */
	public List<EffortUnit> getAllEffortUnits() throws GuiServiceException;

	/**
	 * Retrieves all staffing shapes from database.
	 * 
	 * @return all available staffing shapes
	 */
	public List<StaffingShape> getAllStaffingShapes()
			throws GuiServiceException;

	/**
	 * Retrieves all granularity questions in the following form: { { factorName
	 * => { { granularityLevel => question }}}}
	 * 
	 * @return all granularity questions in the following form: { { factorName
	 *         => { { granularityLevel => question }}}}
	 * @throws GuiServiceException
	 */
	public Map<String, Map<GranularityLevel, GranularityQuestion>> getAllGranularityQuestions()
			throws GuiServiceException;

	/**
	 * Retrieves all granularity levels.
	 * 
	 * @return all granularity levels
	 * @throws GuiServiceException
	 */
	public List<GranularityLevel> getAllGranularityLevels()
			throws GuiServiceException;

	/**
	 * Retrieves all usecase complexities
	 * 
	 * @return all usecase complexities
	 * @throws GuiServiceException
	 */
	public List<UseCaseComplexity> getAllUseCaseComplexities()
			throws GuiServiceException;

	/**
	 * Retrieves all use case certainties
	 * 
	 * @return all use case certainties
	 * @throws GuiServiceException
	 */
	public List<Certainty> getAllCertainties() throws GuiServiceException;

	/**
	 * Retrieves all PiHistory Categories
	 * 
	 * @return all PiHistory Categories
	 * @throws GuiServiceException
	 */
	List<PiHistoryCategory> getAllPiHistoryCategories()
			throws GuiServiceException;

}
