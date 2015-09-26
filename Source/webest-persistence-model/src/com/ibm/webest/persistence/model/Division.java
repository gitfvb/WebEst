package com.ibm.webest.persistence.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Entity implementation class for Entity: Division
 * 
 * @author Dirk Kohlweyer
 */

@Entity
public class Division implements Serializable, TreeItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ManyToOne
	@JoinColumn(name = "organization", referencedColumnName = "id")
	private Organization organization;

	@Column(length = 50, nullable = false, unique = true)
	private String name;

	@OneToMany(mappedBy = "division")
	@OrderBy("lastName, firstName")
	private List<User> users;

	@OneToMany(mappedBy = "division")
	private List<Estimate> estimates;

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
	 * @return the division's name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Sets the division's name.
	 * 
	 * @param name
	 *            the division's name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the organization this division belongs to
	 */
	public Organization getOrganization() {
		return this.organization;
	}

	/**
	 * Assigns this division to an organization.
	 * 
	 * @param organization
	 *            the organization
	 */
	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	/**
	 * @return a list of all users belonging to this division
	 */
	@XmlTransient
	public List<User> getUsers() {
		return this.users;
	}

	/**
	 * Assigns a list of users to this division.
	 * 
	 * @param users
	 *            a list of users
	 */
	public void setUsers(List<User> users) {
		this.users = users;
	}

	/**
	 * @return a list of all estimates belonging to this division
	 */
	@XmlTransient
	public List<Estimate> getEstimates() {
		return this.estimates;
	}

	/**
	 * Assigns a list of estimates to this division.
	 * 
	 * @param estimates
	 *            a list of estimates
	 */
	public void setEstimates(List<Estimate> estimates) {
		this.estimates = estimates;
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
		if (!(obj instanceof Division))
			return false;
		Division other = (Division) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public Collection<? extends TreeItem> getChildren() {
		return null;
	}

}
