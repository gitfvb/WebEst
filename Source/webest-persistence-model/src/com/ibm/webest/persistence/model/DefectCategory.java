package com.ibm.webest.persistence.model;

import java.io.Serializable;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Entity implementation class for Entity: DefectCategory<br>
 * Primary key is id and owner.
 * 
 * @author Gregor Schumm
 */
@Entity
public class DefectCategory implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@ManyToOne
	@JoinColumn(name = "owner")
	private TemplateValues owner;
	@Column(nullable = false)
	private boolean included;
	@Size(max = 50)
	@Column(length = 50, nullable = true)
	private String name;
	@Column(nullable = true, columnDefinition = "smallint")
	@Min(0)
	@Max(100)
	private Byte percentage;
	private static final long serialVersionUID = 1L;

	public DefectCategory() {
		super();
	}

	/**
	 * Part of the identifier.<br>
	 * Primary Key is id and owner.
	 * 
	 * @see DefectCategory#getOwner()
	 * @return the internal identifier
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Part of the identifier.<br>
	 * Primary Key is id and owner.
	 * 
	 * @see DefectCategory#setOwner(TemplateValues)
	 * @param id
	 *            the internal identifier
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Owner is Template or ProjectEnvironment.<br>
	 * Part of the identifier.<br>
	 * Primary Key is id and owner.
	 * 
	 * @see DefectCategory#getId()
	 * @return the owner of the defect category
	 */
	@XmlTransient
	public TemplateValues getOwner() {
		return this.owner;
	}

	/**
	 * Owner is Template or ProjectEnvironment.<br>
	 * Part of the identifier.<br>
	 * Primary Key is id and owner.
	 * 
	 * @see DefectCategory#setId(int)
	 * @param owner
	 *            the owner of the defect category
	 */
	public void setOwner(TemplateValues owner) {
		this.owner = owner;
	}

	/**
	 * Indicates, if the defect category should be included into the project.
	 * 
	 * @return true, if the defect category is included
	 */
	public boolean isIncluded() {
		return this.included;
	}

	/**
	 * Indicates, if the defect category should be included into the project.
	 * 
	 * @param include
	 *            true, if the defect category should be included
	 */
	public void setIncluded(boolean include) {
		this.included = include;
	}

	/**
	 * @return the name of the defect category
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @param name
	 *            the name of the defect category
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Has to be between 0 and 100 or null.
	 * 
	 * @return percentage of the defect category
	 */
	public Byte getPercentage() {
		return this.percentage;
	}

	/**
	 * Has to be between 0 and 100 or null.
	 * 
	 * @param percentage
	 *            percentage of the defect category
	 */
	public void setPercentage(Byte percentage) {
		this.percentage = percentage;
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
		if (!(obj instanceof DefectCategory))
			return false;
		DefectCategory other = (DefectCategory) obj;
		if (id != other.id)
			return false;
		return true;
	}

	/**
	 * uses the original Object.equals
	 * 
	 * @param obj
	 *            the object to check
	 * @return true, if the objects are really equal. false, if objects not
	 *         equal
	 */
	public boolean equalsObject(Object obj) {
		return super.equals(obj);
	}

}
