package com.ibm.webest.persistence.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Pattern;

/**
 * Entity implementation class for Entity: UseCaseComplexity
 * 
 * @author Dirk Kohlweyer
 */
@Entity
public class UseCaseComplexity implements Serializable {

	@Id
	@Column(length = 3)
	@Pattern(regexp = "[A-Z]{1,3}")
	private String id;

	@Column(length = 50, nullable = false)
	private String name;

	@Column(nullable = false)
	private double factor;

	private static final long serialVersionUID = 1L;

	/**
	 * @return the use case comlexity's id
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * Sets the use case comlexity's id.
	 * 
	 * @param id
	 *            factor use case complexity id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the use case comlexity's name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Sets the use case comlexity's name.
	 * 
	 * @param name
	 *            factor use case complexity name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the use case complexity's factor
	 */
	public double getFactor() {
		return this.factor;
	}

	/**
	 * Sets the use case complexity's factor.
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
		if (!(obj instanceof UseCaseComplexity))
			return false;
		UseCaseComplexity other = (UseCaseComplexity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
