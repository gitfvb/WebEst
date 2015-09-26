package com.ibm.webest.persistence.model;

import java.io.Serializable;

import java.util.Collection;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: piHistoryEntry
 * 
 * @author Dirk Kohlweyer
 */
@Entity
@SecondaryTable(name = "PiPpLookup", pkJoinColumns = @PrimaryKeyJoinColumn(name = "pi", referencedColumnName = "pi"))
public class PiHistoryEntry implements Serializable, TreeItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(length = 50, nullable = false)
	private String projectName;

	@ManyToOne
	@JoinColumn(name = "category", nullable = false)
	private PiHistoryCategory category;

	@Column(nullable = false)
	private int pi;

	@Column(updatable = false, nullable = true, table = "PiPpLookup")
	private Integer pp;

	@Column(nullable = false)
	private double effort;

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
	 * @return the entry's project name
	 */
	public String getProjectName() {
		return this.projectName;
	}

	/**
	 * Sets the entry's project name.
	 * 
	 * @param projectName
	 *            project name
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	/**
	 * @return the entry's category
	 */
	public PiHistoryCategory getCategory() {
		return this.category;
	}

	/**
	 * Sets the entry's category.
	 * 
	 * @param category
	 *            category
	 */
	public void setCategory(PiHistoryCategory category) {
		this.category = category;
	}

	/**
	 * @return the entry's PI Value
	 */
	public int getPi() {
		return this.pi;
	}

	/**
	 * Sets the entry's PI value.
	 * 
	 * @param pi
	 *            PI value
	 */
	public void setPi(int pi) {
		this.pi = pi;
	}

	/**
	 * @return the entry's effort
	 */
	public double getEffort() {
		return this.effort;
	}

	/**
	 * Sets the entry's effort.
	 * 
	 * @param effort
	 *            effort
	 */
	public void setEffort(double effort) {
		this.effort = effort;
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
		if (!(obj instanceof PiHistoryEntry))
			return false;
		PiHistoryEntry other = (PiHistoryEntry) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public Collection<? extends TreeItem> getChildren() {
		return null;
	}

	@Override
	public String getName() {
		return getProjectName();
	}

	/**
	 * Returns the PP value for the PI.<br>
	 * The PP is looked up in the PiPpLookup Table.
	 * 
	 * @return the PP or null if no corresponding PP found
	 */
	public Integer getPp() {
		return pp;
	}

}
