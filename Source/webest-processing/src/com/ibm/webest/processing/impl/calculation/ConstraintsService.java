package com.ibm.webest.processing.impl.calculation;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import com.ibm.webest.persistence.model.Constraint;
import com.ibm.webest.persistence.service.CalculationFacadeLocal;
import com.ibm.webest.persistence.service.DatabaseConnectionException;
import com.ibm.webest.persistence.service.EntityNotFoundException;
import com.ibm.webest.processing.administration.AuthenticationServiceLocal;
import com.ibm.webest.processing.calculation.ConstraintsException;
import com.ibm.webest.processing.calculation.ConstraintsServiceLocal;
import com.ibm.webest.processing.calculation.ConstraintsServiceRemote;
import com.ibm.webest.processing.helpers.DefaultValues;
import com.ibm.webest.processing.model.EstimationResult;

/**
 * ConstraintsService Controlls the results with the created constraints
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
public class ConstraintsService implements ConstraintsServiceLocal,
		ConstraintsServiceRemote {

	@EJB
	private CalculationFacadeLocal calcFacade;

	private Logger logger = Logger.getLogger(ConstraintsService.class);

	@Override
	public Boolean checkResult(EstimationResult er) throws ConstraintsException {

		if (er == null) {
			this.logger.error("checkResult: Estimation result was null");
			throw new ConstraintsException("Estimation result was null.");
		}

		if (er.getSolution().getConstraints().size() == 0) {

			// add a temporary constraint to check if the life duration is in
			// bound
			Constraint tmpConstraint = new Constraint();
			tmpConstraint.setTarget(er.getPutnamResult().getTD());

			if (!checkMaxRequirements(er, tmpConstraint)) {
				return false;
			}
		} else {

			for (Constraint c : er.getSolution().getConstraints()) {
				if (c.getType().equals(
						DefaultValues.getString("duration_constraint_type"))) {

					if (!checkMinRequirements(er, c))
						return false;

					if (!checkMaxRequirements(er, c))
						return false;
				}
			}
		}

		return true;
	}

	/**
	 * checkMinRequirements Checks if wanted time is at least 3/4 of the
	 * estimated time
	 * @param EstimationResult
	 * @param Constraint
	 * @return True if EstimationResult is valid with the given constraint
	 */
	private Boolean checkMinRequirements(EstimationResult er, Constraint c)
			throws ConstraintsException {

		if (er == null) {
			this.logger
					.error("checkMinRequirements: Estimation result was null");
			throw new ConstraintsException("Estimationresult was null.");
		} else if (c == null) {
			this.logger.error("checkMinRequirements: Constraint was null");
			throw new ConstraintsException("Constraint was null.");
		}

		try {

			double factor = calcFacade
					.getCalculationParameter("min_duration_factor");
			if (er.getPutnamResult().getTD() * factor > c.getTarget())
				throw new ConstraintsException(
						"Minimum requirements violated. Wanted time is less than 3/4 of the estimated time.");

		} catch (EntityNotFoundException e) {
			this.logger.error("checkMinRequirements: Couldn't find Parammeter",
					e);
			throw new ConstraintsException("Parameter not Found");
		} catch (DatabaseConnectionException e) {
			this.logger.error("checkMinRequirements: Couldn't find Parammeter",
					e);
			throw new ConstraintsException(
					"Could not establish databaseconnection");
		}

		return true;
	}

	/**
	 * checkMaxRequirements Checks if wanted and estimated time is <= 3 years
	 * 
	 * @param EstimationResult
	 * @param Constraint
	 * @return True if EstimationResult is valid with the given constraint
	 * @throws ConstraintsException
	 */
	private Boolean checkMaxRequirements(EstimationResult er, Constraint c)
			throws ConstraintsException {

		if (er == null) {
			this.logger
					.error("checkMaxRequirements: Estimationresult was null");
			throw new ConstraintsException("Estimationresult was null.");
		} else if (c == null) {
			this.logger.error("checkMaxRequirements: Constraint was null");
			throw new ConstraintsException("Constraint was null.");
		}

		try {

			double factor = calcFacade.getCalculationParameter("max_duration");

			if (er.getPutnamResult().getTD() >= factor * 12) // factor in years,
																// TD in months
				throw new ConstraintsException(
						"Maximum requirements violated. Project duration is more than 3 years.");
			else if (c.getTarget() >= factor * 12)
				throw new ConstraintsException(
						"Maximum requirements violated. Wanted time is bigger than 3 years.");

		} catch (EntityNotFoundException e) {
			this.logger.error("checkMaxRequirements: Couldn't find Parammeter",
					e);
			throw new ConstraintsException("Parameter not Found");
		} catch (DatabaseConnectionException e) {
			this.logger.error("checkMaxRequirements: Couldn't find Parammeter",
					e);
			throw new ConstraintsException(
					"Could not establish database connection");
		}

		return true;
	}
}