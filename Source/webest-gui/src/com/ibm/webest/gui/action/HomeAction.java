package com.ibm.webest.gui.action;

import java.util.List;

import javax.ejb.EJB;
import javax.faces.component.UIParameter;
import javax.faces.event.ActionEvent;

import com.ibm.webest.gui.utils.ExceptionUtils;
import com.ibm.webest.gui.utils.ManagedBeanUtils;
import com.ibm.webest.gui.utils.MessageUtils;
import com.ibm.webest.persistence.model.Estimate;
import com.ibm.webest.persistence.model.Solution;
import com.ibm.webest.persistence.model.TreeItem;
import com.ibm.webest.persistence.service.EntityNotFoundException;
import com.ibm.webest.processing.administration.EstimationManagerException;
import com.ibm.webest.processing.administration.EstimationManagerServiceLocal;

/**
 * Managed Bean for the home screen (dashboard).
 * @author Gregor Schumm
 */
public class HomeAction {
	@EJB
	private EstimationManagerServiceLocal estimationManager;

	/**
	 * Handles clicks on the home screen.
	 * Action is invoke, if when user click on a estimate or solution link on the dashboard.
	 * @param e
	 *            the action event from the link
	 */
	public void homeLinkClicked(ActionEvent e) {
		UIParameter param = (UIParameter) e.getComponent()
				.findComponent("node");
		TreeItem node = (TreeItem) param.getValue();
		getEstimateAction().setSelectedNode(node);
		try {
			if (node instanceof Estimate)
				getEstimateAction().setCurrentEstimate((Estimate) node);
			else {
				Solution sol = (Solution) node;
				getEstimateAction().setCurrentEstimate(sol.getEstimate());
				ManagedBeanUtils
						.getBean(SolutionAction.class, "solutionAction")
						.setCurrentSolution(sol);
			}
			getEstimateAction().calculateCurrentRowKey();
		} catch (Throwable ex) {
			String msg = ex.getMessage();
			if ((ex = ExceptionUtils.getException(ex,
					EntityNotFoundException.class)) != null) {
				msg = ex.getMessage();
			}
			// we are not in an action, so save message for next action invocation
			getEstimateAction().getMessageCache().add(MessageUtils.getErrorMessage(msg));
		}
		
	}
	
	/**
	 * @return recently modified estimates of the user
	 * @throws EstimationManagerException
	 */
	public List<Estimate> getRecentUserEstimates()
			throws EstimationManagerException {
		return estimationManager.getRecentUserEstimates(10);
	}

	/**
	 * @return recently modified estimates of the user's division
	 * @throws EstimationManagerException
	 */
	public List<Estimate> getRecentDivisionEstimates()
			throws EstimationManagerException {
		return estimationManager.getRecentDivisionEstimates(10);
	}

	/**
	 * @return recently modified solutions of the user
	 * @throws EstimationManagerException
	 */
	public List<Solution> getRecentUserSolutions()
			throws EstimationManagerException {
		return estimationManager.getRecentUserSolutions(10);
	}

	/**
	 * @return recently modified solutions of the user's division
	 * @throws EstimationManagerException
	 */
	public List<Solution> getRecentDivisionSolutions()
			throws EstimationManagerException {
		return estimationManager.getRecentDivisionSolutions(10);
	}
	
	/**
	 * @return current estimateAction managed bean
	 */
	private EstimateAction getEstimateAction() {
		return ManagedBeanUtils.getBean(EstimateAction.class, "estimateAction");
	}
}
