package com.ibm.webest.persistence.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.openjpa.persistence.jdbc.ForeignKey;

/**
 * Entity implementation class for Entity: Constraint<br>
 * 
 * @author Wail Shakir
 */
@Entity
@Table(name = "\"CONSTRAINT\"")
public class Constraint implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "\"TYPE\"", length = 50, nullable = false)
	private String type;
	@Min(0)
	@Column(nullable = false)
	private double target;
	@Column(nullable = true)
	@Min(0)
	@Max(100)
	private byte targetProbability;

	@ManyToOne
	@ForeignKey
	@JoinColumn(name = "solution", nullable = false)
	private Solution solution;

	public Constraint() {
		super();
	}

	/**
	 * Required and may have max. 50 characters.
	 * 
	 * @return the Constraint identification
	 */
	public String getType() {
		return type;
	}

	/**
	 * Required and may have max. 50 characters.
	 * 
	 * @param type
	 *            is the constraint identification
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Required.
	 * 
	 * @return the target of the constraint
	 */
	public double getTarget() {
		return target;
	}

	/**
	 * Required.
	 * 
	 * @param target
	 *            is the target of the constraint
	 */
	public void setTarget(double target) {
		this.target = target;
	}

	/**
	 * @return the target probability of the constraint
	 */
	public byte getTargetProbability() {
		return targetProbability;
	}

	/**
	 * 
	 * @param targetProbability
	 *            is the target probability of the constraint
	 */
	public void setTargetProbability(byte targetProbability) {
		this.targetProbability = targetProbability;
	}

	/**
	 * Required.
	 * 
	 * @return the solution for this constraint
	 */
	@XmlTransient
	public Solution getSolution() {
		return solution;
	}

	/**
	 * Required.
	 * 
	 * @param solution
	 *            is the solution for this constraint
	 */
	public void setSolution(Solution solution) {
		this.solution = solution;
	}

	/**
	 * A unique id is generated, if omitted.
	 * 
	 * @param id
	 *            the unique identifier of the object
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the unique identifier of the object
	 */
	public int getId() {
		return id;
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
		if (!(obj instanceof Constraint))
			return false;
		Constraint other = (Constraint) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
