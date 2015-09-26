package com.ibm.webest.persistence.model;


import java.io.Serializable;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: EffortUnit<br>
 * Measuring unit for effort (e.g. person days or person hours) used in the
 * project.
 * 
 * @see MeasuringUnit
 * @author Gregor Schumm
 */
@Entity
public class EffortUnit extends MeasuringUnit implements Serializable {
	private static final long serialVersionUID = 1L;

	public EffortUnit() {
		super();
	}

}
