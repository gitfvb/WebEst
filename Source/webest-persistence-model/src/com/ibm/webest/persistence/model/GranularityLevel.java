package com.ibm.webest.persistence.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Entity implementation class for Entity: GranularityLevel<br>
 * 
 * @author Wail Shakir
 */
@Entity
public class GranularityLevel implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private byte id;

	@Column(length = 50, nullable = false, unique = true)
	private String name;
	@Column(name = "\"VALUE\"")
	private byte value;
	@OneToMany(mappedBy = "level")
	private List<GranularityQuestion> questions;

	public GranularityLevel() {
		super();
	}

	/**
	 * @return the identification of the granularity level
	 */
	public byte getId() {
		return id;
	}

	/**
	 * Required and may have max. 50 characters.
	 * 
	 * @return the name of the granularity level
	 */
	public String getName() {
		return name;
	}

	/**
	 * Required and may have max. 50 characters.
	 * 
	 * @param name
	 *            the name of the granularity level
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the value representing this granularity level for the calculation
	 */
	public byte getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value representing this granularity level for the
	 *            calculation
	 */
	public void setValue(byte value) {
		this.value = value;
	}

	/**
	 * @param questions
	 *            all granularity questions belonging to the granularity level
	 */
	public void setQuestions(List<GranularityQuestion> questions) {
		this.questions = questions;
	}

	/**
	 * @return all granularity questions belonging to the granularity level
	 */
	@XmlTransient
	public List<GranularityQuestion> getQuestions() {
		return questions;
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
		if (!(obj instanceof GranularityLevel))
			return false;
		GranularityLevel other = (GranularityLevel) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
