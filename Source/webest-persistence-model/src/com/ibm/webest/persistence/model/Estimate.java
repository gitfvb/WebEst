package com.ibm.webest.persistence.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.openjpa.persistence.ElementDependent;
import org.apache.openjpa.persistence.jdbc.ForeignKey;

/**
 * Entity implementation class for Entity: Estimate
 * 
 * @author Wail Shakir
 */
@Entity
public class Estimate implements Serializable, TreeItem {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@NotNull
	@Size(min=1, max=50)
	@Column(length = 50, nullable = false)
	private String name;

	@NotNull
	@ManyToOne(optional = false)
	@ForeignKey
	@JoinColumn(name = "estimator", nullable = false, referencedColumnName = "id")
	private User estimator;

	@ManyToOne(optional = false)
	@ForeignKey
	@JoinColumn(name = "lastEditor", nullable = false, referencedColumnName = "id")
	private User lastEditor;

	@NotNull
	@ManyToOne(optional = false)
	@JoinColumn(name = "division", nullable = false)
	@ForeignKey
	private Division division;

	@Size(max=4000)
	@Column(length = 4000, nullable = true)
	private String comment;
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar creationDate;
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar modifyDate;

	@OneToOne(mappedBy = "estimate", cascade = CascadeType.ALL)
	@Valid
	private ProjectEnvironment projectEnvironment;

	@OneToMany(mappedBy = "estimate", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@OrderBy("name")
	@ElementDependent
	private List<Solution> solutions;

	public Estimate() {
		super();
	}

	/**
	 * @return the identification of the estimate
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * @param id
	 *            the estimate id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Required and may have max. 50 characters.
	 * 
	 * @return the name of the estimate
	 */
	public String getName() {
		return name;
	}

	/**
	 * Required and may have max. 50 characters.
	 * 
	 * @param name
	 *            is the name of the estimate
	 */
	public void setName(String name) {
		this.name = name;
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
	 * @return the creation date of the estimate
	 */
	public Calendar getCreationDate() {
		return creationDate;
	}

	/**
	 * Requried.
	 * 
	 * @param creationDate
	 *            is the creation date of the estimate
	 */
	public void setCreationDate(Calendar creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * Requried.
	 * 
	 * @return the modify date of the estimate
	 */
	public Calendar getModifyDate() {
		return modifyDate;
	}

	/**
	 * Requried.
	 * 
	 * @param modifyDate
	 *            is the modify date of the estimate
	 */
	public void setModifyDate(Calendar modifyDate) {
		this.modifyDate = modifyDate;
	}

	/**
	 * @param projectEnvironment
	 *            the corresponding project environment parameters
	 */
	public void setProjectEnvironment(ProjectEnvironment projectEnvironment) {
		this.projectEnvironment = projectEnvironment;
	}

	/**
	 * @return the corresponding project environment parameters
	 */
	public ProjectEnvironment getProjectEnvironment() {
		return projectEnvironment;
	}

	/**
	 * @param solutions
	 *            all solutions of this estimate
	 */
	public void setSolutions(List<Solution> solutions) {
		this.solutions = solutions;
	}

	/**
	 * @return all solutions of this estimate
	 */
	public List<Solution> getSolutions() {
		return solutions;
	}

	/**
	 * @return the division, this estimate belongs to
	 */
	public Division getDivision() {
		return division;
	}

	/**
	 * @param division
	 *            the division, this estimate belongs to
	 */
	public void setDivision(Division division) {
		this.division = division;
	}

	/**
	 * @return the user (estimator) who created the estimate
	 */
	public User getEstimator() {
		return estimator;
	}

	/**
	 * @param estimator
	 *            the user (estimator) who created the estimate
	 */
	public void setEstimator(User estimator) {
		this.estimator = estimator;
	}

	/**
	 * @return the user who was the last editor of the estimate
	 */
	public User getLastEditor() {
		return lastEditor;
	}

	/**
	 * @param lastEditor
	 *            the user who was the last editor of the estimate
	 */
	public void setLastEditor(User lastEditor) {
		this.lastEditor = lastEditor;
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
		if (!(obj instanceof Estimate))
			return false;
		Estimate other = (Estimate) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	@XmlTransient
	public Collection<? extends TreeItem> getChildren() {
		return getSolutions();
	}
	
	@Override
	public String toString() {
		return "Estimate";
	}

}
