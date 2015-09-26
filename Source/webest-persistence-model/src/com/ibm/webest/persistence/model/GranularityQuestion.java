package com.ibm.webest.persistence.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: Solution<br>
 * 
 * @author Wail Shakir
 */
@Entity
public class GranularityQuestion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private long id;

	@Column(length = 20)
	private String factorName;

	@Column(length = 4000, nullable = false)
	private String question;
	@Column(nullable = false)
	private double factor;

	@ManyToOne
	@JoinColumn(name = "level")
	private GranularityLevel level;

	public GranularityQuestion() {
		super();
	}

	/**
	 * Groups the "rows" of questions.
	 * 
	 * @return an acronym for the factor of the question
	 */
	public String getFactorName() {
		return factorName;
	}

	/**
	 * Groups the "rows" of questions.
	 * 
	 * @param factorName
	 *            an acronym for the factor of the question
	 */
	public void setFactorName(String factorName) {
		this.factorName = factorName;
	}

	/**
	 * @return the text of the question
	 */
	public String getQuestion() {
		return question;
	}

	/**
	 * @param question
	 *            the text of the question
	 */
	public void setQuestion(String question) {
		this.question = question;
	}

	/**
	 * @return the value of the factor for the calculation
	 */
	public double getFactor() {
		return factor;
	}

	/**
	 * @param factor
	 *            the value of the factor for the calculation
	 */
	public void setFactor(double factor) {
		this.factor = factor;
	}

	/**
	 * Groups the "columns" of questions.
	 * 
	 * @param level
	 *            the level this question belongs to
	 */
	public void setLevel(GranularityLevel level) {
		this.level = level;
	}

	/**
	 * Groups the "columns" of questions.
	 * 
	 * @return the level this question belongs to
	 */
	public GranularityLevel getLevel() {
		return level;
	}

	/**
	 * The id is automatically generated if omitted.
	 * 
	 * @param id
	 *            the unique identifier for the object
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the unique identifier for the object
	 */
	public long getId() {
		return id;
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
		if (!(obj instanceof GranularityQuestion))
			return false;
		GranularityQuestion other = (GranularityQuestion) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
