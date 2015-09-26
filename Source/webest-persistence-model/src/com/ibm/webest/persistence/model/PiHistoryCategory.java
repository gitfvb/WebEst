package com.ibm.webest.persistence.model;

import java.io.Serializable;

import java.util.Collection;
import java.util.List;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: PiHistoryCategory
 * 
 * @author Dirk Kohlweyer
 */
@Entity
public class PiHistoryCategory implements Serializable, TreeItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(length = 50, nullable = false, unique = true)
	private String name;

	@OneToMany(mappedBy = "category", fetch = FetchType.EAGER)
	private List<PiHistoryEntry> piHistoryEntries;

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
	 * @return the category's name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Sets the category's name.
	 * 
	 * @param name
	 *            the categorys's name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return a list of pi history entries belonging to this category
	 */
	public List<PiHistoryEntry> getPiHistoryEntries() {
		return this.piHistoryEntries;
	}

	/**
	 * Assigns a list of PI history entries to this category.
	 * 
	 * @param piHistoryEntries
	 *            a list of PI history entries
	 */
	public void setPiHistoryEntries(List<PiHistoryEntry> piHistoryEntries) {
		this.piHistoryEntries = piHistoryEntries;
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
		if (!(obj instanceof PiHistoryCategory))
			return false;
		PiHistoryCategory other = (PiHistoryCategory) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public Collection<? extends TreeItem> getChildren() {
		return getPiHistoryEntries();
	}

}
