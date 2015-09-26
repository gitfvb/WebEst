package com.ibm.webest.persistence.model;

import java.io.Serializable;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: StaffingShape<br>
 * 
 * @author Wail Shakir
 */
@Entity
public class StaffingShape implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private byte id;

	@Column(length = 50, nullable = false, unique = true)
	private String name;

	public StaffingShape() {
		super();
	}

	/**
	 * @return the identification of the staffing shape
	 */
	public byte getId() {
		return id;
	}

	/**
	 * Required and may have max. 50 characters.
	 * 
	 * @param name
	 *            is the name of the staffing shape
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Required and may have max. 50 characters.
	 * 
	 * @return the name of the staffing shape
	 */
	public String getName() {
		return name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof StaffingShape))
			return false;
		StaffingShape other = (StaffingShape) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
