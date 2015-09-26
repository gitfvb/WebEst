package com.ibm.webest.processing.calculation;

import javax.ejb.Remote;

import com.ibm.webest.processing.model.EstimationResult;
import com.ibm.webest.processing.model.Report;

/**
 * Create a Report
 * 
 * @author Wail Shakir
 */
@Remote
public interface ReportCalculationServiceRemote {

	/**
	 * The function create created a report with the information from
	 * EstimationResult.
	 * 
	 * 
	 * @param result
	 *            is the calculated information (duration "TD from COCOMO" and
	 *            effort from Putnam ) for the solution x
	 * @return a report
	 * @see Report
	 * @throws ReportException
	 */
	public Report createReport(EstimationResult result) throws ReportException;
}
