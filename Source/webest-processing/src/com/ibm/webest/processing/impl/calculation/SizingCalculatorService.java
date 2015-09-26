package com.ibm.webest.processing.impl.calculation;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import com.ibm.webest.persistence.model.GranularityLevel;
import com.ibm.webest.persistence.model.GranularityQuestion;
import com.ibm.webest.persistence.model.Solution;
import com.ibm.webest.persistence.model.UseCase;
import com.ibm.webest.persistence.model.UseCasePack;
import com.ibm.webest.persistence.service.DatabaseConnectionException;
import com.ibm.webest.persistence.service.EntityNotFoundException;
import com.ibm.webest.persistence.service.SolutionFacadeLocal;
import com.ibm.webest.processing.administration.AuthenticationServiceLocal;
import com.ibm.webest.processing.calculation.SizingCalculatorException;
import com.ibm.webest.processing.calculation.SizingCalculatorServiceLocal;
import com.ibm.webest.processing.calculation.SizingCalculatorServiceRemote;

/**
 * SizingCalculatorService Does sizing-related calculations
 * 
 * @author Andre Munzinger, David Dornseifer, Oliver Kreis
 * @version 1.0
 */
@DeclareRoles({ AuthenticationServiceLocal.ROLE_ADMINISTRATOR,
		AuthenticationServiceLocal.ROLE_ESTIMATOR,
		AuthenticationServiceLocal.ROLE_MANAGER })
@RolesAllowed({ AuthenticationServiceLocal.ROLE_ADMINISTRATOR,
		AuthenticationServiceLocal.ROLE_ESTIMATOR,
		AuthenticationServiceLocal.ROLE_MANAGER })
@Stateless
public class SizingCalculatorService implements SizingCalculatorServiceLocal,
		SizingCalculatorServiceRemote {

	@EJB
	private SolutionFacadeLocal solFacade;

	private Logger logger = Logger.getLogger(SizingCalculatorService.class);

	@Override
	public GranularityLevel calcUseCaseGranularity(
			List<GranularityQuestion> questions)
			throws SizingCalculatorException {

		// Check inputparameters
		if (questions == null) {
			this.logger.error("calcUseCaseGranularity: Questions were null");
			throw new SizingCalculatorException("Questions were null.");
		} else if (questions.size() == 0) {
			this.logger.error("calcUseCaseGranularity: There are no questions");
			throw new SizingCalculatorException("There are no questions.");
		}

		// Initialize variables
		byte essential = 1, medium = 2, decomposed = 3;
		float sumEssential = 0, sumMedium = 0, sumDecomposed = 0;

		GranularityLevel result = null;

		try {

			for (GranularityQuestion q : questions) {
				if (q.getLevel().getId() == essential) {
					sumEssential += q.getFactor();
				} else if (q.getLevel().getId() == medium) {
					sumMedium += q.getFactor();
				} else if (q.getLevel().getId() == decomposed) {
					sumDecomposed += q.getFactor();
				}
			}

			// Decide what class has to be chosen
			if (sumEssential > sumMedium) {
				if (sumEssential > sumDecomposed) {
					result = solFacade.getGranularityLevelById(essential);
				} else {
					result = solFacade.getGranularityLevelById(decomposed);
				}
			} else {
				if (sumMedium >= sumDecomposed) {
					result = solFacade.getGranularityLevelById(medium);
				} else {
					result = solFacade.getGranularityLevelById(decomposed);
				}
			}

		} catch (EntityNotFoundException e) {
			this.logger.error(
					"calcUseCaseGranularity: Couldn't find Parammeter", e);
			throw new SizingCalculatorException(
					"Couldn't find Entity in database. Please see log for details.",
					e);
		} catch (DatabaseConnectionException e) {
			this.logger.error(
					"calcUseCaseGranularity: Couldn't connect to database", e);
			throw new SizingCalculatorException(
					"Couldn't connect to database. Please see log for details.",
					e);
		} catch (NullPointerException e) {
			this.logger
					.error("calcUseCaseGranularity: NullPointerException", e);
			throw new SizingCalculatorException(
					"An error occured. Please consult the administrator.", e);
		}

		return result;
	}

	@Override
	public float calcOverallCertanty(Solution sol)
			throws SizingCalculatorException {

		// Check inputparameters
		if (sol == null) {
			this.logger.error("calcOverallCertanty: Solution was null");
			throw new SizingCalculatorException("Solution was null.");
		}

		int anzHigh = 0, anzMedium = 0, anzLow = 0;
		double facHigh = 0, facMedium = 0, facLow = 0;

		// Calc overall certanty
		try {
			if (sol.getUseCasePacks().size() > 0) {
				for (UseCasePack ucp : sol.getUseCasePacks()) {
					if (ucp != null) {
						for (UseCase uc : ucp.getUseCases()) {
							if (uc != null && uc.isInScope()) {
								if (uc.getCertainty().getId().equals("L")) {
									anzLow++;
									facLow = uc.getCertainty().getFactor();
								} else if (uc.getCertainty().getId()
										.equals("M")) {
									anzMedium++;
									facMedium = uc.getCertainty().getFactor();
								} else if (uc.getCertainty().getId()
										.equals("H")) {
									anzHigh++;
									facHigh = uc.getCertainty().getFactor();
								}
							}
						}
					}
				}
			}
		} catch (NullPointerException e) {
			this.logger.error("calcOverallCertanty: NullPointerException", e);
			throw new SizingCalculatorException(
					"An error occured. Please consult the administrator.", e);
		}

		// Check for division by zero
		if ((anzLow + anzMedium + anzHigh) == 0) {
			this.logger.error("calcOverallCertanty: Division by zero");
			throw new SizingCalculatorException("Division by zero.");
		}

		return (float) ((anzLow * facLow + anzMedium * facMedium + anzHigh
				* facHigh) / (anzLow + anzMedium + anzHigh));
	}

	@Override
	public float calcUncertantyRange(Solution sol)
			throws SizingCalculatorException {

		// Check inputparameters
		if (sol == null) {
			this.logger.error("calcUncertantyRannge: Solution was null");
			throw new SizingCalculatorException("Solution was null.");
		}

		return 1 - calcOverallCertanty(sol);
	}

	@Override
	public ArrayList<ArrayList<Integer>> calculateTotalUCP(Solution sol)
			throws SizingCalculatorException {

		// Check inputparameters
		if (sol == null) {
			this.logger.error("calculateTotalUCP: Solution was null");
			throw new SizingCalculatorException("Solution was null.");
		} else {
			if (sol.getUseCasePacks() == null) {
				this.logger
						.error("calculateTotalUCP: There are no UseCasePacks");
				throw new SizingCalculatorException(
						"There are no UseCasePacks.");
			}
		}

		ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();

		int ucp = 0;
		int totalUCP = 0;

		try {

			List<UseCasePack> ucps = sol.getUseCasePacks();

			for (UseCasePack ucpack : ucps) {
				List<UseCase> ucs = ucpack.getUseCases();

				if (ucs == null) {
					this.logger
							.error("calculateTotalUCP: There are no UseCases");
					throw new SizingCalculatorException(
							"There are no UseCases.");
				}

				for (UseCase item : ucs) {

					if (item == null) {
						this.logger
								.error("calculateTotalUCP: There are no UseCase");
						throw new SizingCalculatorException(
								"There are no UseCase.");
					}

					ucp = calcUCP(item, sol.getGranularity());

					if (item.getMultiplier() > 0)
						totalUCP = (int) (calcUCP(item, sol.getGranularity()) * item
								.getMultiplier());
					else
						totalUCP = calcUCP(item, sol.getGranularity());

					ArrayList<Integer> ucList = new ArrayList<Integer>();
					ucList.add(ucp);
					ucList.add(totalUCP);

					result.add(ucList);
				}
			}

		} catch (NullPointerException e) {
			this.logger.error("calculateTotalUCP: NullPointerException", e);
			throw new SizingCalculatorException(
					"An error occured. Please consult the administrator.", e);
		}

		return result;
	}

	@Override
	public int calcUCP(UseCase item, GranularityLevel gLevel)
			throws SizingCalculatorException {
		int ucp = 0;

		if (item == null) {
			this.logger.error("calcUCP: One or both parameters were null");
			throw new SizingCalculatorException(
					"One or both parameters were null.");
		} else if (item.getUseCaseComplexity() == null) {
			this.logger.error("calcUCP: Item-UsecaseComplexity was null");
			throw new SizingCalculatorException(
					"Item-UsecaseComplexity was null.");
		} else if (item.getGranularityOverride() == null && gLevel == null) {
			this.logger.error("calcUCP: Item-GranularityOverride was null");
			throw new SizingCalculatorException(
					"Item-GranularityOverride and Solution-GranularityLevel was null.");
		}

		if (item.isInScope()) {
			if (item.getGranularityOverride() == null)
				ucp = (int) (item.getUseCaseComplexity().getFactor() * gLevel
						.getValue());
			else
				ucp = (int) (item.getUseCaseComplexity().getFactor() * item
						.getGranularityOverride().getValue());
		}

		return ucp;
	}

}