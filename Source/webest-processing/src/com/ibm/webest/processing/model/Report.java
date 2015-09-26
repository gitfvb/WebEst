package com.ibm.webest.processing.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.ibm.webest.persistence.model.Solution;
import com.ibm.webest.processing.impl.calculation.ReportCalculationService;

/**
 * Report Represents a calculated report and its information
 * 
 * @author David Dornseifer
 * @author Wail Shakir
 * @author Gregor Schumm
 */
public class Report implements Serializable {
	private static final long serialVersionUID = 8968253911883319837L;

	private Solution solution;
	private RayleighResult rayleighResults;
	private float averageStaff;
	private int ucp;
	private float sloc;

	private ProjectLife life;

	// keeps the overall information and details of all phases
	private List<ReportPhase> phases = new ArrayList<ReportPhase>();

	/**
	 * constructor for the report
	 * 
	 * @param solution
	 *            the solution for this report
	 * @param ucp
	 * 
	 * @param sloc
	 */
	public Report(Solution solution, int ucp, float sloc) {
		this.solution = solution;
		this.ucp = ucp;
		this.sloc = sloc;
	}

	// no args default constructor
	public Report() {

	}

	/**
	 * @return a ProjectPhase array with 5 element the first element is the life
	 *         phase of the project and the remaining elements are the phases
	 *         form ProjectEnvironment
	 * @see ReportCalculationService
	 */
	public List<ReportPhase> getPhases() {
		return phases;
	}

	/**
	 * @return the name of this report
	 */
	public String getName() {
		return getSolution().getName();
	}

	/**
	 * @return the solution of this report
	 * @see Solution
	 */
	public Solution getSolution() {
		return solution;
	}

	/**
	 * @return a rayleigh result
	 * @see RayleighResult
	 */
	public RayleighResult getRayleighResults() {
		return rayleighResults;
	}

	/**
	 * @param rayleighResults
	 *            set the rayleigh result array for this report
	 */
	public void setRayleighResults(RayleighResult rayleighResults) {
		this.rayleighResults = rayleighResults;
	}

	/**
	 * @return the life average staff for this Solution
	 */
	public float getAverageStaff() {
		return averageStaff;
	}

	/**
	 * @param averageStaff
	 *            the life average staff for this Solution
	 */
	public void setAverageStaff(float averageStaff) {
		this.averageStaff = averageStaff;
	}

	/**
	 * @return the ucp for the solution from this report
	 */
	public int getUcp() {
		return ucp;
	}

	/**
	 * @return the sloc for the solution from this report
	 */
	public float getSloc() {
		return sloc;
	}

	/**
	 * @param life
	 *            the project life summary
	 */
	public void setLife(ProjectLife life) {
		this.life = life;
	}

	/**
	 * @return the project life summary
	 */
	public ProjectLife getLife() {
		return life;
	}

}