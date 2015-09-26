package com.ibm.webest.persistence.model;

import java.io.Serializable;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.openjpa.persistence.jdbc.ForeignKey;
import org.apache.openjpa.persistence.jdbc.ForeignKeyAction;

/**
 * Entity implementation class for Entity: SolutionStaffingShape<br>
 * 
 * @author Wail Shakir
 */
@Entity
@Table(name = "SOLUTION_STAFFINGSHAPE", uniqueConstraints = @UniqueConstraint(columnNames = { "phase", "solution", "staffingShape" }))
public class SolutionStaffingShape implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private long id;
	
	@ManyToOne(cascade = { CascadeType.MERGE})
	@ForeignKey(deleteAction=ForeignKeyAction.CASCADE)
	@JoinColumn(name = "phase", referencedColumnName = "id")
	private Phase phase;
	@ManyToOne(cascade = { CascadeType.MERGE})
	@ForeignKey(deleteAction=ForeignKeyAction.CASCADE)
	@JoinColumn(name = "solution")
	private Solution solution;
	@ManyToOne(cascade = { CascadeType.MERGE})
	@ForeignKey(deleteAction=ForeignKeyAction.CASCADE)
	@JoinColumn(name = "staffingShape", nullable = true)
	private StaffingShape staffingShape;
	@Column(nullable = false)
	private boolean selected;

	public SolutionStaffingShape() {
		super();
	}

	/**
	 * Required.
	 * 
	 * @param selected
	 *            true, if the phase with the staffing shape in the Solution
	 *            should be selected.
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	/**
	 * Required.
	 * 
	 * @return true, if the phase with the staffing shape in the Solution should
	 *         be selected.
	 */
	public boolean isSelected() {
		return selected;
	}

	/**
	 * Required. Phase is a part of the identification.
	 * 
	 * @return the identification items phase
	 */
	public Phase getPhase() {
		return phase;
	}

	/**
	 * Required. Phase is a part of the identification.
	 * 
	 * @param phase
	 *            the identification items of the SolutionStaffingShape
	 */
	public void setPhase(Phase phase) {
		this.phase = phase;
	}

	/**
	 * Required. Solution is a part of the identification.
	 * 
	 * @return the identification items solution
	 */
	@XmlTransient
	public Solution getSolution() {
		return solution;
	}

	/**
	 * Required. Solution is a part of the identification.
	 * 
	 * @param solution
	 *            the identification items of the SolutionStaffingShape
	 */
	public void setSolution(Solution solution) {
		this.solution = solution;
	}

	/**
	 * Required. Staffing shape is a part of the identification.
	 * 
	 * @return the identification items staffing shape
	 */
	public StaffingShape getStaffingShape() {
		return staffingShape;
	}

	/**
	 * Required. Staffing shape is a part of the identification.
	 * 
	 * @param staffingShape
	 *            the identification items of the SolutionStaffingShape
	 */
	public void setStaffingShape(StaffingShape staffingShape) {
		this.staffingShape = staffingShape;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((phase == null) ? 0 : phase.hashCode());
		result = prime * result
				+ ((solution == null) ? 0 : solution.hashCode());
		result = prime * result
				+ ((staffingShape == null) ? 0 : staffingShape.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof SolutionStaffingShape))
			return false;
		SolutionStaffingShape other = (SolutionStaffingShape) obj;
		if (phase == null) {
			if (other.phase != null)
				return false;
		} else if (!phase.equals(other.phase))
			return false;
		if (solution == null) {
			if (other.solution != null)
				return false;
		} else if (!solution.equals(other.solution))
			return false;
		if (staffingShape == null) {
			if (other.staffingShape != null)
				return false;
		} else if (!staffingShape.equals(other.staffingShape))
			return false;
		return true;
	}

	/**
	 * @param id the unique indetifier of the object
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the unique indetifier of the object
	 */
	public long getId() {
		return id;
	}
}
