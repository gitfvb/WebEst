package com.ibm.webest.processing.model;

import java.io.Serializable;

import com.ibm.webest.persistence.model.Solution;

/**
 * EstimationResult
 * 
 * @author Andre Munzinger
 * @author David Dornseifer
 */
public class EstimationResult implements Serializable {

	private static final long serialVersionUID = 6975092756474109527L;

	private Solution sol;
	private PutnamResult pResult;
	private COCOMOResult cResult;
	private int ucp;
	private float sloc;

	/**
	 * getSolution
	 * 
	 * @return given Solution
	 */
	public Solution getSolution() {
		return sol;
	}

	/**
	 * setSolution
	 * 
	 * @param sol Solution
	 */
	public void setSolution(Solution sol) {
		this.sol = sol;
	}

	/**
	 * getPutnamResult
	 * 
	 * @return PutnamResult
	 */
	public PutnamResult getPutnamResult() {
		return pResult;
	}

	/**
	 * setPutnamResult
	 * 
	 * @param pResult PutnamResult
	 */
	public void setPutnamResult(PutnamResult pResult) {
		this.pResult = pResult;
	}

	/**
	 * getCocomoResult
	 * 
	 * @return COCOMOResult
	 */
	public COCOMOResult getCocomoResult() {
		return cResult;
	}

	/**
	 * setCocomoResult
	 * 
	 * @param cResult COCOMOResult
	 */
	public void setCocomoResult(COCOMOResult cResult) {
		this.cResult = cResult;
	}

	/**
	 * getUcp
	 * 
	 * @return UCP
	 */
	public int getUcp() {
		return ucp;
	}

	/**
	 * setUcp
	 * 
	 * @param ucp
	 */
	public void setUcp(int ucp) {
		this.ucp = ucp;
	}

	/**
	 * getSloc
	 * 
	 * @return Sourcelines of code
	 */
	public float getSloc() {
		return sloc;
	}

	/**
	 * setSloc
	 * 
	 * @param sloc Sourcelines
	 *            of code
	 */
	public void setSloc(float sloc) {
		this.sloc = sloc;
	}

}
