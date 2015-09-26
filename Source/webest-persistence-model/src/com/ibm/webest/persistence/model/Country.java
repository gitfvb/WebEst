package com.ibm.webest.persistence.model;

import java.io.Serializable;

import javax.persistence.*;
import javax.validation.constraints.Pattern;

/**
 * Entity implementation class for Entity: Country<br>
 * Represents a country with ISO 3166-1 country code and name.
 * 
 * @author Gregor Schumm
 */
@Entity
public class Country implements Serializable {
	@Id
	@Column(length = 2, columnDefinition = "char(2)")
	@Pattern(regexp = "[A-Z]{2}")
	private String code;
	@Column(length = 50, nullable = false, unique = true)
	private String name;
	private static final long serialVersionUID = 1L;

	public Country() {
		super();
	}

	/**
	 * @return the ISO 3166-1 (2 character upper case) country code
	 */
	public String getCode() {
		return this.code;
	}

	/**
	 * @param code
	 *            the ISO 3166-1 (2 character upper case) country code
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the user friendly country name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @param name
	 *            the user friendly country name
	 */
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Country))
			return false;
		Country other = (Country) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		return true;
	}

}
