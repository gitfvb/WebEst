package com.ibm.webest.processing.model;

import java.io.Serializable;


/**
 * PutnamResultService
 * Saves effort in years, mbi and td
 * @author Andre Munzinger
 */
public class PutnamResult extends CalculationResult implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	//define storage data type for holding putnam values 
	private Float mbi = null;
	private Float td = null;
	
	
	/**
	 * Constructor
	 * @param eff
	 * @param mbi
	 * @param td
	 */
	public PutnamResult(Float eff, Float mbi, Float td) {
		super.setEffort(eff, CalculationResult.CONVERSION_FLAG_NONE);
		setMBI(mbi);
		setTD(td);
	}

	
	/**
	 * setMBI
	 * @param MBI value
	 */
	private void setMBI(Float mbi){
		this.mbi = mbi;
	}
	
	
	
	/**
	 * getMBI
	 * @return MBI value
	 */
	public Float getMBI(){
		return this.mbi;
	}
	
	
	/**
	 * setTD
	 * @param TD value
	 */
	private void setTD(Float td){
		this.td = td;
	}
	
	
	/**
	 * getTD
	 * @return TD value
	 */
	public Float getTD(){
		return this.td;
	}
}