package com.ibm.webest.persistence.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.ibm.webest.persistence.model.CalculationParameter;
import com.ibm.webest.persistence.model.PiPpLookup;
import com.ibm.webest.persistence.service.CalculationFacadeLocal;
import com.ibm.webest.persistence.service.CalculationFacadeRemote;
import com.ibm.webest.persistence.service.DatabaseConnectionException;
import com.ibm.webest.persistence.service.AuthenticationRoles;
import com.ibm.webest.persistence.service.EntityNotFoundException;

/**
 * Provides functionality to retrieve calculation relevant data from the
 * database.<br>
 * Primarily used for parameter lookup.
 * 
 * @author Gregor Schumm
 * 
 */
@DeclareRoles({ AuthenticationRoles.ROLE_ADMINISTRATOR, AuthenticationRoles.ROLE_ESTIMATOR,
		AuthenticationRoles.ROLE_MANAGER })
@RolesAllowed({ AuthenticationRoles.ROLE_ADMINISTRATOR, AuthenticationRoles.ROLE_ESTIMATOR,
		AuthenticationRoles.ROLE_MANAGER })
@Stateless
@SuppressWarnings("unchecked")
public class CalculationFacade implements CalculationFacadeRemote,
		CalculationFacadeLocal {
	@PersistenceContext
	private EntityManager entityManager;

	public CalculationFacade() {
	}
	
	@PermitAll
	@Override
	public int getPpByPi(int pi) throws EntityNotFoundException,
			DatabaseConnectionException {
		PiPpLookup piPp = null;

		try {
			piPp = entityManager.find(PiPpLookup.class, pi);
		} catch (Exception e) {
			throw new DatabaseConnectionException(e);
		}

		if (piPp == null)
			throw new EntityNotFoundException("PI not found in database.");
		return piPp.getPp();

	}

	@Override
	public double getCalculationParameter(String key)
			throws EntityNotFoundException, DatabaseConnectionException {
		CalculationParameter calcParam = null;

		try {
			calcParam = entityManager.find(CalculationParameter.class, key);
		} catch (Exception e) {
			throw new DatabaseConnectionException(e);
		}

		if (calcParam == null)
			throw new EntityNotFoundException(
					"No calculation parameter found for key '" + key + "'.");
		return calcParam.getValue();
	}

	@Override
	public Map<String, Double> getCalculationParameters()
			throws DatabaseConnectionException {
		Map<String, Double> paramMap = new HashMap<String, Double>();
		try {
			Query query = entityManager
					.createQuery("select param from CalculationParameter param");

			List<CalculationParameter> params = (List<CalculationParameter>) query
					.getResultList();
			for (CalculationParameter param : params) {
				paramMap.put(param.getKey(), param.getValue());
			}
		} catch (Exception e) {
			throw new DatabaseConnectionException(e);
		}

		return paramMap;
	}

}
