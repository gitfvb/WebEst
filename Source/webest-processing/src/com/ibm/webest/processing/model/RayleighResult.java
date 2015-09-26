package com.ibm.webest.processing.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * RayleighResult
 * Represents the array which is needed
 * for drawing the rayleighcurves
 * @author David Dornseifer
 *
 */
public class RayleighResult implements Serializable{
	private static final long serialVersionUID = 1L;
	
	//define the storage data type for holding the values 
	private List<List<Double>> xValues = new ArrayList<List<Double>>();
	private List<List<Double>> yValues = new ArrayList<List<Double>>();
 	private List<String> xAxis = new ArrayList<String>();
	
	/**
	 * RayleighResult constructor
	 * Represents the Rayleigh calculation result values
	 * @param yVal
	 * @param xVal
	 * @param xAx
	 */
	public RayleighResult(List<List<Double>> yVal, List<List<Double>> xVal, List<String> xAx) {
		this.yValues = yVal;
		this.xValues = xVal;
		this.xAxis= xAx;
		}
	
	//no arg default constructor
	public RayleighResult() {
		
	}
	/**
	 * Returns just the x values 
	 * @return xValues
	 */
	public List<List<Double>> getxValues() {
		return this.xValues;
	}
	
	/**
	 * Returns just the y values
	 * @return yValues
	 */
	public List<List<Double>> getyValues() {
		return this.yValues;
	}
	
	/**
	 * Returns the x string
	 * @return xAxis
	 */
	public List<String> getxAxis() {
		return this.xAxis;
	}
	

	
}
