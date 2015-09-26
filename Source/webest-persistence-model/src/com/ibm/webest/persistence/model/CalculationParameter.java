package com.ibm.webest.persistence.model;

import java.io.Serializable;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: CalculationParameter<br>
 * Represents a parameter for calculation purpose. It provides the possibility
 * to store calculation constants in the database.
 * 
 * @author Gregor Schumm
 */
@Entity
public class CalculationParameter implements Serializable {
	@Id
	@Column(name = "\"KEY\"", length = 50)
	private String key;
	@Column(name = "\"VALUE\"")
	private double value;
	private static final long serialVersionUID = 1L;

	public CalculationParameter() {
		super();
	}

	/**
	 * @return the unique alphanumeric identifier of the parameter
	 */
	public String getKey() {
		return this.key;
	}

	/**
	 * @param key
	 *            the unique alphanumeric identifier of the parameter
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return the parameter value
	 */
	public double getValue() {
		return this.value;
	}

	/**
	 * @param value
	 *            the parameter value
	 */
	public void setValue(double value) {
		this.value = value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof CalculationParameter))
			return false;
		CalculationParameter other = (CalculationParameter) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		return true;
	}

}
