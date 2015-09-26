package com.ibm.webest.processing.model;

import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import com.ibm.webest.persistence.model.Phase;
import com.ibm.webest.persistence.model.TreeItem;

/**
 * Represents a phase of the project with calculated values as part of a report.
 * 
 * @author Gregor Schumm
 */
public class ReportPhase extends ReportItem {
	private static final long serialVersionUID = -6308403041791304478L;

	private Phase phase;

	private List<ReportMilestone> milestones;

	/**
	 * Default no argument constructor.
	 */
	public ReportPhase() {
	}

	/**
	 * Instantiates the object with the given values.
	 * 
	 * @param phase
	 *            the user defined project phase
	 * @param duration
	 *            the duration of the phase
	 * @param effort
	 *            the effort of the phase
	 * @param startDate
	 *            the start date of the phase
	 * @param endDate
	 *            the end date of the phase
	 */
	public ReportPhase(Phase phase, float duration, float effort,
			Calendar startDate, Calendar endDate) {
		super(duration, effort, startDate, endDate);
		setPhase(phase);
	}

	/**
	 * Instantiates the object with the given values.
	 * 
	 * @param ph
	 *            phase the user defined project phase
	 * @param td
	 *            duration the duration of the phase
	 * @param effort
	 *            the effort of the phase
	 */
	public ReportPhase(Phase ph, float td, float effort) {
		super(td, effort);
		setPhase(ph);
	}

	/**
	 * Instantiates the object with the given values.
	 * 
	 * @param td
	 *            the duration of the phase
	 * @param effort
	 *            the effort of the phase
	 */
	public ReportPhase(float td, float effort) {
		super(td, effort);
	}

	/**
	 * @return all milestones associated with this phase
	 */
	@Override
	public Collection<? extends TreeItem> getChildren() {
		return milestones;
	}

	@Override
	public String getName() {
		return getPhase().getName();
	}

	/**
	 * @param phase
	 *            the user defined project phase
	 */
	public void setPhase(Phase phase) {
		this.phase = phase;
	}

	/**
	 * @return the user defined project phase
	 */
	public Phase getPhase() {
		return phase;
	}

	/**
	 * @param reportMilestones
	 *            all milestones associated with this phase
	 */
	public void setMilestones(List<ReportMilestone> reportMilestones) {
		milestones = reportMilestones;
	}

	/**
	 * @return all milestones associated with this phase
	 */
	public List<ReportMilestone> getMilestones() {
		return milestones;
	}

}
