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
 * Entity implementation class for Entity: IndustrySector<br>
 * Industry sectors are grouped by industry sector groups.
 * 
 * @see IndustrySectorGroup
 * @author Gregor Schumm
 */
@Entity
public class IndustrySector implements Serializable, TreeItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(length = 50, nullable = false)
	private String name;
	@ManyToOne
	@JoinColumn(name = "\"GROUP\"", nullable = false, referencedColumnName = "id")
	private IndustrySectorGroup group;
	private static final long serialVersionUID = 1L;

	public IndustrySector() {
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
	 * @return the name of the industry sector
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @param name
	 *            the name of the industry sector
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the corresponding industry sector group
	 */
	public IndustrySectorGroup getGroup() {
		return this.group;
	}

	/**
	 * @param group
	 *            the corresponding industry sector group
	 */
	public void setGroup(IndustrySectorGroup group) {
		this.group = group;
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
		if (!(obj instanceof IndustrySector))
			return false;
		IndustrySector other = (IndustrySector) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public Collection<? extends TreeItem> getChildren() {
		return null;
	}

}
