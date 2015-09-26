package com.ibm.webest.processing.model;

import java.io.Serializable;

/**
 * COCOMOResult Saves Effort in manmonths and time in months
 * 
 * @author Andre Munzinger
 * @author David Dornseifer
 */
public class COCOMOResult extends CalculationResult implements Serializable {
	private static final long serialVersionUID = 1L;

	// define the needed data fields for the cocomo calculation
	private Float time = null;
	private Float people = null;

	/**
	 * Constructor
	 * 
	 * @param eff
	 *            Effort
	 * @param time
	 *            COCOMO time
	 */
	public COCOMOResult(Float eff, Float time) {
		super.setEffort(eff);
		setTime(time);
	}

	/**
	 * setTime
	 * 
	 * @param t
	 *            timevalue
	 */
	private void setTime(Float t) {

		// Set the time
		this.time = t;

		// Calc the people who are nedded für the different granularities
		this.people = calcPeopleNeeded(getEffort(), t);
	}

	/**
	 * calcPeopleNeeded Calculates the people which are required for the project
	 * People required = COCOMO Effort / Estimated COCOMO Time
	 * 
	 * @param eff
	 *            COCOMOEffort
	 * @param time
	 *            COCOMOTime
	 * @return
	 */
	private Float calcPeopleNeeded(Float eff, Float time) {
		return eff / time;
	}

	/**
	 * getTime
	 * 
	 * @return COCOMOTime
	 */
	public Float getTime() {
		return this.time;
	}

	/**
	 * getPeople
	 * 
	 * @return number of people needed
	 */
	public Float getPeople() {
		return this.people;
	}

}