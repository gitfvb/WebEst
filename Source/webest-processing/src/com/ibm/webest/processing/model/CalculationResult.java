package com.ibm.webest.processing.model;

import java.io.Serializable;

/**
 * Abstract class of a result from one of the used models
 * 
 * @author Andre Munzinger
 */
public abstract class CalculationResult implements Serializable {

	private static final long serialVersionUID = 1L;
	final static int CONVERSION_FLAG_NONE = 0;
	final static int CONVERSION_FLAG_MONTH = 1;
	final static int CONVERSION_FLAG_YEAR = 2;

	private Float effort = null;

	// None = 0; Month = 1; Year = 2
	private Integer effortFormat = -1;

	/**
	 * setEffort
	 * 
	 * @param effort
	 *            Array with length 3 with the effort for all three available
	 *            categories
	 */
	protected void setEffort(Float effort) {
		setEffort(effort, 0);
	}

	/**
	 * setEffort
	 * 
	 * @param effort
	 *            Array with length 3 with the effort for all three available
	 *            categories
	 * @param conversionFlag
	 *            useful if effort should be converted (see attribute above) 0 =
	 *            do nothing; 1 = convert to month; 2 = convert to year
	 */
	protected void setEffort(Float effort, int conversionFlag) {

		// Save the effort in the wanted unit
		switch (conversionFlag) {
		case CONVERSION_FLAG_NONE:
			this.effort = effort;
			break;
		case CONVERSION_FLAG_MONTH:
			this.effort = com.ibm.webest.processing.helpers.Conversion
					.convertToMonth(effort);
			break;
		case CONVERSION_FLAG_YEAR:
			this.effort = com.ibm.webest.processing.helpers.Conversion
					.convertToYear(effort);
			break;
		}

		// Setting the conversiontype
		setConversionFlag(conversionFlag);
	}

	/**
	 * getEffort Returns the content of the effort
	 * 
	 * @return Effort
	 */
	public Float getEffort() {
		return this.effort;
	}

	/**
	 * setConversionFlag Writes the current format of the result in the flag
	 * 
	 * @param value
	 *            See ENUMS in Classheader
	 */
	protected void setConversionFlag(int value) {
		this.effortFormat = value;
	}

	/**
	 * getConversionFlag
	 * 
	 * @return the Format of the result (month, year, ...)
	 */
	public int getConversionFlag() {
		return this.effortFormat;
	}
}