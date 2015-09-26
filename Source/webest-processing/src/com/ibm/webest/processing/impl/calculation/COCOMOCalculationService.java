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
import com.ibm.webest.processing.calculation.COCOMOCalcException;
import com.ibm.webest.processing.calculation.COCOMOCalculationServiceLocal;
import com.ibm.webest.processing.calculation.COCOMOCalculationServiceRemote;
import com.ibm.webest.processing.model.COCOMOResult;

/**
 * COCOMOCalculationService Does calculations withing the COCOMO I model
 * 
 * @author Andre Munzinger
 * @author David Dornseifer
 * @version 1.0
 */
@DeclareRoles({ AuthenticationServiceLocal.ROLE_ADMINISTRATOR,
		AuthenticationServiceLocal.ROLE_ESTIMATOR,
		AuthenticationServiceLocal.ROLE_MANAGER })
@RolesAllowed({ AuthenticationServiceLocal.ROLE_ADMINISTRATOR,
		AuthenticationServiceLocal.ROLE_ESTIMATOR,
		AuthenticationServiceLocal.ROLE_MANAGER })
@Stateless
public class COCOMOCalculationService implements COCOMOCalculationServiceLocal,
		COCOMOCalculationServiceRemote {

	@EJB
	private CalculationFacadeLocal calcFacade;

	private Logger logger = Logger.getLogger(COCOMOCalculationService.class);

	/**
	 * calcEffort Calculates the effort with the COCOMO model COCOMO Effort = A
	 * * Size (KSLOC) ^ B
	 * 
	 * @param ksloc
	 *            Sourcelines of Code as product of thousand (e.g. 31.250 if
	 *            sloc is 31,250)
	 * @return calculated COCOMO-Effort
	 * @throws COCOMOCalcException
	 */
	private float calcEffort(float ksloc) throws COCOMOCalcException {
		float result = 0;

		if (ksloc == 0) {
			this.logger.error("calcEffort: KSLOC was zero.");
			throw new COCOMOCalcException("KSLOC was zero.");
		}

		try {
			Float a = new Float(
					calcFacade.getCalculationParameter("cocomo_a_simple"));
			Float b = new Float(
					calcFacade.getCalculationParameter("cocomo_b_simple"));
			result = (float) (a * Math.pow(ksloc, b));

		} catch (EntityNotFoundException e) {
			this.logger.error("calcEffort: Couldn't find Parammeter", e);
			throw new COCOMOCalcException(
					"Couldn't find Entity in database. Please see log for details.",
					e);
		} catch (DatabaseConnectionException e) {
			this.logger.error("calcEffort: Couldn't connect to database", e);
			throw new COCOMOCalcException(
					"Couldn't connect to database. Please see log for details.",
					e);
		}

		return result;
	}

	/**
	 * calcTime Calculates the time with the cocomo model Estimated COCOMO Time
	 * = C * COCOMO Effort ^ D
	 * 
	 * @param eff
	 *            [i COCOMO-Effort
	 * @return calculated COCOMO-time
	 * @throws COCOMOCalcException
	 */
	private float calcTime(float eff) throws COCOMOCalcException {
		float result = 0;

		if (eff == 0) {
			this.logger.error("calcTime: Effort was zero.");
			throw new COCOMOCalcException("Effort was zero.");
		}

		try {
			Double c = calcFacade.getCalculationParameter("cocomo_c_simple");
			Double d = calcFacade.getCalculationParameter("cocomo_d_simple");
			result = (float) (c * Math.pow(eff, d));

		} catch (EntityNotFoundException e) {
			this.logger.error("calcTime: Couldn't find Parammeter", e);
			throw new COCOMOCalcException("Parameter not Found");
		} catch (DatabaseConnectionException e) {
			this.logger.error("calcTime: Couldn't find Parammeter", e);
			throw new COCOMOCalcException(
					"Could not establish databaseconnection");
		}

		return result;
	}

	@Override
	public COCOMOResult calc(float sloc) throws COCOMOCalcException {

		if (sloc <= 0) {
			this.logger.error("calc: Sourcelines of code were zero.");
			throw new COCOMOCalcException("Sourcelines of code were zero.");
		}

		float eff = calcEffort(sloc / 1000);
		float time = calcTime(eff);

		return new COCOMOResult(eff, time);
	}

}
