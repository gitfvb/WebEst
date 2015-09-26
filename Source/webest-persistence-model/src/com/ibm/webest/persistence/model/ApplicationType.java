package com.ibm.webest.persistence.model;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Entity implementation class for Entity: ApplicationType<br>
 * Application types are grouped by application type groups.
 * 
 * @see ApplicationTypeGroup
 * 
 * @author Gregor Schumm
 */
@Entity
public class ApplicationType implements Serializable, TreeItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(length = 50, nullable = false)
	private String name;
	@ManyToOne
	@JoinColumn(name = "\"GROUP\"", nullable = false, referencedColumnName = "id")
	private ApplicationTypeGroup group;
	private static final long serialVersionUID = 1L;

	public ApplicationType() {
		super();
	}

	/**
	 * Gets the internal numeric identifier.<br>
	 * Automatically generated if omitted.
	 * 
	 * @return the identifier of the object
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Sets the internal numeric identifier.<br>
	 * Automatically generated if omitted.
	 * 
	 * @param id
	 *            the identifier of the object
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the name of the application type
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @param name
	 *            the name of the application type
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param group
	 *            the corresponding application type group
	 */
	public void setGroup(ApplicationTypeGroup group) {
		this.group = group;
	}

	/**
	 * @return the corresponding application type group
	 */
	public ApplicationTypeGroup getGroup() {
		return group;
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
		if (!(obj instanceof ApplicationType))
			return false;
		ApplicationType other = (ApplicationType) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public Collection<? extends TreeItem> getChildren() {
		return null;
	}

}
