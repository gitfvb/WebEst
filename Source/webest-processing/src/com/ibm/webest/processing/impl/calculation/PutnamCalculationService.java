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
import com.ibm.webest.processing.calculation.PutnamCalcException;
import com.ibm.webest.processing.calculation.PutnamCalculationServiceLocal;
import com.ibm.webest.processing.calculation.PutnamCalculationServiceRemote;
import com.ibm.webest.processing.model.COCOMOResult;
import com.ibm.webest.processing.model.PutnamResult;

/**
 * PutnamCalculationService Does calculations within the Putnam model
 * 
 * @author Andre Munzinger, David Dornseifer
 * @version 1.0
 */
@DeclareRoles({ AuthenticationServiceLocal.ROLE_ADMINISTRATOR,
		AuthenticationServiceLocal.ROLE_ESTIMATOR,
		AuthenticationServiceLocal.ROLE_MANAGER })
@RolesAllowed({ AuthenticationServiceLocal.ROLE_ADMINISTRATOR,
		AuthenticationServiceLocal.ROLE_ESTIMATOR,
		AuthenticationServiceLocal.ROLE_MANAGER })
@Stateless
public class PutnamCalculationService implements PutnamCalculationServiceLocal,
		PutnamCalculationServiceRemote {

	@EJB
	private CalculationFacadeLocal calcFacade;

	private Logger logger = Logger.getLogger(PutnamCalculationService.class);

	@Override
	public float getE(int sloc) throws PutnamCalcException {
		float e = 0;

		if (sloc <= 0) {
			this.logger.error("getE: Sourcelines of code were zero");
			throw new PutnamCalcException("Sourcelines of code were zero.");
		}

		try {
			if (sloc > 0 && sloc < 15000) {
				e = (float) calcFacade.getCalculationParameter("cocomo_e_5000");
			} else if (sloc >= 15000 && sloc < 20000) {
				e = (float) calcFacade
						.getCalculationParameter("cocomo_e_20000");
			} else if (sloc >= 20000 && sloc < 30000) {
				e = (float) calcFacade
						.getCalculationParameter("cocomo_e_30000");
			} else if (sloc >= 30000 && sloc < 40000) {
				e = (float) calcFacade
						.getCalculationParameter("cocomo_e_40000");
			} else if (sloc >= 40000 && sloc < 50000) {
				e = (float) calcFacade
						.getCalculationParameter("cocomo_e_50000");
			} else if (sloc >= 50000) {
				e = (float) calcFacade
						.getCalculationParameter("cocomo_e_>50000");
			}

		} catch (EntityNotFoundException e1) {
			this.logger.error("getE: Couldn't find Parammeter");
			throw new PutnamCalcException("Parameter not Found", e1);
		} catch (DatabaseConnectionException e1) {
			this.logger.error("getE: Couldn't find Parammeter", e1);
			throw new PutnamCalcException(
					"Could not establish databaseconnection");
		}

		return e;
	}

	/**
	 * calcEffort Calculates the effort with the Putnam model Putnam Effort = (
	 * Size / ( (PP * Dev.Time (Years) ) ^ (4/3) ) ^ 3 ) * E
	 * 
	 * @param cocomoResult
	 * @param PP
	 * @param Sourcelines of code
	 * @return calculated Putnam-Effort
	 * @throws PutnamCalcException
	 */
	private float calcEffort(float sloc, float pp, float cocomoResult)
			throws PutnamCalcException {

		if (sloc == 0) {
			this.logger.error("calcEffort: Sourcelines of code were zero");
			throw new PutnamCalcException("Sourcelines of code were zero.");
		} else if (pp == 0) {
			this.logger.error("calcEffort: PP was zero");
			throw new PutnamCalcException("PP was zero.");
		} else if (cocomoResult == 0) {
			this.logger.error("calcEffort: COCOMO time was zero");
			throw new PutnamCalcException("COCOMO time was zero.");
		}

		float result;

		// should load a value dependence on the sloc
		float e = getE((int) sloc);

		if ((pp * Math.pow(cocomoResult / 12.0, 4.0 / 3.0)) == 0) {
			this.logger.error("calcEffort: Division by zero");
			throw new PutnamCalcException("Division by zero.");
		}

		result = (float) Math.pow(
				sloc / (pp * Math.pow(cocomoResult / 12.0, 4.0 / 3.0)), 3.0)
				* e;

		return result;
	}

	/**
	 * calcMBI Calculates the MBI with the Putnam-Model MBI = Putnam-Effort(in
	 * Years) / COCOMO-Time (in years)^3
	 * 
	 * @param Putnam-Effort
	 * @param cocomoResult
	 * @return calculated MBI
	 * @throws PutnamCalcException
	 */
	private float calcMBI(float eff, float cocomoResult)
			throws PutnamCalcException {

		if (eff == 0) {
			this.logger.error("calcMBI: Effort was zero");
			throw new PutnamCalcException("Effort was zero.");
		} else if (cocomoResult == 0) {
			this.logger.error("calcMBI: COCOMO time was zero");
			throw new PutnamCalcException("COCOMO time was zero.");
		} else if (Math.pow((cocomoResult / 12.0), 3.0) == 0) {
			this.logger.error("calcMBI: Division by zero");
			throw new PutnamCalcException("Division by zero.");
		}

		float result = (float) (eff / Math.pow((cocomoResult / 12.0), 3.0));

		return result;
	}

	/**
	 * calcTD Calculates the TD with the Putnam-Model TD = COCOMO-Time
	 * @param cocomoResult
	 * @return calculated TD in months
	 * @throws PutnamCalcException
	 */
	private float calcTD(COCOMOResult cocomoResult) throws PutnamCalcException {

		if (cocomoResult == null) {
			this.logger.error("calcTD: COCOMO time was null");
			throw new PutnamCalcException("COCOMO time was null.");
		}

		float result = cocomoResult.getTime();

		return result;
	}

	@Override
	public PutnamResult calc(float sloc, float pp, COCOMOResult cocomoResults)
			throws PutnamCalcException, EntityNotFoundException,
			DatabaseConnectionException {

		if (sloc <= 0) {
			this.logger.error("calc: Sourcelines of code were null");
			throw new PutnamCalcException("Sourcelines of code were null.");
		} else if (pp <= 0) {
			this.logger.error("calc: PP was null");
			throw new PutnamCalcException("PP was null.");
		} else if (cocomoResults == null) {
			this.logger.error("calc: COCOMO time was null");
			throw new PutnamCalcException("COCOMO time was null.");
		}

		float eff = calcEffort(sloc, pp, cocomoResults.getTime());
		float mbi = calcMBI(eff, cocomoResults.getTime());
		float td = calcTD(cocomoResults);

		return new PutnamResult(eff, mbi, td);
	}

}