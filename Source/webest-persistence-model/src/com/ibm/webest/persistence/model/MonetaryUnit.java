package com.ibm.webest.persistence.model;


import java.io.Serializable;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: MonetaryUnit<br>
 * Currency used in the project.
 * 
 * @see MeasuringUnit
 * @author Gregor Schumm
 */
@Entity
public class MonetaryUnit extends MeasuringUnit implements Serializable {
	private static final long serialVersionUID = 1L;

	public MonetaryUnit() {
		super();
	}

}
