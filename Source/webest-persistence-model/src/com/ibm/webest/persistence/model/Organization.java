package com.ibm.webest.persistence.model;

import java.io.Serializable;

import java.util.Collection;
import java.util.List;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Entity implementation class for Entity: Organization
 * 
 * @author Dirk Kohlweyer
 */
@Entity
public class Organization implements Serializable, TreeItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(length = 50, nullable = false, unique = true)
	private String name;

	@OrderBy("name")
	@OneToMany(mappedBy = "organization", fetch = FetchType.EAGER)
	private List<Division> divisions;

	private static final long serialVersionUID = 1L;

	/**
	 * ID will be automatically generated when committed.
	 * 
	 * @return The identifier (Primary Key) for this object
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * @return the organization's name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Sets the organization's name.
	 * 
	 * @param name
	 *            the organization's name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return a list of all divisions belonging to this organization
	 */
	@XmlTransient
	public List<Division> getDivisions() {
		return this.divisions;
	}

	/**
	 * Assigns a list of divisions to this organization.
	 * 
	 * @param divisions
	 *            a set of divisions
	 */
	public void setDivisions(List<Division> divisions) {
		this.divisions = divisions;
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
		if (!(obj instanceof Organization))
			return false;
		Organization other = (Organization) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public Collection<? extends TreeItem> getChildren() {
		return getDivisions();
	}

}
