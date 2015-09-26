package com.ibm.webest.persistence.service.impl;

import java.util.List;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import com.ibm.webest.persistence.model.ApplicationTypeGroup;
import com.ibm.webest.persistence.model.Country;
import com.ibm.webest.persistence.model.EffortUnit;
import com.ibm.webest.persistence.model.IndustrySectorGroup;
import com.ibm.webest.persistence.model.MTTDTimeUnit;
import com.ibm.webest.persistence.model.MeasuringUnit;
import com.ibm.webest.persistence.model.MonetaryUnit;
import com.ibm.webest.persistence.model.Organization;
import com.ibm.webest.persistence.model.Template;
import com.ibm.webest.persistence.service.AuthenticationRoles;
import com.ibm.webest.persistence.service.DatabaseConnectionException;
import com.ibm.webest.persistence.service.EntityNotFoundException;
import com.ibm.webest.persistence.service.ProjectEnvironmentFacadeLocal;
import com.ibm.webest.persistence.service.ProjectEnvironmentFacadeRemote;

/**
 * Facade for entity ProjectEnvironment.<br>
 * Provides functionality to retrieve available values for the project
 * environment parameters.
 * 
 * @author Gregor Schumm
 */
@DeclareRoles({ AuthenticationRoles.ROLE_ADMINISTRATOR, AuthenticationRoles.ROLE_ESTIMATOR,
		AuthenticationRoles.ROLE_MANAGER })
@RolesAllowed({ AuthenticationRoles.ROLE_ADMINISTRATOR, AuthenticationRoles.ROLE_ESTIMATOR,
		AuthenticationRoles.ROLE_MANAGER })
@Stateless
@SuppressWarnings("unchecked")
public class ProjectEnvironmentFacade implements ProjectEnvironmentFacadeRemote,
		ProjectEnvironmentFacadeLocal {

	@PersistenceContext
	private EntityManager entityManager;

	public ProjectEnvironmentFacade() {
	}

	@Override
	public List<Country> getCountries() throws DatabaseConnectionException {
		try {
			return entityManager.createQuery(
					"select c from Country c order by c.name").getResultList();
		} catch (Exception e) {
			throw new DatabaseConnectionException(e);
		}
	}

	@Override
	public List<MonetaryUnit> getMonetaryUnits()
			throws DatabaseConnectionException {
		try {
			return getUnits(MonetaryUnit.class);
		} catch (Exception e) {
			throw new DatabaseConnectionException(e);
		}
	}

	private <U extends MeasuringUnit> List<U> getUnits(Class<U> type) {
		return entityManager.createQuery(
				"select o from " + type.getSimpleName() + " o order by o.name")
				.getResultList();
	}

	@Override
	public List<EffortUnit> getEffortUnits() throws DatabaseConnectionException {
		try {
			return getUnits(EffortUnit.class);
		} catch (Exception e) {
			throw new DatabaseConnectionException(e);
		}
	}

	@Override
	public List<MTTDTimeUnit> getMTTDTimeUnits()
			throws DatabaseConnectionException {
		try {
			return getUnits(MTTDTimeUnit.class);
		} catch (Exception e) {
			throw new DatabaseConnectionException(e);
		}
	}

	@Override
	public List<ApplicationTypeGroup> getApplicationTypeGroups()
			throws DatabaseConnectionException {
		try {
			return entityManager.createQuery(
					"select o from ApplicationTypeGroup o order by o.name")
					.getResultList();
		} catch (Exception e) {
			throw new DatabaseConnectionException(e);
		}

	}

	@Override
	public List<IndustrySectorGroup> getIndustrySectorGroups()
			throws DatabaseConnectionException {
		try {
			return entityManager.createQuery(
					"select o from IndustrySectorGroup o order by o.name")
					.getResultList();
		} catch (Exception e) {
			throw new DatabaseConnectionException(e);
		}
	}

	@Override
	public List<Organization> getOrganizations()
			throws DatabaseConnectionException {
		try {
			return entityManager.createQuery(
					"select o from Organization o order by o.name")
					.getResultList();
		} catch (Exception e) {
			throw new DatabaseConnectionException(e);
		}
	}

	@Override
	public List<Template> getTemplates() throws DatabaseConnectionException {
		try {
			return entityManager.createQuery(
					"select t from Template t order by t.name").getResultList();
		} catch (Exception e) {
			throw new DatabaseConnectionException(e);
		}
	}

	@Override
	public Template saveTemplate(Template template)
			throws DatabaseConnectionException {
		try {
			Template merge = entityManager.merge(template);
			entityManager.flush();
			return merge;
		} catch (Exception e) {
			throw new DatabaseConnectionException(e);
		}
	}

	@Override
	public <U extends MeasuringUnit> U getReferenceUnit(Class<U> unitType)
			throws EntityNotFoundException, DatabaseConnectionException {
		try {
			return (U) entityManager.createQuery(
			"select u from "+unitType.getSimpleName()+" u where u.reference = true").getSingleResult();
		}catch (NoResultException e) {
			throw new EntityNotFoundException(e);
		}
		catch (Exception e) {
			throw new DatabaseConnectionException(e);
		}
	}

}
