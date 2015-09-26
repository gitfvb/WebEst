package com.ibm.webest.persistence.service;

import java.util.List;

import javax.ejb.Remote;
import com.ibm.webest.persistence.model.*;

/**
 * Facade for entity ProjectEnvironment.<br>
 * Provides functionality to retrieve available values for the project
 * environment parameters.
 * 
 * @author Gregor Schumm
 */
@Remote
public interface ProjectEnvironmentFacadeRemote {
	/**
	 * @return all available countries ordered by name
	 * @throws DatabaseConnectionException
	 *             an error occurred while connecting to the database
	 */
	public List<Country> getCountries() throws DatabaseConnectionException;

	/**
	 * @return all available monetary units ordered by name
	 * @throws DatabaseConnectionException
	 *             an error occurred while connecting to the database
	 */
	public List<MonetaryUnit> getMonetaryUnits()
			throws DatabaseConnectionException;

	/**
	 * @return all available effort units ordered by name
	 * @throws DatabaseConnectionException
	 *             an error occurred while connecting to the database
	 */
	public List<EffortUnit> getEffortUnits() throws DatabaseConnectionException;

	/**
	 * Retrieves the reference unit (having the <code>reference</code> attribute set to true) of the given unit type.
	 * @param unitType the type of the measuring unit
	 * @return the object of the reference unit
	 * @throws EntityNotFoundException if not reference unit was found
	 * @throws DatabaseConnectionException an error occurred while connecting to the database
	 */
	public <U extends MeasuringUnit> U getReferenceUnit(Class<U> unitType) throws EntityNotFoundException, DatabaseConnectionException;
	
	/**
	 * @return all available mean time to defect units ordered by name
	 * @throws DatabaseConnectionException
	 *             an error occurred while connecting to the database
	 */
	public List<MTTDTimeUnit> getMTTDTimeUnits()
			throws DatabaseConnectionException;

	/**
	 * @return all available application type groups ordered by name
	 * @throws DatabaseConnectionException
	 *             an error occurred while connecting to the database
	 */
	public List<ApplicationTypeGroup> getApplicationTypeGroups()
			throws DatabaseConnectionException;

	/**
	 * @return all available industry sector groups ordered by name
	 * @throws DatabaseConnectionException
	 *             an error occurred while connecting to the database
	 */
	public List<IndustrySectorGroup> getIndustrySectorGroups()
			throws DatabaseConnectionException;

	/**
	 * @return all available organizations ordered by name
	 * @throws DatabaseConnectionException
	 *             an error occurred while connecting to the database
	 */
	public List<Organization> getOrganizations()
			throws DatabaseConnectionException;

	/**
	 * @return all available templates
	 * @throws DatabaseConnectionException
	 *             an error occurred while connecting to the database
	 */
	public List<Template> getTemplates() throws DatabaseConnectionException;

	/**
	 * Merges the given template into the database.
	 * 
	 * @param template
	 *            the template to save
	 * @return the saved template
	 * @throws DatabaseConnectionException
	 *             an error occurred while connecting to the database
	 */
	public Template saveTemplate(Template template)
			throws DatabaseConnectionException;

}
