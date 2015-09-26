package com.ibm.webest.persistence.model;

import java.io.Serializable;

import javax.persistence.*;
import javax.validation.constraints.Min;

/**
 * Entity implementation class for Entity: MeasuringUnit<br>
 * Abstract entity for different measuring units.
 * 
 * @see MonetaryUnit
 * @see MTTDTimeUnit
 * @see EffortUnit
 * @author Gregor Schumm
 */
@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class MeasuringUnit implements Serializable {
	@Id
	@Column(length = 10)
	private String id;
	@Column(length = 50, nullable = false, unique = true)
	private String name;
	@Column(nullable = false)
	private boolean reference;
	@Column(nullable = false)
	@Min(0)
	private double factor;
	private static final long serialVersionUID = 1L;

	public MeasuringUnit() {
		super();
	}

	/**
	 * Should be an acronym.
	 * 
	 * @return identifier for the unit
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * Should be an acronym.
	 * 
	 * @param id
	 *            identifier for the unit
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Indicates, if the unit is the reference (base) unit.<br>
	 * Factors of other units have to be relative to the reference unit.<br>
	 * Factor of the reference unit should be 1.<br>
	 * Only one unit of the same type is the reference unit.
	 * 
	 * @see MeasuringUnit#getFactor()
	 * @return true, if unit is the reference unit
	 */
	public boolean isReference() {
		return this.reference;
	}

	/**
	 * Indicates, if the unit is the reference (base) unit.<br>
	 * Factors of other units have to be relative to the reference unit.<br>
	 * Factor of the reference unit should be 1.<br>
	 * Only one unit of the same type is the reference unit.
	 * 
	 * @see MeasuringUnit#setFactor(double)
	 * @param reference
	 *            true, if unit is the reference unit
	 */
	public void setReference(boolean reference) {
		this.reference = reference;
	}

	/**
	 * Factor relative to the reference unit.<br>
	 * Factor of the reference unit should be 1.<br>
	 * 
	 * @return the relative factor
	 */
	public double getFactor() {
		return this.factor;
	}

	/**
	 * Factor relative to the reference unit.<br>
	 * Factor of the reference unit should be 1.<br>
	 * 
	 * @param factor
	 *            the relative factor
	 */
	public void setFactor(double factor) {
		this.factor = factor;
	}

	/**
	 * Required and unique.
	 * 
	 * @param name
	 *            human readable name of the unit
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Required and unique.
	 * 
	 * @return human readable name of the unit
	 */
	public String getName() {
		return name;
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
		if (!(obj instanceof MeasuringUnit))
			return false;
		MeasuringUnit other = (MeasuringUnit) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
