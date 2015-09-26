package com.ibm.webest.processing.model;

import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import com.ibm.webest.persistence.model.TreeItem;

/**
 * Represents the life of a project in the report.<br>
 * Contains start, end date, duration and effort of the whole project.
 * @author Gregor Schumm
 */
public class ProjectLife extends ReportItem {
	private static final long serialVersionUID = 6539532025864172271L;
	private List<ReportPhase> phases;
	
	/**
	 * Default no argument constructor.
	 */
	public ProjectLife() {
		super();
	}

	/**
	 * Instantiates the object with the given values.
	 * @param duration the duration of the project
	 * @param effort the effort of the project
	 * @param startDate the start date of the project
	 * @param endDate the end date of the project
	 */
	public ProjectLife(float duration, float effort, Calendar startDate,
			Calendar endDate) {
		super(duration, effort, startDate, endDate);
	}

	/**
	 * Instantiates the object with the given values.
	 * @param duration the duration of the project
	 * @param effort the effort of the project
	 */
	public ProjectLife(float duration, float effort) {
		super(duration, effort);
	}

	/**
	 * @return all phases with their calculated values of the project
	 */
	@Override
	public Collection<? extends TreeItem> getChildren() {
		return getPhases();
	}

	/**
	 * Always returns "Life" as name.
	 */
	@Override
	public String getName() {
		return "Life";
	}

	/**
	 * All phases of the project with their specific calculated values.
	 * @param phases the project phases to set
	 */
	public void setPhases(List<ReportPhase> phases) {
		this.phases = phases;
	}

	/**
	 * @return the phases of the project
	 */
	public List<ReportPhase> getPhases() {
		return phases;
	}

}
