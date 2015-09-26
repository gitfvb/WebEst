package com.ibm.webest.gui.utils;

import java.util.Locale;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import com.ibm.webest.persistence.model.MeasuringUnit;

/**
 * JSF converter to convert numeric values from one MeasuringUnit to another.
 * @author Gregor Schumm
 *
 */
public class UnitConverter implements Converter {
	public static final String CONVERTER_ID = "com.ibm.webest.gui.utils.UnitConverter";
	private MeasuringUnit sourceUnitInstance;
	private MeasuringUnit targetUnitInstance;
	private String sourceUnit;
	private String targetUnit;
	private int precision = 2;

	/**
	 * Can be used as dynamic alternative to {@link #getSourceUnitInstance()}.
	 * @return the EL expression where the source unit can be retrieved from (expression has to evaluate to a MeasuringUnit instance) 
	 */
	public String getSourceUnit() {
		return sourceUnit;
	}

	/**
	 * Can be used as dynamic alternative to {@link #setSourceUnitInstance(MeasuringUnit)}.
	 * @param sourceUnit the EL expression where the source unit can be retrieved from (expression has to evaluate to a MeasuringUnit instance) 
	 */
	public void setSourceUnit(String sourceUnit) {
		this.sourceUnit = sourceUnit;
	}
	
	/**
	 * Can be used as dynamic alternative to {@link #getTargetUnitInstance()}.
	 * @return the EL expression where the target unit can be retrieved from (expression has to evaluate to a MeasuringUnit instance) 
	 */
	public String getTargetUnit() {
		return targetUnit;
	}

	/**
	 * Can be used as dynamic alternative to {@link #setTargetUnitInstance(MeasuringUnit)}.
	 * @param targetUnit the EL expression where the target unit can be retrieved from (expression has to evaluate to a MeasuringUnit instance) 
	 */
	public void setTargetUnit(String targetUnit) {
		this.targetUnit = targetUnit;
	}

	/**
	 * Can be used as static alternative to {@link #setTargetUnit(String)}.
	 * @param targetUnitInstance an instance of the target unit 
	 */
	public void setTargetUnitInstance(MeasuringUnit targetUnitInstance) {
		this.targetUnitInstance = targetUnitInstance;
	}

	/**
	 * Can be used as static alternative to {@link #getTargetUnit()}.
	 * @return an instance of the target unit 
	 */
	public MeasuringUnit getTargetUnitInstance() {
		if (targetUnitInstance == null && targetUnit != null)
			return ManagedBeanUtils.evaluateEl(targetUnit);
		return targetUnitInstance;
	}

	/**
	 * Can be used as static alternative to {@link #setSourceUnit(String)}.
	 * @param sourceUnitInstance an instance of the source unit 
	 */
	public void setSourceUnitInstance(MeasuringUnit sourceUnitInstance) {
		this.sourceUnitInstance = sourceUnitInstance;
	}

	/**
	 * Can be used as static alternative to {@link #getSourceUnit()}.
	 * @return an instance of the source unit 
	 */
	public MeasuringUnit getSourceUnitInstance() {
		if (sourceUnitInstance == null && sourceUnit != null)
			return ManagedBeanUtils.evaluateEl(sourceUnit);
		return sourceUnitInstance;
	}

	/**
	 * @param precision numbers of digits after decimal point in the output string
	 */
	public void setPrecision(int precision) {
		this.precision = precision;
	}
	
	/**
	 * @return numbers of digits after decimal point in the output string
	 */
	public int getPrecision() {
		return precision;
	}

	/**
	 * Converts a string representation of a decimal number from target unit to source unit.
	 * @return the converted (double) value in source units
	 * @param arg0 the faces context
	 * @param arg1 the corresponding component
	 * @param arg2 the string to convert
	 */
	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2)
			throws ConverterException {
		try {
			double v = Double.parseDouble(arg2);
			v /= getTargetUnitInstance().getFactor();
			v *= getSourceUnitInstance().getFactor();
			return v;
		} catch (NumberFormatException e) {
			throw new ConverterException(e);
		}
	}

	/**
	 * Converts a Number from source unit to target unit.
	 * @return a string representation of the converted value in target units
	 * @param arg0 the faces context
	 * @param arg1 the corresponding component
	 * @param arg2 the number to convert
	 */
	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2)
			throws ConverterException {
		if (arg2 instanceof Number) {
			double value = ((Number) arg2).doubleValue();
			value /= getSourceUnitInstance().getFactor();
			value *= getTargetUnitInstance().getFactor();
			return String.format(Locale.US, "%,."+precision+"f", value);
		}
		throw new ConverterException("Value has to be a number.");
	}
}
