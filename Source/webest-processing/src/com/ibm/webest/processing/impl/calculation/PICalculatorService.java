package com.ibm.webest.processing.impl.calculation;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import com.ibm.webest.persistence.service.CalculationFacadeLocal;
import com.ibm.webest.persistence.service.DatabaseConnectionException;
import com.ibm.webest.persistence.service.EntityNotFoundException;
import com.ibm.webest.processing.administration.AuthenticationServiceLocal;
import com.ibm.webest.processing.calculation.PICalculatorException;
import com.ibm.webest.processing.calculation.PICalculatorServiceLocal;
import com.ibm.webest.processing.calculation.PICalculatorServiceRemote;

/**
 * PICalculatorService Does PI-related calculations
 * 
 * @author Andre Munzinger
 * @version 1.0
 */
@DeclareRoles({ AuthenticationServiceLocal.ROLE_ADMINISTRATOR,
		AuthenticationServiceLocal.ROLE_ESTIMATOR,
		AuthenticationServiceLocal.ROLE_MANAGER })
@RolesAllowed({ AuthenticationServiceLocal.ROLE_ADMINISTRATOR,
		AuthenticationServiceLocal.ROLE_ESTIMATOR,
		AuthenticationServiceLocal.ROLE_MANAGER })
@Stateless
public class PICalculatorService implements PICalculatorServiceLocal,
		PICalculatorServiceRemote {

	@EJB
	private CalculationFacadeLocal calcFacade;

	private Logger logger = Logger.getLogger(PICalculatorService.class);

	@Override
	public int calculatePP(int pi) throws PICalculatorException {

		if (pi <= 0) {
			this.logger.error("calculatePP: PI was not valid");
			throw new PICalculatorException("PI was not valid.");
		}

		int result = 0;

		try {
			result = calcFacade.getPpByPi(pi);
		} catch (EntityNotFoundException e) {
			this.logger.error("calculatePP: Couldn't find Parammeter", e);
			throw new PICalculatorException("Parameter not Found");
		} catch (DatabaseConnectionException e) {
			this.logger.error("calculatePP: Couldn't find Parammeter", e);
			throw new PICalculatorException(
					"Could not establish databaseconnection");
		}

		return result;
	}
}