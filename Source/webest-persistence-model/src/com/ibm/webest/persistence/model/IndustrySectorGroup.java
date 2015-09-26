package com.ibm.webest.persistence.model;

import java.io.Serializable;

import java.util.Collection;
import java.util.List;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Entity implementation class for Entity: IndustryGroup<br>
 * Groups industry sectors.
 * 
 * @see IndustrySector
 * @author Gregor Schumm
 */
@Entity
public class IndustrySectorGroup implements Serializable, TreeItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(length = 50, unique = true, nullable = false)
	private String name;
	@OneToMany(mappedBy = "group", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@OrderBy("name")
	private List<IndustrySector> industrySectors;
	private static final long serialVersionUID = 1L;

	public IndustrySectorGroup() {
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
	 * @return the name of the industry sector group
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @param name
	 *            the name of the industry sector group
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return name ordered list of corresponding industry sectors
	 */
	@XmlTransient
	public List<IndustrySector> getIndustrySectors() {
		return this.industrySectors;
	}

	/**
	 * @param industrySectors
	 *            list of corresponding industry sectors
	 */
	public void setIndustries(List<IndustrySector> industrySectors) {
		this.industrySectors = industrySectors;
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
		if (!(obj instanceof IndustrySectorGroup))
			return false;
		IndustrySectorGroup other = (IndustrySectorGroup) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public Collection<? extends TreeItem> getChildren() {
		return getIndustrySectors();
	}

}
