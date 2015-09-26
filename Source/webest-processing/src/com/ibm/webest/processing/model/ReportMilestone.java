package com.ibm.webest.processing.model;

import java.util.Calendar;
import java.util.Collection;

import com.ibm.webest.persistence.model.Milestone;
import com.ibm.webest.persistence.model.TreeItem;

/**
 * Represents a milestone in a phase with calculated values as part of a report.
 * @author Gregor Schumm
 */
public class ReportMilestone extends ReportItem {
	private static final long serialVersionUID = 573748046258062947L;
	private Milestone milestone;
	
	/**
	 * Default no argument constructor.
	 */
	public ReportMilestone() {
		super();
	}
	
	/**
	 * Instantiates the object with the given values.
	 * @param m the user defined milestone
	 * @param duration the time from begin of phase to milestone due date
	 * @param effort the effort until milestone due date
	 * @param startDate the start date of the corresponding phase
	 * @param endDate the due date of the milestone
	 */
	public ReportMilestone(Milestone m, float duration, float effort, Calendar startDate,
			Calendar endDate) {
		super(duration, effort, startDate, endDate);
		setMilestone(m);
	}

	/**
	 * Instantiates the object with the given values.
	 * @param m the user defined milestone
	 * @param duration the time from begin of phase to milestone due date
	 * @param effort the effort until milestone due date
	 */
	public ReportMilestone(Milestone m, float duration, float effort) {
		super(duration, effort);
		setMilestone(m);
	}

	/**
	 * @return returns always null because their are no children
	 */
	@Override
	public Collection<? extends TreeItem> getChildren() {
		return null;
	}

	@Override
	public String getName() {
		return getMilestone().getName();
	}

	/**
	 * @param milestone the user defined milestone 
	 */
	public void setMilestone(Milestone milestone) {
		this.milestone = milestone;
	}

	/**
	 * @return the user defined milestone 
	 */
	public Milestone getMilestone() {
		return milestone;
	}
	
}
