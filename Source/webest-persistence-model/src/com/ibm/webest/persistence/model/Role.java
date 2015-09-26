package com.ibm.webest.persistence.model;

import java.io.Serializable;

import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Entity implementation class for Entity: Role Each user is assigned to a role.
 * A Role defines the users access rights on the stored data.
 * 
 * @author Dirk Kohlweyer
 */
@Entity
public class Role implements Serializable {

	@Id
	@Column(length = 20)
	@Pattern(regexp = "[a-z0-9_]{1,20}")
	private String id;

	@Column(length = 50, nullable = false, unique = true)
	private String name;

	@OneToMany(mappedBy = "role")
	private Set<User> users;

	private static final long serialVersionUID = 1L;

	/**
	 * ID will be automatically generated when committed.
	 * 
	 * @return The identifier (Primary Key) for this object
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * @return the role's name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Sets the role's name.
	 * 
	 * @param name
	 *            the role's name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return a set of (all) users assigned to this role
	 */
	@XmlTransient
	public Set<User> getUsers() {
		return this.users;
	}

	/**
	 * Assigns a set of users to this role.
	 * 
	 * @param users
	 *            a set of users
	 */
	public void setUsers(Set<User> users) {
		this.users = users;
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
		if (!(obj instanceof Role))
			return false;
		Role other = (Role) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
