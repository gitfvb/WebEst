package com.ibm.webest.persistence.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import org.apache.openjpa.persistence.ElementDependent;

import com.ibm.webest.persistence.validation.UniqueMilestoneIds;
import com.ibm.webest.persistence.validation.UniquePhaseNumbers;

/**
 * Entity implementation class for Entity: TemplateValues<br>
 * Container for values used in templates and project environments.<br>
 * The id is shared with Template and ProjectEnvironment entities and has to be
 * unique amongst all Template and ProjectEnvironment objects.
 * 
 * @see Template
 * @see ProjectEnvironment
 * @author Gregor Schumm
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@UniqueMilestoneIds
@UniquePhaseNumbers
public abstract class TemplateValues implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int id;
	@Column(nullable = false)
	@Min(1)
	@Max(7)
	private byte daysPerWeek;
	@Column(nullable = false)
	@Min(1)
	@Max(24)
	private byte hoursPerDay;
	@Valid
	@ElementDependent
	@OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<DefectCategory> defectCategories;
	@OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@OrderBy("milestoneId")
	@ElementDependent
	@Valid
	private List<Milestone> milestones;
	@Size(min = 1, max = 4)
	@Valid
	@OrderBy("number")
	@ElementDependent
	@OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Phase> phases;

	private static final long serialVersionUID = 1L;

	public TemplateValues() {
		super();
	}

	/**
	 * The id is shared with Template and ProjectEnvironment and has to be
	 * unique amongst all Template and ProjectEnvironment objects. <br>
	 * It is automatically generated if omitted.
	 * 
	 * @param id
	 *            the unique identifier
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * The id is shared with Template and ProjectEnvironment and has to be
	 * unique amongst all Template and ProjectEnvironment objects.<br>
	 * It is automatically generated if omitted.
	 * 
	 * @return the unique identifier
	 */
	public int getId() {
		return id;
	}

	/**
	 * Has to be between 0 and 7.
	 * 
	 * @return the work days per week
	 */
	public byte getDaysPerWeek() {
		return this.daysPerWeek;
	}

	/**
	 * Has to be between 0 and 7.
	 * 
	 * @param daysPerWeek
	 *            the work days per week
	 */
	public void setDaysPerWeek(byte daysPerWeek) {
		this.daysPerWeek = daysPerWeek;
	}

	/**
	 * Has to be between 0 and 24.
	 * 
	 * @return the working hours per day
	 */
	public byte getHoursPerDay() {
		return this.hoursPerDay;
	}

	/**
	 * Has to be between 0 and 24.
	 * 
	 * @param hoursPerDay
	 *            the working hours per day
	 */
	public void setHoursPerDay(byte hoursPerDay) {
		this.hoursPerDay = hoursPerDay;
	}

	/**
	 * @return project associated milestones ordered by id
	 */
	public List<Milestone> getMilestones() {
		return this.milestones;
	}

	/**
	 * @param milestones
	 *            project associated milestones
	 */
	public void setMilestones(List<Milestone> milestones) {
		this.milestones = milestones;
	}

	/**
	 * @return project associated phases ordered by their number
	 */
	public List<Phase> getPhases() {
		return this.phases;
	}

	/**
	 * @param phases
	 *            project associated phases
	 */
	public void setPhases(List<Phase> phases) {
		this.phases = phases;
	}

	/**
	 * @return project associated defect categories
	 */
	public List<DefectCategory> getDefectCategories() {
		return this.defectCategories;
	}

	/**
	 * @param defectCategories
	 *            project associated defect categories
	 */
	public void setDefectCategories(List<DefectCategory> defectCategories) {
		this.defectCategories = defectCategories;
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
		if (!(obj instanceof TemplateValues))
			return false;
		TemplateValues other = (TemplateValues) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TemplateValues";
	}

}
