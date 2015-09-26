package com.ibm.webest.processing.impl.calculation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import com.ibm.webest.persistence.model.Milestone;
import com.ibm.webest.persistence.model.Phase;
import com.ibm.webest.persistence.model.ProjectEnvironment;
import com.ibm.webest.persistence.service.CalculationFacadeLocal;
import com.ibm.webest.persistence.service.DatabaseConnectionException;
import com.ibm.webest.persistence.service.EntityNotFoundException;
import com.ibm.webest.processing.administration.AuthenticationServiceLocal;
import com.ibm.webest.processing.calculation.RayleighCalculationException;
import com.ibm.webest.processing.calculation.RayleighCalculationServiceLocal;
import com.ibm.webest.processing.calculation.ReportCalculationServiceLocal;
import com.ibm.webest.processing.calculation.ReportCalculationServiceRemote;
import com.ibm.webest.processing.calculation.ReportException;
import com.ibm.webest.processing.helpers.Conversion;
import com.ibm.webest.processing.helpers.ConversionException;
import com.ibm.webest.processing.model.EstimationResult;
import com.ibm.webest.processing.model.ProjectLife;
import com.ibm.webest.processing.model.PutnamResult;
import com.ibm.webest.processing.model.Report;
import com.ibm.webest.processing.model.ReportItem;
import com.ibm.webest.processing.model.ReportMilestone;
import com.ibm.webest.processing.model.ReportPhase;

/**
 * ReportCalculatorService Creates a report and fills it with information
 * 
 * @author David Dornseifer
 * @author Wail Shakir
 * @version 1.0
 */
@DeclareRoles({ AuthenticationServiceLocal.ROLE_ADMINISTRATOR,
		AuthenticationServiceLocal.ROLE_ESTIMATOR,
		AuthenticationServiceLocal.ROLE_MANAGER })
@RolesAllowed({ AuthenticationServiceLocal.ROLE_ADMINISTRATOR,
		AuthenticationServiceLocal.ROLE_ESTIMATOR,
		AuthenticationServiceLocal.ROLE_MANAGER })
@Stateless
public class ReportCalculationService implements ReportCalculationServiceLocal,
		ReportCalculationServiceRemote {
	private Logger logger = Logger.getLogger(ReportCalculationService.class);

	private Report report;
	private PutnamResult putnamResult;

	private double weeksPerMonth;
	private byte hoursPerDay;
	private int daysPerWeek;

	@EJB
	private RayleighCalculationServiceLocal reayleightService;

	@EJB
	private CalculationFacadeLocal calculationFacade;

	private Report getReport() {
		return report;
	}

	@Override
	public Report createReport(EstimationResult result) throws ReportException {
		this.putnamResult = result.getPutnamResult();

		this.report = new Report(result.getSolution(), result.getUcp(),
				result.getSloc());
		this.hoursPerDay = getReport().getSolution().getEstimate()
				.getProjectEnvironment().getHoursPerDay();
		this.daysPerWeek = getReport().getSolution().getEstimate()
				.getProjectEnvironment().getDaysPerWeek();
		getReport()
				.setAverageStaff(
						((putnamResult.getEffort() * 12.0F) / (putnamResult
								.getTD() * ((float) this.hoursPerDay))));

		try {
			this.report.setRayleighResults(reayleightService.calc(result
					.getPutnamResult().getEffort(), result.getPutnamResult()
					.getTD()));
		} catch (RayleighCalculationException e1) {
			logger.error("Error in rayleigh calculation method", e1);
			throw new ReportException("Error in rayleigh calculation method",
					e1);
		}
		try {
			this.weeksPerMonth = calculationFacade
					.getCalculationParameter("weeks_per_month");
		} catch (EntityNotFoundException e) {
			logger.error("Don't find weeks per month in Database!", e);
			throw new ReportException(
					"Don't find weeks per month in Database!", e);
		} catch (DatabaseConnectionException e) {
			logger.error("Database connections problen!", e);
			throw new ReportException("Database connections problem!", e);
		}

		try {
			createProjectDetails();
		} catch (ConversionException e) {
			logger.error("The date for project details can't created!", e);
			throw new ReportException(
					"The date for project details can't created!", e);
		}
		for (int i = 0; i < getReport().getPhases().size(); i++) {
			List<ReportMilestone> milestones = getReport().getPhases().get(i)
					.getMilestones();
			Collections.sort(milestones, new Comparator<ReportMilestone>() {

				@Override
				public int compare(ReportMilestone object1,
						ReportMilestone object2) {
					double value = object1.getDuration()
							- object2.getDuration();
					
					return (value == 0) ? object1.getMilestone()
							.getMilestoneId()
							- object2.getMilestone().getMilestoneId()
							: ((value > 0) ? 1 : -1);
				}
			});
			changeStartDateDurationMilestone(milestones, getReport()
					.getPhases().get(i).getStartDate());

		}

		return getReport();

	}

	private void changeStartDateDurationMilestone(
			List<ReportMilestone> milestones, Calendar startDatePhase) {
		for (int i = 0; i < milestones.size(); i++) {
			if (i == 0)
				milestones.get(i).setStartDate(startDatePhase);
			else {

				milestones.get(i).setStartDate(
						milestones.get(i - 1).getEndDate());
				milestones.get(i).setDuration(
						milestones.get(i).getDuration()
								- milestones.get(i - 1).getDuration());
			}
		}

	}

	/**
	 * create a project phases array with size 5. The first project phase (array
	 * number 0) is the life of the project and from 1 to 4 are the neutral
	 * phases
	 * 
	 * @param putnamResult
	 *            the calculated putnam result.
	 * @param startDate
	 *            the phase start date
	 * @throws ConversionException
	 */
	private void createProjectDetails() throws ConversionException {
		ProjectEnvironment pe = getReport().getSolution().getEstimate()
				.getProjectEnvironment();
		float effort = putnamResult.getEffort() * 12F;
		Calendar startDate = getReport().getSolution().getProjectStartDate();
		getReport().setLife(new ProjectLife(putnamResult.getTD(), effort));
		Calendar calendar = (Calendar) startDate.clone();
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		createDate(report.getLife(), calendar);
		List<Phase> phases = pe.getPhases();
		ReportPhase lastPhase = null;
		for (Phase phase : phases) {
			ReportPhase rp = createProjectPhase(phase, putnamResult.getTD(),
					effort);
			getReport().getPhases().add(rp);
			if (lastPhase == null) {
				Calendar calendar2 = (Calendar) getReport().getLife()
						.getStartDate().clone();
				calendar2.add(Calendar.DAY_OF_MONTH, -1);
				createDate(rp, calendar2);
			} else {
				createDate(rp, lastPhase.getEndDate());
			}
			createMilestonePlan(rp);
			lastPhase = rp;
		}
		getReport().getLife().setPhases(getReport().getPhases());
	}

	/**
	 * create the duration, the effort and the average staff for a project phase
	 * 
	 * @param effort
	 *            the life effort
	 * @param percentageOfTheEffort
	 *            percentage of the effort for this phase
	 * @param phaseName
	 *            the name of the phase
	 * @return a ProjectPhase with duration, effort and average staff
	 */
	private ReportPhase createProjectPhase(Phase phase, float duration,
			float effort) {
		duration = calculatePercentage(phase.getPercentage(), duration);
		effort = calculatePercentage(phase.getPercentage(), effort);
		return new ReportPhase(phase, duration, effort);
	}

	private ReportMilestone createReportMilestone(Milestone mile,
			float duration, float effort) {
		duration = calculatePercentage(mile.getPercentage(), duration);
		effort = calculatePercentage(mile.getPercentage(), effort);

		return new ReportMilestone(mile, duration, effort);
	}

	private static float calculatePercentage(float percentage, float of) {
		return of * (percentage / 100.0F);
	}

	/**
	 * create the start date and the end date for a project phase
	 * 
	 * @param reportItem
	 *            the project phase that need a the start and end date
	 * @param startDate
	 *            is the start date from the Life Phase or the end date from the
	 *            last Phase or by milestones the start date from his Phase
	 * @param addOneDayToStartDate
	 *            true if
	 * @return a project phase
	 * @throws ConversionException
	 */
	private void createDate(ReportItem reportItem, Calendar startDate)
			throws ConversionException {

		Calendar calendar = (Calendar) startDate.clone();
		Conversion.date(calendar, 1, this.daysPerWeek);
		reportItem.setStartDate(calendar);
		Calendar calendar2 = (Calendar) startDate.clone();
		Conversion.date(calendar2, reportItem.getDuration(), weeksPerMonth,
				this.daysPerWeek, this.hoursPerDay);
		reportItem.setEndDate(calendar2);
	}

	/*----------------- MileStones ------------------------------*/

	/**
	 * create a milestone plan for every phase, but not for the life phase.
	 * 
	 * @throws DatabaseConnectionException
	 * @throws EntityNotFoundException
	 */
	private void createMilestonePlan(ReportPhase phase)
			throws ConversionException {
		List<Milestone> milestones = phase.getPhase().getMilestones();
		List<ReportMilestone> reportMilestones = new ArrayList<ReportMilestone>(
				milestones.size());
		for (Milestone ms : milestones) {
			ReportMilestone rm = createReportMilestone(ms, phase.getDuration(),
					phase.getEffort());
			Calendar calendar = (Calendar) phase.getStartDate().clone();
			calendar.add(Calendar.DAY_OF_MONTH, -1);
			createDate(rm, calendar);
			reportMilestones.add(rm);
		}
		phase.setMilestones(reportMilestones);
	}
}
