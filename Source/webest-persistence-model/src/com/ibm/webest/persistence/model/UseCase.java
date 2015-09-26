package com.ibm.webest.persistence.model;

import java.io.Serializable;

import java.util.Collection;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Entity implementation class for Entity: UseCase
 * 
 * @author Dirk Kohlweyer
 */
@Entity
public class UseCase implements Serializable, TreeItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(nullable = false)
	@Min(0)
	private double multiplier;

	@ManyToOne
	@JoinColumn(name = "granularityOverride", nullable = true)
	private GranularityLevel granularityOverride;

	@Column(nullable = false)
	private boolean inScope;

	@NotNull
	@Size(min = 1, max = 50)
	@Column(length = 50, nullable = false)
	private String name;

	@Size(max = 4000)
	@Column(length = 4000, nullable = true)
	private String assumptions;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "certainty", nullable = false)
	private Certainty certainty;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "useCaseComplexity", nullable = false)
	private UseCaseComplexity useCaseComplexity;

	@ManyToOne(cascade = { CascadeType.MERGE })
	@JoinColumn(name = "useCasePack", nullable = false)
	private UseCasePack useCasePack;

	private static final long serialVersionUID = 1L;

	/**
	 * ID will be automatically generated when committed.
	 * 
	 * @return The identifier (Primary Key) for this object
	 */
	public long getId() {
		return this.id;
	}

	/**
	 * @return the use case's multiplier
	 */
	public double getMultiplier() {
		return this.multiplier;
	}

	/**
	 * Sets the use case's multiplier.
	 * 
	 * @param multiplier
	 *            the multiplier
	 */
	public void setMultiplier(double multiplier) {
		this.multiplier = multiplier;
	}

	/**
	 * @return true if the use case is in scope
	 */
	public boolean isInScope() {
		return this.inScope;
	}

	/**
	 * Setter for inScope-Flag
	 * 
	 * @param inScope
	 *            true if the use case is in scope
	 */
	public void setInScope(boolean inScope) {
		this.inScope = inScope;
	}

	/**
	 * @return the use case's name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Sets the use case's name.
	 * 
	 * @param name
	 *            the name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the use case's certainty
	 */
	public Certainty getCertainty() {
		return this.certainty;
	}

	/**
	 * Sets the use case's certainty.
	 * 
	 * @param certainty
	 *            the certainty
	 */
	public void setCertainty(Certainty certainty) {
		this.certainty = certainty;
	}

	/**
	 * @return the use case's use case complexity
	 */
	public UseCaseComplexity getUseCaseComplexity() {
		return this.useCaseComplexity;
	}

	/**
	 * Sets the use case's use case complexity.
	 * 
	 * @param useCaseComplexity
	 *            use case complexity
	 */
	public void setUseCaseComplexity(UseCaseComplexity useCaseComplexity) {
		this.useCaseComplexity = useCaseComplexity;
	}

	/**
	 * @return the use case's use case pack
	 */
	@XmlTransient
	public UseCasePack getUseCasePack() {
		return this.useCasePack;
	}

	/**
	 * Assigns this Use Case to a Use Case Pack.
	 * 
	 * @param useCasePack
	 *            use case pack
	 */
	public void setUseCasePack(UseCasePack useCasePack) {
		this.useCasePack = useCasePack;
	}

	/**
	 * @return the use case's assumptions
	 */
	public String getAssumptions() {
		return this.assumptions;
	}

	/**
	 * Sets the use case's assumptions.
	 * 
	 * @param assumptions
	 *            assumptions
	 */
	public void setAssumptions(String assumptions) {
		this.assumptions = assumptions;
	}

	/**
	 * @return granularity level
	 */
	public GranularityLevel getGranularityOverride() {
		return granularityOverride;
	}

	/**
	 * 
	 * @param granularityOverride
	 */
	public void setGranularityOverride(GranularityLevel granularityOverride) {
		this.granularityOverride = granularityOverride;
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
		if (!(obj instanceof UseCase))
			return false;
		UseCase other = (UseCase) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public Collection<? extends TreeItem> getChildren() {
		return null;
	}

}
