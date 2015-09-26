package com.ibm.webest.persistence.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Entity implementation class for Entity: ApplicationTypeGroup<br>
 * Groups application type entities.
 * 
 * @see ApplicationType
 * @author Gregor Schumm
 */
@Entity
public class ApplicationTypeGroup implements Serializable, TreeItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(length = 50, unique = true, nullable = false)
	private String name;
	@OneToMany(mappedBy = "group", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@OrderBy("name")
	private List<ApplicationType> applicationTypes;
	private static final long serialVersionUID = 1L;

	public ApplicationTypeGroup() {
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
	 * Gets the internal numeric identifier.<br>
	 * Automatically generated if omitted.
	 * 
	 * @param id
	 *            the identifier of the object
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the name of the application type group
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @param name
	 *            the name of the application type group
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return name ordered list of corresponding application types
	 */
	@XmlTransient
	public List<ApplicationType> getApplicationTypes() {
		return this.applicationTypes;
	}

	/**
	 * @param applicationTypes
	 *            list of corresponding application types
	 */
	public void setApplicationTypes(List<ApplicationType> applicationTypes) {
		this.applicationTypes = applicationTypes;
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
		if (!(obj instanceof ApplicationTypeGroup))
			return false;
		ApplicationTypeGroup other = (ApplicationTypeGroup) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public Collection<? extends TreeItem> getChildren() {
		return getApplicationTypes();
	}

}
