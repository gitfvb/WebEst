package com.ibm.webest.persistence.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.openjpa.persistence.ElementDependent;
import org.apache.openjpa.persistence.jdbc.ForeignKey;
import org.apache.openjpa.persistence.jdbc.ForeignKeyAction;

import com.ibm.webest.persistence.validation.OneUseCaseInScope;
import com.ibm.webest.persistence.validation.ValidPiPpSelection;

/**
 * Entity implementation class for Entity: Solution<br>
 * 
 * @author Wail Shakir
 */
@Entity
@ValidPiPpSelection
@OneUseCaseInScope
public class Solution implements Serializable, TreeItem {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@NotNull
	@Size(min=1,max=50)
	@Column(length = 50, nullable = false)
	private String name;
	@ManyToMany(cascade = CascadeType.MERGE)
	@JoinTable(name = "SOLUTION_GRANULARITYQUESTION", joinColumns = { @JoinColumn(name = "solution") }, inverseJoinColumns = { @JoinColumn(name = "granularityQuestion", referencedColumnName = "id") })
	private List<GranularityQuestion> granularityQuestions;

	@ManyToOne(optional = false, fetch=FetchType.EAGER)
	@JoinColumn(name = "estimate", nullable = false)
	@ForeignKey(deleteAction=ForeignKeyAction.CASCADE)
	private Estimate estimate;

	@OneToMany(mappedBy = "solution", cascade = CascadeType.ALL)
	@ElementDependent
	private List<Constraint> constraints;

	@OneToMany(mappedBy = "solution", cascade = CascadeType.ALL)
	@ElementDependent
	private List<SolutionStaffingShape> solutionStaffingShapes;
	@Column(nullable = true)
	@Min(1)
	private Integer pi;
	@Column(nullable = true)
	@Min(1)
	private Integer pp;

	@Column(nullable = false)
	private int gearingFactor;

	@NotNull
	@Column(nullable = false)
	@Temporal(TemporalType.DATE)
	private Calendar projectStartDate;
	
	@Size(max=4000)
	@Column(length = 4000, nullable = true)
	private String comment;
	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar creationDate;
	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar modifyDate;

	@ManyToOne(optional = false)
	@JoinColumn(name = "lasteditor", nullable = false)
	private User lastEditor;

	@OneToMany(mappedBy = "solution", cascade = CascadeType.ALL)
	//@ElementDependent
	@Size(min=1, message="{validation.useCasePacks.size}")
	@Valid
	private List<UseCasePack> useCasePacks;

	@NotNull
	@ManyToOne(optional = false)
	@JoinColumn(name = "granularity", nullable = false)
	private GranularityLevel granularity;

	public List<UseCasePack> getUseCasePacks() {
		return useCasePacks;
	}

	public void setUseCasePacks(List<UseCasePack> useCasePacks) {
		this.useCasePacks = useCasePacks;
	}

	public Solution() {
		super();
	}

	/**
	 * @return the identification of the solution
	 */
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Either pp or pi has to be set.
	 * 
	 * @return the pi of the solution
	 */
	public Integer getPi() {
		return pi;
	}

	/**
	 * Either pp or pi has to be set.
	 * 
	 * @param pi
	 *            the pi of the solution
	 */
	public void setPi(Integer pi) {
		this.pi = pi;
	}

	/**
	 * Requried.
	 * 
	 * @return the gearing factor of the solution
	 */
	public int getGearingFactor() {
		return gearingFactor;
	}

	/**
	 * Requried.
	 * 
	 * @param gearingFactor
	 *            the gearing factor of the solution
	 */
	public void setGearingFactor(int gearingFactor) {
		this.gearingFactor = gearingFactor;
	}

	/**
	 * Requried.
	 * 
	 * @return the start date of the solution
	 */
	public Calendar getProjectStartDate() {
		return projectStartDate;
	}

	/**
	 * Requried.
	 * 
	 * @param startDate
	 *            the start date of the solution
	 */
	public void setProjectStartDate(Calendar startDate) {
		this.projectStartDate = startDate;
	}

	/**
	 * May have max. 4000 characters.
	 * 
	 * @return the estimates comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * May have max. 4000 characters.
	 * 
	 * @param comment
	 *            text for comment field
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * Requried.
	 * 
	 * @return the creation date of the solution
	 */
	public Calendar getCreationDate() {
		return creationDate;
	}

	/**
	 * Requried.
	 * 
	 * @param creationDate
	 *            the creation date of the solution
	 */
	public void setCreationDate(Calendar creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * Requried.
	 * 
	 * @return the modify date of the solution
	 */
	public Calendar getModifyDate() {
		return modifyDate;
	}

	/**
	 * Requried.
	 * 
	 * @param modifyDate
	 *            the modify date of the solution
	 */
	public void setModifyDate(Calendar modifyDate) {
		this.modifyDate = modifyDate;
	}

	/**
	 * @return the last editor for this Solution
	 */
	public User getLastEditor() {
		return lastEditor;
	}

	/**
	 * @param lastEditor
	 *            the last editor for this Solution
	 */
	public void setLastEditor(User lastEditor) {
		this.lastEditor = lastEditor;
	}

	/**
	 * @return a list of the granularity question for this solution
	 * @see GranularityQuestion
	 */
	public List<GranularityQuestion> getGranularityQuestions() {
		return granularityQuestions;
	}

	/**
	 * @param questions
	 *            the granularity questions form this solution
	 * @see GranularityQuestion
	 */
	public void setGranularityQuestions(List<GranularityQuestion> questions) {
		this.granularityQuestions = questions;
	}

	/**
	 * @param estimate
	 *            the estimate that have this solution
	 * @see Estimate
	 */
	public void setEstimate(Estimate estimate) {
		this.estimate = estimate;
	}

	/**
	 * @return the estimate form this solution
	 * @see Estimate
	 */
	@XmlTransient
	public Estimate getEstimate() {
		return estimate;
	}

	/**
	 * @param constraints
	 *            the constraints for this solution
	 * @see Constraint
	 */
	public void setConstraints(List<Constraint> constraints) {
		this.constraints = constraints;
	}

	/**
	 * @return a list of constraints form this solution
	 * @see Constraint
	 */
	public List<Constraint> getConstraints() {
		return constraints;
	}

	/**
	 * @param solutionStaffingShape
	 *            the solutionStaffingShape for this list
	 * @see SolutionStaffingShape
	 */
	public void setSolutionStaffingShapes(
			List<SolutionStaffingShape> solutionStaffingShape) {
		this.solutionStaffingShapes = solutionStaffingShape;
	}

	/**
	 * @return a list of SolutionStaffingShape form this solution
	 * @see SolutionStaffingShape
	 */
	public List<SolutionStaffingShape> getSolutionStaffingShapes() {
		return solutionStaffingShapes;
	}

	/**
	 * Sets the solution wide use case's granularity.
	 * 
	 * @param granularity
	 *            the GranularityLevel for this Solution
	 * @see GranularityLevel
	 */
	public void setGranularity(GranularityLevel granularity) {
		this.granularity = granularity;
	}

	/**
	 * @return the solution wide use case's granularity
	 * @see GranularityLevel
	 */
	public GranularityLevel getGranularity() {
		return granularity;
	}

	/**
	 * Either pp or pi has to be set.
	 * 
	 * @param pp
	 *            the pp value for the solution
	 */
	public void setPp(Integer pp) {
		this.pp = pp;
	}

	/**
	 * Either pp or pi has to be set.
	 * 
	 * @return the pp value for the solution
	 */
	public Integer getPp() {
		return pp;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Solution))
			return false;
		Solution other = (Solution) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	@XmlTransient
	public Collection<? extends TreeItem> getChildren() {
		return null;
	}

	@Override
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @param name
	 *            the representative name of the current item
	 */
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Solution";
	}
}
