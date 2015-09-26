package com.ibm.webest.processing.model;

import java.io.Serializable;
import java.util.Calendar;

import com.ibm.webest.persistence.model.TreeItem;

/**
 * Abstract report item which holds a duration, an effort, a start and end date.
 * @author Gregor Schumm
 */
public abstract class ReportItem implements TreeItem, Serializable {
	private static final long serialVersionUID = 6508277797484126036L;
	private float duration;
	private float effort;
	private Calendar startDate;
	private Calendar endDate;
	
	/**
	 * Default no argument constructor.
	 */
	protected ReportItem() {
	}
	
	/**
	 * Instantiates the object with the given values.
	 * @param duration the duration of the project
	 * @param effort the effort of the project
	 * @param startDate the start date of the project
	 * @param endDate the end date of the project
	 */
	protected ReportItem(float duration, float effort, Calendar startDate,
			Calendar endDate) {
		this(duration, effort);
		this.startDate = startDate;
		this.endDate = endDate;
	}
	/**
	 * Instantiates the object with the given values.
	 * @param duration the duration of the project
	 * @param effort the effort of the project
	 */
	protected ReportItem(float duration, float effort) {
		this.duration = duration;
		this.effort = effort;
	}
	
	/**
	 * @return the duration of the represented project segment
	 */
	public float getDuration() {
		return duration;
	}
	
	/**
	 * @param duration the duration of the represented project segment
	 */
	public void setDuration(float duration) {
		this.duration = duration;
	}
	
	/**
	 * @return the effort of the represented project segment
	 */
	public float getEffort() {
		return effort;
	}
	
	/**
	 * @param effort the effort of the represented project segment
	 */
	public void setEffort(float effort) {
		this.effort = effort;
	}
	
	/**
	 * @return the start date of the represented project segment
	 */
	public Calendar getStartDate() {
		return startDate;
	}
	
	/**
	 * @param startDate the start date of the represented project segment
	 */
	public void setStartDate(Calendar startDate) {
		this.startDate = startDate;
	}
	
	/**
	 * @return the end date of the represented project segment
	 */
	public Calendar getEndDate() {
		return endDate;
	}
	
	/**
	 * @param endDate the end date of the represented project segment
	 */
	public void setEndDate(Calendar endDate) {
		this.endDate = endDate;
	}
	
}
