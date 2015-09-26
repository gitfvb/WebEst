package com.ibm.webest.persistence.model;

import java.io.Serializable;

import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Entity implementation class for Entity: Phase<br>
 * A project can define different phases.<br>
 * Primary key is number and owner.
 * 
 * @author Gregor Schumm
 * @author Wail Shakir
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "number", "owner" }))
public class Phase implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Min(1)
	@Max(4)
	private byte number;
	@ManyToOne
	@JoinColumn(name = "owner")
	private TemplateValues owner;
	@OneToMany(mappedBy = "phase")
	private List<Milestone> milestones;

	@NotNull
	@Size(min = 1, max = 10)
	@Column(length = 10, nullable = false)
	private String acronym;
	@NotNull
	@Size(min = 1, max = 50)
	@Column(length = 50, nullable = false)
	private String name;
	@Size(max = 4000)
	@Column(length = 4000, nullable = true)
	private String description;
	@NotNull
	@Column(nullable = false)
	@Min(0)
	@Max(100)
	private Byte percentage;

	private static final long serialVersionUID = 1L;

	public Phase() {
		super();
	}

	/**
	 * Part of the identifier.<br>
	 * Primary Key is number and owner.<br>
	 * Phase number has to be between 1 and 4 and has to be unique in one
	 * project or template.
	 * 
	 * @see Phase#getOwner()
	 * @return the phase number
	 */
	public byte getNumber() {
		return this.number;
	}

	/**
	 * Part of the identifier.<br>
	 * Primary Key is number and owner.<br>
	 * Phase number has to be between 1 and 4 and has to be unique in one
	 * project or template.
	 * 
	 * @see Phase#setOwner(TemplateValues)
	 * @param number
	 *            the phase number
	 */
	public void setNumber(byte number) {
		this.number = number;
	}

	/**
	 * Required and may have max. 10 characters.
	 * 
	 * @return an acronym for the phase
	 */
	public String getAcronym() {
		return this.acronym;
	}

	/**
	 * Required and may have max. 10 characters.
	 * 
	 * @param acronym
	 *            an acronym for the phase
	 */
	public void setAcronym(String acronym) {
		this.acronym = acronym;
	}

	/**
	 * Required and may have max. 50 characters.
	 * 
	 * @return a name for the phase
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Required and may have max. 50 characters.
	 * 
	 * @param name
	 *            a name for the phase
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * May have max. 4000 characters.
	 * 
	 * @return a description for the phase
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * May have max. 4000 characters.
	 * 
	 * @param description
	 *            a description for the phase
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Owner is Template or ProjectEnvironment.<br>
	 * Part of the identifier.<br>
	 * Primary Key is number and owner.
	 * 
	 * @see Phase#setId(int)
	 * @param owner
	 *            the owner of the phase
	 */
	public void setOwner(TemplateValues owner) {
		this.owner = owner;
	}

	/**
	 * Owner is Template or ProjectEnvironment.<br>
	 * Part of the identifier.<br>
	 * Primary Key is id and owner.
	 * 
	 * @see Phase#getId()
	 * 
	 * @return the owner of the phase
	 */
	@XmlTransient
	public TemplateValues getOwner() {
		return owner;
	}

	/**
	 * @param id
	 *            the unique identifier for the phase
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the unique identifier for the phase
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param percentage
	 *            the percentage of this phase to the project
	 */
	public void setPercentage(Byte percentage) {
		this.percentage = percentage;
	}

	/**
	 * @return the percentage of this phase to the project
	 */
	public Byte getPercentage() {
		return percentage;
	}

	@XmlTransient
	public List<Milestone> getMilestones() {
		return milestones;
	}

	/**
	 * @param milestones
	 *            the milestones for this phase
	 */
	public void setMilestones(List<Milestone> milestones) {
		this.milestones = milestones;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		if (id == 0) {
			result = prime * result + number;
			result = prime * result + ((owner == null) ? 0 : owner.hashCode());
		} else {
			result = prime * result + id;
		}
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Phase))
			return false;
		Phase other = (Phase) obj;
		if (id == 0 && other.id == 0) {
			if (number != other.number)
				return false;
			if (owner == null) {
				if (other.owner != null)
					return false;
			} else if (!owner.equals(other.owner))
				return false;
		}

		if (id != other.id)
			return false;
		return true;
	}

}
