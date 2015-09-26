package com.ibm.webest.persistence.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: PiPpLookup
 * 
 * @author Dirk Kohlweyer
 */
@Entity
public class PiPpLookup implements Serializable {

	@Id
	private int pi;

	@Column(nullable = false)
	private int pp;

	private static final long serialVersionUID = 1L;

	/**
	 * @return the pi value
	 */
	public int getPi() {
		return this.pi;
	}

	/**
	 * Sets the pi value.
	 * 
	 * @param pi
	 *            pi value
	 */
	public void setPi(int pi) {
		this.pi = pi;
	}

	/**
	 * @return the pp value
	 */
	public int getPp() {
		return this.pp;
	}

	/**
	 * Sets the pp value.
	 * 
	 * @param pp
	 *            pp value
	 */
	public void setPp(int pp) {
		this.pp = pp;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + pi;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof PiPpLookup))
			return false;
		PiPpLookup other = (PiPpLookup) obj;
		if (pi != other.pi)
			return false;
		return true;
	}

}
