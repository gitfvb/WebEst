package com.ibm.webest.persistence.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.openjpa.persistence.jdbc.ForeignKey;

/**
 * Entity implementation class for Entity: User
 * 
 * @author Dirk Kohlweyer
 */
@Entity
@Table(name = "\"USER\"")
public class User implements Serializable {

	@Id
	@Column(length = 50, nullable = false)
	@Pattern(regexp = "[a-z0-9@_.-]{1,50}")
	private String id;

	@Column(length = 32, nullable = false)
	@Pattern(regexp = "[a-fA-F0-9]{32}")
	private transient String password;

	@Column(length = 50, nullable = false)
	private String firstName;

	@Column(length = 50, nullable = false)
	private String lastName;

	@ManyToOne
	@JoinColumn(name = "role", nullable = true)
	@ForeignKey
	private Role role;

	@ManyToOne
	@JoinColumn(name = "division", nullable = false, referencedColumnName = "id")
	@ForeignKey
	private Division division;

	@OneToMany(mappedBy = "estimator")
	private List<Estimate> estimates;

	private static final long serialVersionUID = 1L;

	public User() {
		super();
	}

	/**
	 * @return the user's id
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * @return the user's password hashed with md5
	 */
	@XmlTransient
	public String getPassword() {
		return this.password;
	}

	/**
	 * @param password
	 *            the user's password hashed with md5
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the user's first name
	 */
	public String getFirstName() {
		return this.firstName;
	}

	/**
	 * Sets the user's first name.
	 * 
	 * @param firstName
	 *            the user's first name
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the user's last name
	 */
	public String getLastName() {
		return this.lastName;
	}

	/**
	 * Sets the user's first name.
	 * 
	 * @param lastName
	 *            the user's last name
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the user's role
	 */
	public Role getRole() {
		return role;
	}

	/**
	 * Assigns this user to a role.
	 * 
	 * @param role
	 *            The role
	 */
	public void setRole(Role role) {
		this.role = role;
	}

	/**
	 * @return the user's division
	 */
	public Division getDivision() {
		return division;
	}

	/**
	 * Assigns this user to a division.
	 * 
	 * @param division
	 *            The division
	 */
	public void setDivision(Division division) {
		this.division = division;
	}

	/**
	 * @param estimates
	 *            a list of all estimates created by this user
	 */
	public void setEstimates(List<Estimate> estimates) {
		this.estimates = estimates;
	}

	/**
	 * @return a list of all estimates created by this user
	 */
	@XmlTransient
	public List<Estimate> getEstimates() {
		return estimates;
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
		if (!(obj instanceof User))
			return false;
		User other = (User) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
