package com.ibm.webest.test.processing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.List;

import javax.naming.NamingException;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ibm.webest.persistence.model.Milestone;
import com.ibm.webest.persistence.model.Phase;
import com.ibm.webest.persistence.model.ProjectEnvironment;
import com.ibm.webest.persistence.model.Solution;
import com.ibm.webest.persistence.service.DatabaseConnectionException;
import com.ibm.webest.persistence.service.EntityNotFoundException;
import com.ibm.webest.persistence.service.SolutionFacadeRemote;
import com.ibm.webest.processing.administration.EstimationManagerException;
import com.ibm.webest.processing.administration.EstimationManagerServiceRemote;
import com.ibm.webest.processing.model.EstimationResult;
import com.ibm.webest.processing.model.ProjectLife;
import com.ibm.webest.processing.model.Report;
import com.ibm.webest.processing.model.ReportItem;
import com.ibm.webest.processing.model.ReportMilestone;
import com.ibm.webest.processing.model.ReportPhase;
import com.ibm.webest.test.processing.tools.JdbcUtils;

/**
 * Test for the report calculation service.
 * 
 * @author Wail Shakir
 * @author Gregor Schumm
 */
public class TestReportCalculationService {

	private static final double DELTA = .001;
	private static EstimationManagerServiceRemote estimationRemote;
	private static SolutionFacadeRemote solutionRemote;
	EstimationResult estimationResult;

	private Report[] report;

	float[] averageStaff = { 317.56757F, 496.90298F };
	float[][][] durationEffort = {
			{ { 10.445498F, 26537.21F }, { 2.6113746F, 6634.3027F },
					{ 2.6113746F, 6634.3027F }, { 2.6113746F, 6634.3027F },
					{ 2.6113746F, 6634.3027F } },
			{ { 8.858007F, 26409.422F }, { 2.6574023F, 7922.8267F },
					{ 1.7716016F, 5281.8843F }, { 0.8858008F, 2640.9421F },
					{ 3.543203F, 10563.769F } } };
	int[] ucp = { 245, 150 };
	float[] sloc = { 30625.0F, 18750.0F };
	int[][] startDateYears = { { 2011, 2011, 2011, 2011, 2011 },
			{ 2011, 2011, 2011, 2011, 2011 } };
	int[][] startDateMonths = { { 0, 0, 2, 5, 7 }, { 4, 4, 6, 8, 9 } };
	int[][] startDateDays = { { 3, 3, 23, 10, 30 }, { 4, 4, 26, 20, 18 } };
	int[][] endDateYears = { { 2011, 2011, 2011, 2011, 2011 },
			{ 2012, 2011, 2011, 2011, 2012 } };
	int[][] endDateMonths = { { 10, 2, 5, 7, 10 }, { 0, 6, 8, 9, 1 } };
	int[][] endDateDays = { { 15, 22, 9, 29, 16 }, { 30, 25, 19, 17, 1 } };
	int[][][] milestoneEndDateYears = {
			{ { 2011 }, { 2011 }, { 2011, 2011 }, { 2011, 2011 } },
			{ { 2011, 2011 }, { 2011 }, { 2011 }, { 2011, 2012 } } };
	int[][][] milestoneEndDateMonths = { { { 1 }, { 4 }, { 6, 6 }, { 8, 8 } },
			{ { 5, 4 }, { 7 }, { 9 }, { 11, 0 } } };
	int[][][] milestoneEndDateDays = {
			{ { 17 }, { 17 }, { 20, 27 }, { 29, 21 } },
			{ { 6, 30 }, { 22 }, { 4 }, { 27, 18 } } };
	Float[][][][] milestoneDurationEffort = {
			{ { { 1.5668248F, 3980.5818F } }, { { 1.8279622F, 4644.0117F } },
					{ { 1.3056873F, 3317.1514F }, { 1.5668248F, 3980.5818F } },
					{ { 1.0445498F, 2653.7212F }, { 0.7834124F, 1990.2909F } } },
			{ { { 1.062961F, 3169.1306F }, { 0.7972207F, 2376.8481F } },
					{ { 0.8858008F, 2640.9421F } },
					{ { 0.48719043F, 1452.5182F } },
					{ { 2.3739462F, 7077.725F }, { 3.0825868F, 9190.479F } } } };

	@BeforeClass
	public static void beforeClass() throws FileNotFoundException,
			SQLException, ClassNotFoundException, NamingException {

		JdbcUtils.createUser();

		estimationRemote = EjbUtils.getEjb(
				EstimationManagerServiceRemote.class,
				TestProperties.getEjbUsername(),
				TestProperties.getEjbPassword());
		solutionRemote = EjbUtils.getEjb(SolutionFacadeRemote.class,
				TestProperties.getEjbUsername(),
				TestProperties.getEjbPassword());

	}

	@AfterClass
	public static void cleanUpClass() throws FileNotFoundException,
			SQLException, ClassNotFoundException {
		CleanUp();
		JdbcUtils.executeScript("test_data_Szenario.sql");
	}

	@Before
	public void init() throws FileNotFoundException, SQLException,
			ClassNotFoundException, NamingException,
			DatabaseConnectionException, EntityNotFoundException,
			EstimationManagerException {
		CleanUp();
		JdbcUtils.executeScript("test_data_Report.sql");
		createReportTestData();
	}

	private static void CleanUp() throws SQLException, ClassNotFoundException,
			FileNotFoundException {

		JdbcUtils.executeScript("del_test_data_Szenario.sql");
	}

	public void createReportTestData() throws DatabaseConnectionException,
			EntityNotFoundException, EstimationManagerException, SQLException,
			ClassNotFoundException {
		this.report = new Report[durationEffort.length];
		for (int i = 0; i < durationEffort.length; i++) {
			Solution solution = solutionRemote.loadSolutionById(i + 1);
			ProjectEnvironment pe = estimationRemote.reloadEstimate(
					solution.getEstimate()).getProjectEnvironment();
			this.report[i] = new Report(solution, this.ucp[i], this.sloc[i]);
			this.report[i].setAverageStaff(this.averageStaff[i]);

			for (int j = 0; j < 5; j++) {

				Calendar startDate = GregorianCalendar.getInstance();
				startDate.set(this.startDateYears[i][j],
						this.startDateMonths[i][j], this.startDateDays[i][j]);
				Calendar endDate = GregorianCalendar.getInstance();
				endDate.set(this.endDateYears[i][j], this.endDateMonths[i][j],
						this.endDateDays[i][j]);
				if (j == 0) {
					this.report[i].setLife(new ProjectLife(
							this.durationEffort[i][j][0],
							this.durationEffort[i][j][1], startDate, endDate));
				} else {
					Phase ph = pe.getPhases().get(j - 1);

					ReportMilestone[] milestone = new ReportMilestone[this.milestoneDurationEffort[i][j - 1].length];

					for (int h = 0; h < milestone.length; h++) {

						Calendar endDate1 = GregorianCalendar.getInstance();
						endDate1.set(this.milestoneEndDateYears[i][j - 1][h],
								milestoneEndDateMonths[i][j - 1][h],
								milestoneEndDateDays[i][j - 1][h]);
						Milestone m = getMilestone(h, ph);
						milestone[h] = new ReportMilestone(m,
								this.milestoneDurationEffort[i][j - 1][h][0],
								this.milestoneDurationEffort[i][j - 1][h][1],
								startDate, endDate1);

					}
					ReportPhase rp = new ReportPhase(ph,
							this.durationEffort[i][j][0],
							this.durationEffort[i][j][1], startDate, endDate);
					rp.setMilestones(Arrays.asList(milestone));
					this.report[i].getPhases().add(rp);

					Collections.sort(this.report[i].getPhases().get(j - 1)
							.getMilestones(),
							new Comparator<ReportMilestone>() {

								@Override
								public int compare(ReportMilestone object1,
										ReportMilestone object2) {
									return object1.getEndDate().compareTo(
											object2.getEndDate());
								}
							});
					changeStartDateDurationMilestone(this.report[i].getPhases()
							.get(j - 1).getMilestones(), this.report[i]
							.getPhases().get(j - 1).getStartDate());

				}
			}

		}
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

	private Milestone getMilestone(int skip, Phase ph) throws SQLException,
			ClassNotFoundException {
		Statement statement = JdbcUtils.getJdbcConnection().createStatement();
		ResultSet resultSet = statement
				.executeQuery("select name, percentage from milestone where phase = "
						+ ph.getId() + " order by milestoneid");
		Milestone m = new Milestone();
		while (resultSet.next() && skip-- >= 0) {
			m.setName(resultSet.getString(1));
			m.setPercentage(resultSet.getByte(2));
		}
		return m;
	}

	@Test(expected = EstimationManagerException.class)
	public void reportExpectionTest() throws EstimationManagerException {
		Solution solution = new Solution();
		try {
			solution = solutionRemote.loadSolutionById(4);
		} catch (DatabaseConnectionException e) {
			e.printStackTrace();
		} catch (EntityNotFoundException e) {
			e.printStackTrace();
		}

		estimationRemote.generateReport(solution);
	}

	@Test
	public void reportDataTest() throws EstimationManagerException,
			EntityNotFoundException, DatabaseConnectionException {
		for (int r = 0; r < this.report.length; r++) {

			Report report = estimationRemote.generateReport(solutionRemote
					.loadFullSolution(this.report[r].getSolution()));
			assertEquals(this.report[r].getUcp(), report.getUcp());
			assertEquals(this.report[r].getSloc(), report.getSloc(), DELTA);

			assertEquals(this.report[r].getAverageStaff(),
					report.getAverageStaff(), DELTA);
			ReportItem phase;
			ReportItem reference;
			for (int i = -1; i < report.getPhases().size(); i++) {
				if (i >= 0) {
					phase = report.getPhases().get(i);
					reference = this.report[r].getPhases().get(i);
				} else {
					phase = report.getLife();
					reference = this.report[r].getLife();

				}
				assertTrue(phase.getName().equals(reference.getName()));
				assertEquals(reference.getDuration(), phase.getDuration(),
						DELTA);
				assertEquals(reference.getEffort(), phase.getEffort(), DELTA);

				assertEquals(reference.getStartDate()
						.get(Calendar.DAY_OF_MONTH),
						phase.getStartDate().get(Calendar.DAY_OF_MONTH));
				assertEquals(reference.getStartDate().get(Calendar.MONTH),
						phase.getStartDate().get(Calendar.MONTH));

				assertEquals(reference.getStartDate().get(Calendar.YEAR), phase
						.getStartDate().get(Calendar.YEAR));

				assertEquals(reference.getEndDate().get(Calendar.DAY_OF_MONTH),
						phase.getEndDate().get(Calendar.DAY_OF_MONTH));
				assertEquals(reference.getEndDate().get(Calendar.MONTH), phase
						.getEndDate().get(Calendar.MONTH));
				assertEquals(reference.getEndDate().get(Calendar.YEAR), phase
						.getEndDate().get(Calendar.YEAR));

				if (phase instanceof ReportPhase) {
					ReportPhase repPhase = (ReportPhase) phase;

					for (int j = 0; j < repPhase.getChildren().size(); j++) {

						ReportMilestone localCandidate = repPhase
								.getMilestones().get(j);
						ReportMilestone localReference = ((ReportPhase) reference)
								.getMilestones().get(j);

						assertEquals(localReference.getName(),
								localCandidate.getName());

						assertEquals(localReference.getDuration(),
								localCandidate.getDuration(), DELTA);

						assertEquals(localReference.getEffort(),
								localCandidate.getEffort(), DELTA);

						assertEquals(
								localReference.getStartDate().get(
										Calendar.DAY_OF_MONTH),
								localCandidate.getStartDate().get(
										Calendar.DAY_OF_MONTH));
						assertEquals(
								localReference.getStartDate().get(
										Calendar.MONTH), localCandidate
										.getStartDate().get(Calendar.MONTH));
						assertEquals(
								localReference.getStartDate()
										.get(Calendar.YEAR), localCandidate
										.getStartDate().get(Calendar.YEAR));

						assertEquals(
								localReference.getEndDate().get(
										Calendar.DAY_OF_MONTH), localCandidate
										.getEndDate()
										.get(Calendar.DAY_OF_MONTH));
						assertEquals(
								localReference.getEndDate().get(Calendar.MONTH),
								localCandidate.getEndDate().get(Calendar.MONTH));
						assertEquals(
								localReference.getEndDate().get(Calendar.YEAR),
								localCandidate.getEndDate().get(Calendar.YEAR));

					}
				}
			}

		}
	}

}
