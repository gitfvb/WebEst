package com.ibm.webest.persistence.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.openjpa.persistence.jdbc.ForeignKey;
import org.apache.openjpa.persistence.jdbc.ForeignKeyAction;

/**
 * Entity implementation class for Entity: Milestone<br>
 * A project can define different milestone.<br>
 * Primary key is id and owner.
 * 
 * @author Gregor Schumm
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "milestoneId",
		"owner" }))
public class Milestone implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(nullable = false)
	private byte milestoneId;

	@ManyToOne(optional = false)
	@JoinColumn(name = "owner", nullable = false)
	private TemplateValues owner;
	@NotNull
	@Size(min = 1, max = 10)
	@Column(length = 10, nullable = false)
	private String acronym;
	@Column(length = 50, nullable = false)
	@NotNull
	@Size(min = 1, max = 50)
	private String name;
	@Size(max = 4000)
	@Column(length = 4000, nullable = true)
	private String description;
	@Column(nullable = false)
	@Min(0)
	@Max(100)
	private byte percentage;
	@NotNull
	@ManyToOne(cascade = CascadeType.MERGE, optional = false)
	@JoinColumn(name = "phase", referencedColumnName = "id")
	@ForeignKey(deleteAction = ForeignKeyAction.NULL)
	private Phase phase;
	private boolean included;
	private static final long serialVersionUID = 1L;

	public Milestone() {
		super();
	}

	/**
	 * The identifier is generated if omitted.
	 * 
	 * @return the unique identifier for the object
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * @param id
	 *            the unique identifier for the object
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Required and may have max. 10 characters.
	 * 
	 * @return an acronym for the milestone
	 */
	public String getAcronym() {
		return this.acronym;
	}

	/**
	 * Required and may have max. 10 characters.
	 * 
	 * @param acronym
	 *            an acronym for the milestone
	 */
	public void setAcronym(String acronym) {
		this.acronym = acronym;
	}

	/**
	 * Required and may have max. 50 characters.
	 * 
	 * @return a name for the milestone
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Required and may have max. 50 characters.
	 * 
	 * @param name
	 *            a name for the milestone
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * May have max. 4000 characters.
	 * 
	 * @return a description for the milestone
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * May have max. 4000 characters.
	 * 
	 * @param description
	 *            a description for the milestone
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Has to be between 0 and 100.
	 * 
	 * @return the percentage of this milestone in the project
	 */
	public byte getPercentage() {
		return this.percentage;
	}

	/**
	 * Has to be between 0 and 100.
	 * 
	 * @param percentage
	 *            the percentage of this milestone in the project
	 */
	public void setPercentage(byte percentage) {
		this.percentage = percentage;
	}

	/**
	 * @return the phase to which the milestones belongs
	 */
	public Phase getPhase() {
		return this.phase;
	}

	/**
	 * @param phase
	 *            the phase to which the milestones belongs
	 */
	public void setPhase(Phase phase) {
		this.phase = phase;
	}

	/**
	 * Owner is Template or ProjectEnvironment.<br>
	 * Part of the identifier.<br>
	 * Primary Key is id and owner.
	 * 
	 * @see Milestone#getId()
	 * @param owner
	 *            the owner of the milestone
	 */
	public void setOwner(TemplateValues owner) {
		this.owner = owner;
	}

	/**
	 * Owner is Template or ProjectEnvironment.<br>
	 * Part of the identifier.<br>
	 * Primary Key is id and owner.
	 * 
	 * @see Milestone#setId(int)
	 * 
	 * @return the owner of the milestone
	 */
	@XmlTransient
	public TemplateValues getOwner() {
		return owner;
	}

	/**
	 * @return true, if the milestone is included in the project
	 */
	public boolean isIncluded() {
		return included;
	}

	/**
	 * @param included
	 *            true, if the milestone should be included in the project
	 */
	public void setIncluded(boolean included) {
		this.included = included;
	}

	/**
	 * User defined identifier for the milestone.<br>
	 * Has to be unique amongst all milestones belonging to one owner.
	 * 
	 * @param milestoneId
	 *            internal identifier for the milestone
	 */
	public void setMilestoneId(byte milestoneId) {
		this.milestoneId = milestoneId;
	}

	/**
	 * User defined identifier for the milestone.<br>
	 * Has to be unique amongst all milestones belonging to one owner.
	 * 
	 * @return internal identifier for the milestone
	 */
	public byte getMilestoneId() {
		return milestoneId;
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
		if (!(obj instanceof Milestone))
			return false;
		Milestone other = (Milestone) obj;
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

	@Override
	public String toString() {
		// because of OpenJPA bug we can not access local fields here
		return "Milestone@" + super.hashCode();
	}

}
