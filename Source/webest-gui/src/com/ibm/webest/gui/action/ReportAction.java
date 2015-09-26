package com.ibm.webest.gui.action;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;

import org.apache.myfaces.trinidad.model.ChildPropertyTreeModel;
import org.apache.myfaces.trinidad.model.TreeModel;

import com.ibm.webest.gui.utils.ManagedBeanUtils;
import com.ibm.webest.gui.utils.MessageUtils;
import com.ibm.webest.persistence.model.EffortUnit;
import com.ibm.webest.processing.administration.EstimationManagerException;
import com.ibm.webest.processing.administration.EstimationManagerServiceLocal;
import com.ibm.webest.processing.model.Report;
import com.ibm.webest.processing.model.ReportItem;

/**
 * Managed Bean for displaying the report.
 * @author Dirk Kohlweyer
 * @author Gregor Schumm
 */
public class ReportAction {
	private TreeModel milestonePlan = null;
	private Report currentReport = null;
	
	private List<ReportItem> phases = new ArrayList<ReportItem>();

	private EffortUnit referenceEffortUnit;
	private EffortUnit effortUnit;
	
	@EJB
	private EstimationManagerServiceLocal estimationManager;

	/**
	 * getter for the current report
	 * 
	 * @return the current report
	 */
	public Report getCurrentReport() {
		return currentReport;
	}

	/**
	 * setter for the current report
	 * 
	 * @param currentReport
	 */
	public void setCurrentReport(Report currentReport) {
		this.currentReport = currentReport;
		setEffortUnit(null);
		getPhases().clear();
		getPhases().add(currentReport.getLife());
		getPhases().addAll(currentReport.getPhases());
		setMilestonePlan(new ChildPropertyTreeModel(currentReport.getLife(),
				"children"));
	}

	/**
	 * output-function for navigation-rules
	 * 
	 * @return string-output "showReport" for a navigationrule defined in
	 *         faces-config
	 */
	public String generateReport() {
		try {
			setCurrentReport(estimationManager.generateReport(ManagedBeanUtils
					.getBean(SolutionAction.class, "solutionAction")
					.getCurrentSolution()));
		} catch (Throwable e) {
			MessageUtils.addErrorMessage("Report could not be generated: "+e.getMessage());
			return null;
		}
		return "showReport";
	}

	/**
	 * Tree: ProjectLife -> ReportPhases -> ReportMilestones
	 * @param tableModel sets the tree model for the milestone plan
	 */
	public void setMilestonePlan(TreeModel tableModel) {
		this.milestonePlan = tableModel;
	}

	/**
	 * Tree: ProjectLife -> ReportPhases -> ReportMilestones
	 * @return the tree model for milestone plan
	 */
	public TreeModel getMilestonePlan() {
		return milestonePlan;
	}

	/**
	  * The reference effort unit is used for conversion of units in the report.<br>
	 * The reference unit is used for the effort values in the calculated report.
	 * @param referenceEffortUnit the used reference effort unit
	 */
	public void setReferenceEffortUnit(EffortUnit referenceEffortUnit) {
		this.referenceEffortUnit = referenceEffortUnit;
	}

	/**
	 * The reference effort unit is used for conversion of units in the report.<br>
	 * The reference unit is used for the effort values in the calculated report.
	 * @return the used reference effort unit
	 */
	public EffortUnit getReferenceEffortUnit() throws EstimationManagerException {
		if (referenceEffortUnit == null)
			setReferenceEffortUnit(estimationManager.getReferenceEffortUnit());
		return referenceEffortUnit;
	}

	/**
	 * Currently displayed effort unit.<br>
	 * Report values will be converted from reference unit to this unit.
	 * @param effortUnit the effort unit object
	 */
	public void setEffortUnit(EffortUnit effortUnit) {
		this.effortUnit = effortUnit;
	}

	/**
	 * Currently displayed effort unit.<br>
	 * Report values will be converted from reference unit to this unit.
	 * @return the effort unit object
	 */
	public EffortUnit getEffortUnit() {
		if (effortUnit == null && getCurrentReport() != null)
			setEffortUnit(getCurrentReport().getSolution().getEstimate().getProjectEnvironment().getEffortUnit());
		return effortUnit;
	}

	/**
	 * @param phases the list of the project phases (first element is project life)
	 */
	public void setPhases(List<ReportItem> phases) {
		this.phases = phases;
	}

	/**
	 * @return the list of the project phases (first element is project life)
	 */
	public List<ReportItem> getPhases() {
		return phases;
	}
}
