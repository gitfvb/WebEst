package com.ibm.webest.persistence.model;


import java.io.Serializable;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: MTTDTimeUnit<br>
 * Mean time to defect time unit used in the project.
 * 
 * @see MeasuringUnit
 * @author Gregor Schumm
 */
@Entity
public class MTTDTimeUnit extends MeasuringUnit implements Serializable {
	private static final long serialVersionUID = 1L;

	public MTTDTimeUnit() {
		super();
	}

}
