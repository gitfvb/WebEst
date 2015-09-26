package com.ibm.webest.gui.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIParameter;
import javax.faces.event.ActionEvent;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;

import org.apache.log4j.Logger;
import org.apache.myfaces.extensions.validator.baseval.annotation.Length;
import org.apache.myfaces.trinidad.model.ChildPropertyTreeModel;
import org.apache.myfaces.trinidad.model.TreeModel;

import com.ibm.webest.gui.utils.ExceptionUtils;
import com.ibm.webest.gui.utils.ManagedBeanUtils;
import com.ibm.webest.gui.utils.MessageUtils;
import com.ibm.webest.gui.utils.TextUtils;
import com.ibm.webest.persistence.model.ApplicationTypeGroup;
import com.ibm.webest.persistence.model.DefectCategory;
import com.ibm.webest.persistence.model.Estimate;
import com.ibm.webest.persistence.model.IndustrySectorGroup;
import com.ibm.webest.persistence.model.Milestone;
import com.ibm.webest.persistence.model.Organization;
import com.ibm.webest.persistence.model.Solution;
import com.ibm.webest.persistence.model.Template;
import com.ibm.webest.persistence.model.TreeItem;
import com.ibm.webest.persistence.service.EntityNotFoundException;
import com.ibm.webest.processing.ServiceException;
import com.ibm.webest.processing.administration.AuthenticationServiceLocal;
import com.ibm.webest.processing.administration.EstimationManagerException;
import com.ibm.webest.processing.administration.EstimationManagerServiceLocal;
import com.ibm.webest.processing.administration.GuiServiceException;
import com.ibm.webest.processing.calculation.SizingCalculatorException;

/**
 * Managed bean to handle estimate selection.<br>
 * Also holds selected estimate after selection.
 * 
 * @author Florian Friedrichs
 * @author Gregor Schumm
 */
public class EstimateAction {
	@Valid
	private Estimate currentEstimate = null;
	@EJB
	private AuthenticationServiceLocal authService;
	@EJB
	private EstimationManagerServiceLocal estimationManager;
	private static Logger log = Logger.getLogger(EstimateAction.class);
	private boolean newEstimatePopupVisible = false;
	private boolean deletePopupVisible = false;
	private TreeItem selectedNode;
	private List<Integer> selectedRowKey;
	private Template chosenTemplate;
	@Length(minimum = 1, maximum = 4000)
	@NotNull
	private String newTemplateDescription;
	@Length(minimum = 1, maximum = 50)
	@NotNull
	private String newTemplateName;
	private boolean saveTemplatePopupVisible;
	private List<FacesMessage> messageCache = new LinkedList<FacesMessage>();
	private List<Estimate> estimatesCache;
	private long cacheTime = 0;

	private Validator validator = Validation.buildDefaultValidatorFactory()
			.getValidator();

	/**
	 * @return the template that was chosen by the user
	 */
	public Template getChosenTemplate() {
		return chosenTemplate;
	}

	/**
	 * @param chosenTemplate
	 *            the template that was chosen by the user
	 */
	public void setChosenTemplate(Template chosenTemplate) {
		this.chosenTemplate = chosenTemplate;
	}

	/**
	 * @return the selected node in the navigation
	 */
	public TreeItem getSelectedNode() {
		return selectedNode;
	}

	/**
	 * @param selectedNode
	 *            the selected node in the navigation
	 */
	public void setSelectedNode(TreeItem selectedNode) {
		this.selectedNode = selectedNode;
	}

	/**
	 * Event handler to be called after an estimate in the navigation pane is
	 * selected.<br>
	 * Sets currenEstimate property to the selected estimate.
	 * 
	 * @param event
	 *            JSF action event
	 * @throws GuiServiceException
	 * @throws EstimationManagerException
	 * @throws SizingCalculatorException
	 */
	@SuppressWarnings("unchecked")
	public void navigationNodeSelected(ActionEvent event) {
		UIParameter param = (UIParameter) event.getComponent().findComponent(
				"node");
		UIParameter param2 = (UIParameter) event.getComponent().findComponent(
				"rowKey");
		setSelectedRowKey((List<Integer>)param2.getValue());
		TreeItem node = (TreeItem) param.getValue();
		try {
			if (node instanceof Estimate)
				setCurrentEstimate((Estimate) node);
			else {
				Solution sol = (Solution) node;
				setCurrentEstimate(sol.getEstimate());
				ManagedBeanUtils
						.getBean(SolutionAction.class, "solutionAction")
						.setCurrentSolution(sol);
			}
		} catch (Throwable e) {
			String msg = e.getMessage();
			if ((e = ExceptionUtils.getException(e,
					EntityNotFoundException.class)) != null) {
				msg = e.getMessage();
			}
			messageCache.add(MessageUtils.getErrorMessage(msg));
		}
		setSelectedNode(node);
	}

	/**
	 * Sets the selected row key in dependence of the selected node in the
	 * navigation tree.
	 */
	public void calculateCurrentRowKey() {
		try {
			List<Estimate> estimates = getEstimates();
			List<Integer> rowKey = new LinkedList<Integer>();
			Estimate est = null;
			if (getSelectedNode() instanceof Solution) {
				Solution sol = ((Solution) getSelectedNode());
				est = sol.getEstimate();
				int solIdx = est.getSolutions().indexOf(sol);
				if (solIdx >= 0)
					rowKey.add(solIdx);
			} else if (getSelectedNode() instanceof Estimate) {
				est = (Estimate) getSelectedNode();
			}
			int estIdx = estimates.indexOf(est);
			if (estIdx >= 0)
				rowKey.add(0, estIdx);
			setSelectedRowKey(rowKey);
		} catch (EstimationManagerException e) {
			log.error("Error while calculating row key.", e);
		}
	}

	/**
	 * Navigate to the detail page of the selected navigation node.
	 * 
	 * @return destination
	 */
	public String gotoSelectedNode() {
		if (messageCache.size() > 0) {
			MessageUtils.addMessages(messageCache);
			messageCache.clear();
			return null;
		}
		if (selectedNode instanceof Solution)
			return gotoSolution();
		else
			return gotoEstimate();
	}

	/**
	 * goes to an estimation with support of navigation rules, if object is
	 * initialized
	 * 
	 * @return string filled with "goToHome" or "goToSolution"
	 */
	public String gotoSolution() {
		if (ManagedBeanUtils.getBean(SolutionAction.class, "solutionAction")
				.getCurrentSolution() == null) {
			return "goToHome";
		} else {
			return "goToSolution";
		}
	}

	/**
	 * goes to an estimation with support of navigation rules, if object is
	 * initialized
	 * 
	 * @return string filled with "goToEstimate" or "goToHome"
	 */
	public String gotoEstimate() {
		if (currentEstimate == null) {
			return "goToHome";
		} else {
			return "goToEstimate";
		}
	}

	/**
	 * @param estimate
	 *            the currently selected estimate
	 * @throws GuiServiceException
	 * @throws EstimationManagerException
	 */
	public void setCurrentEstimate(Estimate estimate)
			throws EstimationManagerException {
		Organization org = null;
		ApplicationTypeGroup appType = null;
		IndustrySectorGroup ind = null;
		if (estimate != null && estimate.getId() > 0) {
			currentEstimate = estimationManager.reloadEstimate(estimate);
			org = estimate.getDivision().getOrganization();
			appType = estimate.getProjectEnvironment().getApplicationType().getGroup();
			ind = estimate.getProjectEnvironment().getIndustrySector().getGroup();
		} else {
			currentEstimate = estimate;
		}
		destroyBeans();
		ProjectDescriptionAction projectDescriptionAction = ManagedBeanUtils.getBean(ProjectDescriptionAction.class, "projectDescriptionAction");
		projectDescriptionAction.setSelectedOrganization(org);
		projectDescriptionAction.setSelectedApplicationTypeGroup(appType);
		projectDescriptionAction.setSelectedIndustrySectorGroup(ind);		
	}

	/**
	 * Destroys the dependent managed beans.
	 */
	private void destroyBeans() {
		ManagedBeanUtils.destroyBean(ReportAction.class, "reportAction");
		ManagedBeanUtils.destroyBean(SolutionAction.class, "solutionAction");
		ManagedBeanUtils.destroyBean(GranularityAction.class, "granularityAction");
		ManagedBeanUtils.destroyBean(ProjectDescriptionAction.class, "projectDescriptionAction");
		ManagedBeanUtils.destroyBean(SizingAction.class, "sizingAction");
		ManagedBeanUtils.destroyBean(PiHistoryAction.class, "piHistoryAction");
	}

	/**
	 * @return the currently selected estimate
	 */
	public Estimate getCurrentEstimate() {
		return currentEstimate;
	}

	/**
	 * @return all available estimates, the current user is allowed to see
	 * @throws GuiServiceException
	 *             if an error occurred while fetching the data
	 */
	public List<Estimate> getEstimates() throws EstimationManagerException {
		if (estimatesCache == null
				|| cacheTime + 5000 < System.currentTimeMillis())
			estimatesCache = estimationManager.getAllEstimates();
		return estimatesCache;
	}

	/**
	 * @return data for the navigation tree with estimates and their solutions
	 * @throws EstimationManagerException
	 */
	public TreeModel getEstimatesTree() throws EstimationManagerException {
		List<Estimate> estimates = getEstimates();
		if (estimates.size() < 1) {
			setSelectedNode(null);
			setSelectedRowKey(new ArrayList<Integer>());
		}
		return new ChildPropertyTreeModel(estimates, "children");
	}

	/**
	 * saves the current estimate
	 * 
	 * @throws GuiServiceException
	 * @throws EstimationManagerException
	 */
	public String saveEstimate() {
		if (checkValidation()) {
			try {
				int id = currentEstimate.getId();
				setCurrentEstimate(estimationManager
						.saveEstimate(currentEstimate));
				MessageUtils.addInfoMessage("Estimate successfully saved.");
				setSelectedNode(currentEstimate);
				if (id < 1)
					calculateCurrentRowKey();
			} catch (Throwable e) {
				String msg = e.getMessage();
				if (ExceptionUtils.exceptionMatches(e,
						".*primary key constraint .* defined on 'MILESTONE'.*")) {
					msg = "Milestones must have unique IDs.";
				} else {
					msg = ExceptionUtils.getMessageFromException(e, msg);
				}
				if (msg == null)
					msg = "An unknown error occurred.";
				MessageUtils
						.addErrorMessage("Estimate could not be saved: " + msg);
			}
		} else {
			MessageUtils
					.addErrorMessage("Estimate could not be saved: Please check your input values.");
		}
		return "goToEstimate";
	}

	/**
	 * deletes the current estimate
	 * 
	 * @throws GuiServiceException
	 * @throws EstimationManagerException
	 */
	public String deleteEstimate() {
		try {
			deletePopupVisible = false;
			estimationManager.deleteEstimate(currentEstimate);
			MessageUtils.addInfoMessage("Estimate successfully deleted.");
			setSelectedRowKey(new ArrayList<Integer>());
			setSelectedNode(null);
			setCurrentEstimate(null);
		} catch (Throwable e) {
			MessageUtils.addErrorMessage("Estimate could not be deleted: "
					+ e.getMessage());
			return null;
		}
		return "goToHome";
	}

	/**
	 * Shortcut to add an signature with date and username
	 * 
	 * @throws ServiceException
	 * @throws EntityNotFoundException
	 */
	public void addCommentSignature(ActionEvent e)
			throws EntityNotFoundException, ServiceException {
		TextUtils.addCommentSignature(currentEstimate,
				authService.getCurrentUser());
	}

	/**
	 * Create a new solution for the current estimate an redirect to the new
	 * solution.
	 * 
	 * @return goToSolution
	 * @throws GuiServiceException
	 * @throws EstimationManagerException
	 * @throws SizingCalculatorException
	 */
	public String createSolution() throws GuiServiceException,
			EstimationManagerException, SizingCalculatorException {
		if (currentEstimate != null) {
			Solution s = estimationManager.createSolution(currentEstimate);
			SolutionAction solutionAction = ManagedBeanUtils.getBean(
					SolutionAction.class, "solutionAction");
			solutionAction.setCurrentSolution(s);
			setSelectedRowKey(new ArrayList<Integer>());
			setSelectedNode(s);
		}
		return "goToSolution";
	}

	/**
	 * changes the visibility-boolean of pi-history-popup
	 */
	public void changeNewEstimatePopupVisibility(ActionEvent e) {
		this.newEstimatePopupVisible = !newEstimatePopupVisible;
	}

	/**
	 * getter pi-history-popup visibility
	 * 
	 * @return boolean for pi-history-popup visibility
	 */
	public boolean isDeletePopupVisible() {
		return this.deletePopupVisible;
	}

	/**
	 * changes the visibility-boolean of delete estimate popup
	 */
	public void changeDeletePopupVisible(ActionEvent e) {
		this.deletePopupVisible = !deletePopupVisible;
	}

	/**
	 * changes the visibility-boolean of new template popup
	 */
	public void changeSaveTemplatePopupVisible(ActionEvent e) {
		this.saveTemplatePopupVisible = !saveTemplatePopupVisible;
	}

	/**
	 * Empties the properties newTemplateDescription and newTemplateName.
	 */
	public String resetSaveTemplatePopup() {
		newTemplateDescription = null;
		newTemplateName = null;
		return null;
	}
	
	/**
	 * getter delete estimate popup visibility
	 * 
	 * @return boolean for delete estimate popup visibility
	 */
	public boolean isNewEstimatePopupVisible() {
		return this.newEstimatePopupVisible;
	}

	/**
	 * clones an estimate
	 * 
	 * @return string value "goToEstimate" for navigation link in
	 *         faces-config.xml
	 */
	public String cloneEstimate() {
		try {
			setCurrentEstimate(estimationManager.cloneEstimate(currentEstimate));
			setSelectedNode(currentEstimate);
			setSelectedRowKey(new ArrayList<Integer>());
			MessageUtils.addInfoMessage("Estimate successfully cloned.");
			MessageUtils.addWarnMessage("Estimate is not saved yet.");
		} catch (Throwable e) {
			MessageUtils.addErrorMessage("Estimate was not cloned: "
					+ e.getMessage());
		}
		return "goToEstimate";
	}

	/**
	 * add an empty row to defect categories
	 * 
	 * @param event
	 */
	public void addRowDefectCategories(ActionEvent event) {
		DefectCategory d = new DefectCategory();
		d.setPercentage((byte) 0);
		d.setOwner(currentEstimate.getProjectEnvironment());
		currentEstimate.getProjectEnvironment().getDefectCategories().add(d);
	}

	/**
	 * deletes an defect category
	 * 
	 * @param event
	 *            given action event of gui
	 */
	public void deleteDefectCategory(ActionEvent event) {
		UIParameter param = (UIParameter) event.getComponent().findComponent(
				"defectCategory");
		DefectCategory dc = (DefectCategory) param.getValue();
		Iterator<DefectCategory> it = currentEstimate.getProjectEnvironment()
				.getDefectCategories().iterator();
		while (it.hasNext()) {
			if (it.next().equalsObject(dc)) {
				it.remove();
				break;
			}
		}
	}

	/**
	 * add an empty row to milestones
	 * 
	 * @param event
	 *            given action event of gui
	 */
	public void addRowMilestones(ActionEvent event) {
		Milestone m = new Milestone();
		m.setOwner(currentEstimate.getProjectEnvironment());
		currentEstimate.getProjectEnvironment().getMilestones().add(m);
	}

	/**
	 * deletes an milestone
	 * 
	 * @param event
	 *            given action event of gui
	 */
	public void deleteMilestone(ActionEvent event) {
		// remove(Object) not possible because .equals is working with id and
		// unsaved objects have all the same "null"-id
		UIParameter param = (UIParameter) event.getComponent().findComponent(
				"milestone");
		Milestone ms = (Milestone) param.getValue();
		Iterator<Milestone> it = currentEstimate.getProjectEnvironment()
				.getMilestones().iterator();
		while (it.hasNext()) {
			if (it.next().equalsObject(ms)) {
				it.remove();
				break;
			}
		}
	}

	/**
	 * Restores the default values specified in the template.
	 * 
	 * @return null
	 */
	public String restoreDefaults() {
		try {
			estimationManager.restoreTemplateDefaults(currentEstimate);
			MessageUtils.addInfoMessage("Defaults successfully restored.");
		} catch (Throwable e) {
			MessageUtils.addErrorMessage("Defaults could not be restored: "
					+ e.getMessage());
		}
		return null;
	}

	/**
	 * @param selectedRowKey
	 *            the row key of the selected node in the navigation
	 */
	public void setSelectedRowKey(List<Integer> selectedRowKey) {
		this.selectedRowKey = selectedRowKey;
	}

	/**
	 * @return the row key of the selected node in the navigation
	 */
	public List<Integer> getSelectedRowKey() {
		return selectedRowKey;
	}

	/**
	 * @param selectedRowKeys
	 *            all row keys of selected nodes in the navigation
	 */
	public void setSelectedRowKeys(List<List<Integer>> selectedRowKeys) {
		if (selectedRowKeys != null && selectedRowKeys.size() > 0)
			setSelectedRowKey(selectedRowKeys.get(0));
	}

	/**
	 * @return all row keys of selected nodes in the navigation
	 */
	public List<List<Integer>> getSelectedRowKeys() {
		List<List<Integer>> keys = new ArrayList<List<Integer>>(1);
		keys.add(getSelectedRowKey());
		return keys;
	}

	/**
	 * Creates a new estimate. If a template was chosen, it will be used.
	 * 
	 * @return goToEstimate, if creation was successful
	 */
	public String createEstimate() {
		try {
			if (chosenTemplate == null)
				setCurrentEstimate(estimationManager.createEstimate());
			else
				setCurrentEstimate(estimationManager
						.createEstimate(chosenTemplate));
			newEstimatePopupVisible = false;
			setSelectedNode(currentEstimate);
			setSelectedRowKey(new ArrayList<Integer>());
			return "goToEstimate";
		} catch (Throwable e) {
			MessageUtils.addErrorMessage("Estimate could not be created: "
					+ e.getMessage());
		}

		return null;
	}

	/**
	 * Save the current project environment as new template.
	 */
	public void saveAsTemplate() {
		try {
			if (checkValidation()) {
				estimationManager.saveAsTemplate(newTemplateName,
						newTemplateDescription,
						currentEstimate.getProjectEnvironment());
				setSaveTemplatePopupVisible(false);
				resetSaveTemplatePopup();
				MessageUtils.addInfoMessage("Template successfully changed.");
				MessageUtils
						.addInfoMessage("Template was automatically assigned to current estimate.");
			} else {
				MessageUtils
				.addErrorMessage("Template could not be saved: Please check your input values.");
			}
		} catch (Throwable e) {
			if (ExceptionUtils.exceptionMatches(e,
					".*'UNQ_TPL_NAME' defined on 'TEMPLATE'.*")) {
				MessageUtils
						.addErrorMessage("A Template with same name does already exist.");
			} else {
				MessageUtils.addErrorMessage(e.getMessage());
			}
		}
	}

	/**
	 * @param b
	 *            true if the save template popup should be visible
	 */
	public void setSaveTemplatePopupVisible(boolean b) {
		saveTemplatePopupVisible = b;
	}

	/**
	 * @return true if the save template popup is visible
	 */
	public boolean isSaveTemplatePopupVisible() {
		return saveTemplatePopupVisible;
	}

	/**
	 * @param newTemplateName
	 *            the name of the new template
	 */
	public void setNewTemplateName(String newTemplateName) {
		this.newTemplateName = newTemplateName;
	}

	/**
	 * @return the name of the new template
	 */
	public String getNewTemplateName() {
		return newTemplateName;
	}

	/**
	 * @param newTemplateDescription
	 *            the description of the new template
	 */
	public void setNewTemplateDescription(String newTemplateDescription) {
		this.newTemplateDescription = newTemplateDescription;
	}

	/**
	 * @return the description of the new template
	 */
	public String getNewTemplateDescription() {
		return newTemplateDescription;
	}

	/**
	 * @return messages cached during action listener invocations
	 */
	public List<FacesMessage> getMessageCache() {
		return messageCache;
	}

	/**
	 * Checks if the current estimate object only contains valid values.
	 * @return true if valid
	 */
	private boolean checkValidation() {
		Set<ConstraintViolation<Estimate>> validate = validator
				.validate(getCurrentEstimate());
		for (Iterator<ConstraintViolation<Estimate>> iterator = validate
				.iterator(); iterator.hasNext();) {
			ConstraintViolation<Estimate> constraintViolation = iterator.next();
			MessageUtils.addValidationMessage(constraintViolation);
		}
		return validate.isEmpty();
	}

	/**
	 * checks if the used browser is an mobile safari or not
	 * 
	 * @return true, if the browser is an mobile ios-browser, otherwise false
	 */
	public boolean isIosBrowser() {
		return ManagedBeanUtils.isIosBrowser();
	}

	/**
	 * @return true if current user can select organizations and divisions 
	 * @throws EntityNotFoundException
	 * @throws ServiceException
	 */
	public boolean isDivisionSelectable() throws EntityNotFoundException, ServiceException {
		return authService.isAdministrator() || authService.getCurrentUser().equals(currentEstimate.getEstimator());
	}
}
