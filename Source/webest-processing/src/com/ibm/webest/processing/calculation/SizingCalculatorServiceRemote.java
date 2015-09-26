package com.ibm.webest.processing.calculation;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.ibm.webest.persistence.model.GranularityLevel;
import com.ibm.webest.persistence.model.GranularityQuestion;
import com.ibm.webest.persistence.model.Solution;
import com.ibm.webest.persistence.model.UseCase;

/**
 * SizingCalculatorServiceRemote Interface for sizing-related calculations
 * 
 * @author Andre Munzinger, David Dornseifer, Oliver Kreis
 * @version 1.0
 */
@Remote
public interface SizingCalculatorServiceRemote {

	/**
	 * calcUseCaseGranularity Calculates the GranularityLevel of a project with
	 * help of the given form
	 * 
	 * @param questions
	 *            with questionsfactors
	 * @return GranularityLevel
	 * @throws SizingCalculatorException
	 *             Thrown if - questions are null - questions Size is zero -
	 *             Exception occured in SolutionFacade
	 */
	public GranularityLevel calcUseCaseGranularity(
			List<GranularityQuestion> questions)
			throws SizingCalculatorException;

	/**
	 * calcOverallCertanty
	 * 
	 * @param sol
	 *            Solution -object
	 * @return OverallCertanty
	 * @throws SizingCalculatorException
	 *             Thrown if - Solution is null - Division by zero
	 */
	public float calcOverallCertanty(Solution sol)
			throws SizingCalculatorException;

	/**
	 * calcUncertantyRannge
	 * 
	 * @param sol
	 *            Solution-object
	 * @return UncertantyRange
	 * @throws SizingCalculatorException
	 *             Thrown if - Solution is null
	 */
	public float calcUncertantyRange(Solution sol)
			throws SizingCalculatorException;

	/**
	 * calculateUCP Calculates the UCP from the given solution
	 * 
	 * @param sol
	 *            Solution -object
	 * @return UCP-value
	 * @throws SizingCalculatorException
	 *             Thrown if - Solution is null
	 */
	public ArrayList<ArrayList<Integer>> calculateTotalUCP(Solution sol)
			throws SizingCalculatorException;

	/**
	 * calcUCP Calculates the total UCP of one usecase
	 * 
	 * @param item
	 *            UseCase -item
	 * @param gLevel
	 * @return ucp
	 * @throws SizingCalculatorException
	 *             Thrown if - Item is null - UseCaseComplexity of item is null
	 *             - GranularityOverride of item is null
	 */
	public int calcUCP(UseCase item, GranularityLevel gLevel)
			throws SizingCalculatorException;

}
