package com.ibm.webest.persistence.model;

import java.io.Serializable;

import java.util.Collection;
import java.util.List;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.openjpa.persistence.ElementDependent;
import org.apache.openjpa.persistence.jdbc.ForeignKey;
import org.apache.openjpa.persistence.jdbc.ForeignKeyAction;

/**
 * Entity implementation class for Entity: UseCasePack
 * 
 * @author Dirk Kohlweyer
 */
@Entity
public class UseCasePack implements Serializable, TreeItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@NotNull
	@Size(min=1, max=50)
	@Column(length = 50, nullable = false)
	private String name;

	@ElementDependent
	@Valid
	@Size(min=1, message="{validation.useCases.size}")
	@OrderBy("id")
	@OneToMany(mappedBy = "useCasePack", cascade = CascadeType.ALL)
	private List<UseCase> useCases;

	@ManyToOne
	@JoinColumn(name = "solution", nullable = false)
	@ForeignKey(deleteAction=ForeignKeyAction.CASCADE)
	private Solution solution;

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
	 * @return the use case pack's name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Sets the use cae packs's name.
	 * 
	 * @param name
	 *            the use case packs's name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return a list of all use cases belonging to this use case pack
	 */
	public List<UseCase> getUseCases() {
		return this.useCases;
	}

	/**
	 * Assigns a list of use cases to this use case pack.
	 * 
	 * @param useCases
	 *            a list of use cases
	 */
	public void setUseCases(List<UseCase> useCases) {
		this.useCases = useCases;
	}

	/**
	 * @return the use case pack's solution
	 */
	@XmlTransient
	public Solution getSolution() {
		return this.solution;
	}

	/**
	 * Assigns a solution to this use case pack.
	 * 
	 * @param solution
	 *            the solution
	 */
	public void setSolution(Solution solution) {
		this.solution = solution;
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
		if (!(obj instanceof UseCasePack))
			return false;
		UseCasePack other = (UseCasePack) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	@XmlTransient
	public Collection<? extends TreeItem> getChildren() {
		return getUseCases();
	}

}
