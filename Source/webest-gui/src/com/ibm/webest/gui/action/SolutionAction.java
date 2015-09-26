package com.ibm.webest.gui.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJB;
import javax.faces.component.UIParameter;
import javax.faces.event.ActionEvent;
import javax.persistence.EntityExistsException;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;

import com.ibm.webest.gui.utils.ExceptionUtils;
import com.ibm.webest.gui.utils.ManagedBeanUtils;
import com.ibm.webest.gui.utils.MessageUtils;
import com.ibm.webest.gui.utils.SelectBoxUtils;
import com.ibm.webest.gui.utils.TextUtils;
import com.ibm.webest.persistence.model.Constraint;
import com.ibm.webest.persistence.model.Solution;
import com.ibm.webest.persistence.model.StaffingShape;
import com.ibm.webest.persistence.service.EntityNotFoundException;
import com.ibm.webest.processing.ServiceException;
import com.ibm.webest.processing.administration.AuthenticationServiceLocal;
import com.ibm.webest.processing.administration.EstimationManagerException;
import com.ibm.webest.processing.administration.EstimationManagerServiceLocal;
import com.ibm.webest.processing.administration.GuiServiceException;
import com.ibm.webest.processing.administration.GuiServiceLocal;
import com.ibm.webest.processing.calculation.SizingCalculatorException;
import com.ibm.webest.processing.helpers.DefaultValues;

/**
 * Managed bean to handle solution selection.<br>
 * Also holds selected solution after selection.
 * 
 * @author Florian Friedrichs
 * @author Gregor Schumm
 * @author Dirk Kohlweyer
 */
public class SolutionAction {
	@Valid
	private Solution currentSolution = null;
	@EJB
	private GuiServiceLocal guiService;

	@EJB
	private EstimationManagerServiceLocal estimationManager;
	@EJB
	private AuthenticationServiceLocal authService;

	private Validator validator = Validation.buildDefaultValidatorFactory()
			.getValidator();

	/**
	 * if editConstraint is called, the boolean-value will be inverted and a
	 * constraint-object will be deleted or created
	 */
	private boolean editConstraint;

	private boolean deletePopupVisible;

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
	public void selectSolution(ActionEvent event) throws GuiServiceException,
			EstimationManagerException, SizingCalculatorException {
		UIParameter param = (UIParameter) event.getComponent().findComponent(
				"solution");
		setCurrentSolution((Solution) param.getValue());

	}

	/**
	 * @param solution
	 *            the currently selected solution
	 * @throws GuiServiceException
	 * @throws EstimationManagerException
	 * @throws SizingCalculatorException
	 */
	public void setCurrentSolution(Solution solution)
			throws EstimationManagerException, GuiServiceException,
			SizingCalculatorException {
		if (solution != null && solution.getId() > 0) {

			this.currentSolution = estimationManager.reloadSolution(solution);
		} else {
			this.currentSolution = solution;
			this.editConstraint = false;
		}

		destroyBeans();

		if (solution != null) {

			getGranularityAction().generateGranularityQuestionsMap();

			// fill UseCases
			getSizingAction().fillUseCaseList();

			// PI Selection
			getPiHistoryAction().initiate((currentSolution.getPp() == null));

		}

	}

	/**
	 * Destroys the dependent managed beans.
	 */
	private void destroyBeans() {
		ManagedBeanUtils.destroyBean(ReportAction.class, "reportAction");
		ManagedBeanUtils.destroyBean(GranularityAction.class,
				"granularityAction");
		ManagedBeanUtils.destroyBean(SizingAction.class, "sizingAction");
		ManagedBeanUtils.destroyBean(PiHistoryAction.class, "piHistoryAction");
	}

	/**
	 * @return the currently selected estimate
	 */
	public Solution getCurrentSolution() {
		return currentSolution;
	}

	/**
	 * @return all available staffing shapes
	 * @throws GuiServiceException
	 */
	public Map<String, StaffingShape> getStaffingShapes()
			throws GuiServiceException {
		return SelectBoxUtils.createSelectItemsMap(
				guiService.getAllStaffingShapes(), null, "name");
	}

	/**
	 * changes the visibility-boolean of pi-history-popup
	 */
	public void changeDeletePopupVisible(ActionEvent e) {
		this.deletePopupVisible = !deletePopupVisible;
	}

	/**
	 * getter for deletion popup
	 * 
	 * @return deletion popup
	 */
	public boolean isDeletePopupVisible() {
		return deletePopupVisible;
	}

	/**
	 * setter for deletion popup
	 * 
	 * @param deletePopupVisible
	 */
	public void setDeletePopupVisible(boolean deletePopupVisible) {
		this.deletePopupVisible = deletePopupVisible;
	}

	/**
	 * clones the solution
	 * 
	 * @return string-output "goToSolution" for a navigationrule defined in
	 *         faces-config
	 * @throws EstimationManagerException
	 */
	public String cloneSolution() throws EstimationManagerException {
		try {
			setCurrentSolution(estimationManager
					.cloneSolutionForEstimate(currentSolution));
			MessageUtils.addInfoMessage("Solution successfully cloned.");
			getEstimateAction().setSelectedNode(currentSolution);
			getEstimateAction().setSelectedRowKey(new ArrayList<Integer>());
		} catch (Throwable e) {
			MessageUtils.addErrorMessage("Solution could not be cloned: "
					+ e.getMessage());
			return null;
		}
		return "goToSolution";
	}

	/**
	 * deletes an solution
	 * 
	 * @return string-output "goToEstimate" for a navigationrule defined in
	 *         faces-config
	 * @throws EstimationManagerException
	 */
	public String deleteSolution() throws EstimationManagerException {
		try {
			Solution sol = currentSolution;
			estimationManager.deleteSolution(currentSolution);
			setCurrentSolution(null);
			MessageUtils.addInfoMessage("Solution successfully deleted.");
			EstimateAction ea = getEstimateAction();
			ea.setSelectedNode(sol.getEstimate());
			ea.setCurrentEstimate(sol.getEstimate());
			ea.calculateCurrentRowKey();
		} catch (Throwable e) {
			MessageUtils.addErrorMessage("Solution could not be deleted: "
					+ e.getMessage());
			return null;
		}
		return "goToEstimate";
	}

	/**
	 * Saves the current solution in the database
	 */
	public void saveSolution() {
		currentSolution.setUseCasePacks(getSizingAction().getUseCasePacks());
		getGranularityAction().processGranularityQuestions();

		if (checkValidation()) {
			try {
				long id = currentSolution.getId();
				setCurrentSolution(estimationManager
						.saveSolution(currentSolution));
				ManagedBeanUtils
						.getBean(EstimateAction.class, "estimateAction")
						.setCurrentEstimate(currentSolution.getEstimate());
				MessageUtils.addInfoMessage("Solution successfully saved.");
				if (id < 1)
					getEstimateAction().calculateCurrentRowKey();
			} catch (Throwable e) {
				Throwable exception = ExceptionUtils.getException(e,
						EntityExistsException.class);
				String msg;
				if (exception != null)
					msg = exception.getMessage();
				else
					msg = ExceptionUtils.getMessageFromException(e,
							e.getMessage());
				MessageUtils.addErrorMessage("Solution could not be saved: "
						+ msg);
			}
		} else {
			MessageUtils
					.addErrorMessage("Solution could not be saved: Please check your input values.");
		}
	}

	/**
	 * Runs the validator over the current solution object. If there are
	 * constraints violated a message is added to the current context.
	 * 
	 * @return true if the object is valid
	 */
	private boolean checkValidation() {
		Set<ConstraintViolation<Solution>> validate = validator
				.validate(getCurrentSolution());
		for (Iterator<ConstraintViolation<Solution>> iterator = validate
				.iterator(); iterator.hasNext();) {
			ConstraintViolation<Solution> constraintViolation = iterator.next();
			MessageUtils.addValidationMessage(constraintViolation);
		}
		return validate.isEmpty();
	}

	/**
	 * Shortcut to add a signature with date and user name to the comments box.
	 * 
	 * @throws ServiceException
	 * @throws EntityNotFoundException
	 */
	public void addCommentSignature(ActionEvent e)
			throws EntityNotFoundException, ServiceException {
		TextUtils.addCommentSignature(currentSolution,
				authService.getCurrentUser());
	}

	/**
	 * Sets the flag, if the user wants to specify the duration constraint.
	 * 
	 * @param editConstraint
	 *            true if constraint specification is enabled
	 */
	public void setEditConstraint(boolean editConstraint) {
		this.editConstraint = editConstraint;

		if (this.editConstraint) {
			Constraint c = new Constraint();
			c.setSolution(currentSolution);
			c.setType(DefaultValues.getString("duration_constraint_type"));
			currentSolution.getConstraints().add(c);
		} else {
			currentSolution.getConstraints().clear();
		}

	}

	/**
	 * @return true if edit constraint checkbox is checked
	 */
	public boolean isEditConstraint() {
		return (currentSolution.getConstraints().size() != 0);
	}

	/**
	 * getter for shown constraint. if edit constraint is false, this object will
	 * be an new an empty constraint.
	 * 
	 * @return solution's first constraint of a new constraint
	 */
	public Constraint getConstraint() {
		if (currentSolution.getConstraints().size() != 0)
			return currentSolution.getConstraints().get(0);
		return new Constraint();
	}

	/**
	 * setter for shown constraint.
	 * 
	 * @param constraint
	 */
	public void setConstraint(Constraint constraint) {
		if (currentSolution.getConstraints().size() != 0)
			this.currentSolution.getConstraints().set(0, constraint);
	}

	/**
	 * @return the current sizingAction managed bean
	 */
	private SizingAction getSizingAction() {
		return ManagedBeanUtils.getBean(SizingAction.class, "sizingAction");
	}

	/**
	 * @return the current piHistoryAction managed bean
	 */
	private PiHistoryAction getPiHistoryAction() {
		return ManagedBeanUtils.getBean(PiHistoryAction.class,
				"piHistoryAction");
	}

	/**
	 * @return the current granularityAction managed bean
	 */
	private GranularityAction getGranularityAction() {
		return ManagedBeanUtils.getBean(GranularityAction.class,
				"granularityAction");
	}

	/**
	 * @return the current estimateAction managed bean
	 */
	private EstimateAction getEstimateAction() {
		return ManagedBeanUtils.getBean(EstimateAction.class, "estimateAction");
	}

}
