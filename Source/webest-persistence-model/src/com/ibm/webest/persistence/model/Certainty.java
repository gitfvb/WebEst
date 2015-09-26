package com.ibm.webest.persistence.model;

import java.io.Serializable;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

/**
 * Entity implementation class for Entity: Certanity
 * 
 * @author Dirk Kohlweyer
 */
@Entity
public class Certainty implements Serializable {

	@Id
	@Column(length = 1, columnDefinition = "char(1)")
	@Pattern(regexp = "[A-Z]{1}")
	private String id;

	@Column(length = 50, nullable = false)
	private String name;

	@Column(nullable = false)
	@Min(0)
	@Max(1)
	private double factor;

	private static final long serialVersionUID = 1L;

	/**
	 * @return the certainty's id
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * Sets the cartainty's id.
	 * 
	 * @param certaintyId
	 *            factor certainty id
	 */
	public void setId(String certaintyId) {
		this.id = certaintyId;
	}

	/**
	 * @return the certainty's name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Sets the cartainty's name.
	 * 
	 * @param name
	 *            factor certainty name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the certainty's factor
	 */
	public double getFactor() {
		return this.factor;
	}

	/**
	 * Sets the cartainty's factor.
	 * 
	 * @param factor
	 *            certainty factor
	 */
	public void setFactor(double factor) {
		this.factor = factor;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Certainty))
			return false;
		Certainty other = (Certainty) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
