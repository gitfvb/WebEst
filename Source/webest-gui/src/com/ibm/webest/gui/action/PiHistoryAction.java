package com.ibm.webest.gui.action;

import java.util.Iterator;

import javax.faces.event.ActionEvent;

import org.apache.myfaces.trinidad.component.core.data.CoreTreeTable;
import org.apache.myfaces.trinidad.component.core.input.CoreInputText;
import org.apache.myfaces.trinidad.context.RequestContext;
import org.apache.myfaces.trinidad.event.SelectionEvent;

import com.ibm.webest.gui.model.PiHistoryItem;
import com.ibm.webest.gui.utils.ManagedBeanUtils;
import com.ibm.webest.gui.utils.TreeUtils;
import com.ibm.webest.processing.administration.GuiServiceException;

/**
 * @author Florian Friedrichs
 * @author Gregor Schumm
 */
public class PiHistoryAction {

	private boolean piHistoryPopupVisible = false;
	private CoreInputText piInputBox;
	private CoreInputText ppInputBox;
	private boolean piSelected = true;
	private int avgPp = 0;

	/**
	 * Getter for the solution session.
	 * 
	 * @return the current solution session.
	 */
	public SolutionAction getSolutionAction() {
		return ManagedBeanUtils.getBean(SolutionAction.class, "solutionAction");
	}

	/**
	 * changes the visibility-boolean of pi-history-popup
	 */
	public void changePiHistoryPopupVisibility(ActionEvent e) {
		this.piHistoryPopupVisible = !piHistoryPopupVisible;
	}

	/**
	 * getter pi-history-popup visibility
	 * 
	 * @return boolean for pi-history-popup visibility
	 */
	public boolean isPiHistoryPopupVisible() {
		return this.piHistoryPopupVisible;
	}

	/**
	 * Getter for selectedPP
	 * 
	 * @return selected PP
	 */
	public boolean isPpSelected() {
		return !isPiSelected();
	}

	/**
	 * Setter for selectedPP
	 * 
	 * @param ppSelection
	 */
	public void setPpSelected(boolean ppSelection) {
	}

	/**
	 * Getter for selectedPI
	 * 
	 * @return the selected PI
	 */
	public boolean isPiSelected() {
		return piSelected;
	}

	/**
	 * Setter for selectedPI
	 * 
	 * @param piSelection
	 */
	public void setPiSelected(boolean piSelection) {
		this.piSelected = piSelection;
		getPiInputBox().setValue(null);
		RequestContext.getCurrentInstance().addPartialTarget(piInputBox);
		getPpInputBox().setValue(null);
		RequestContext.getCurrentInstance().addPartialTarget(ppInputBox);
	}

	/**
	 * initiate the selected value
	 * 
	 * @param piSelection
	 */
	public void initiate(boolean piSelection) {
		this.piSelected = piSelection;
	}

	/**
	 * calculates the avg PI, if a new project or category is selected
	 * 
	 * @param event
	 *            the given SelectionEvent
	 * @throws GuiServiceException
	 */
	public void calcAvgPp(SelectionEvent event) throws GuiServiceException {
		CoreTreeTable ctt = (CoreTreeTable) event.getComponent();
		Iterator<Object> it;
		Object rowKey;
		TreeUtils.selectAndUnselectChildren(event);

		// calculate average pp value
		double avg = 0;
		int pp = 0;
		it = ctt.getSelectedRowKeys().iterator();
		while (it.hasNext()) {
			rowKey = it.next();
			pp = ((PiHistoryItem) ctt.getRowData(rowKey)).getPp();
			avg += (double) pp / (double) ctt.getSelectedRowKeys().getSize();
		}
		this.avgPp = (int) Math.round(avg);
	}

	/**
	 * saves the average pi of pi history popup in the pi-field and activates it
	 * 
	 * @param event
	 *            the given ActionEvent
	 */
	public void applyPiHistoryValue(ActionEvent event) {
		this.changePiHistoryPopupVisibility(event);
		if (avgPp > 0) {
			this.setPiSelected(false);
			this.setPiBox(null);
			this.setPpBox(Integer.toString(avgPp));
			ppInputBox.setValue(getPpBox());
			RequestContext.getCurrentInstance().addPartialTarget(ppInputBox);
		}
	}

	/**
	 * setter for average pp of pi history popup
	 * 
	 * @param avgPp
	 */
	public void setAvgPp(int avgPp) {
		this.avgPp = avgPp;
	}

	/**
	 * getter for average pp of pi history popup
	 * 
	 * @return the average pp of pi history popup
	 */
	public int getAvgPp() {
		return avgPp;
	}

	public CoreInputText getPiInputBox() {
		if (piInputBox == null) {
			piInputBox = new CoreInputText();
			piInputBox.setValue(getPiBox());
		}
		return piInputBox;
	}

	public void setPiInputBox(CoreInputText piInputBox) {
		this.piInputBox = piInputBox;
	}

	public CoreInputText getPpInputBox() {
		if (ppInputBox == null) {
			ppInputBox = new CoreInputText();
			ppInputBox.setValue(getPpBox());
		}
		return ppInputBox;
	}

	public void setPpInputBox(CoreInputText ppInputBox) {
		this.ppInputBox = ppInputBox;
	}

	public String getPiBox() {

		return (getSolutionAction().getCurrentSolution() == null || getSolutionAction()
				.getCurrentSolution().getPi() == null) ? null
				: getSolutionAction().getCurrentSolution().getPi().toString();
	}

	public void setPiBox(String piBox) {
		getSolutionAction().getCurrentSolution().setPi(
				(piBox == null || piBox.isEmpty()) ? null : Integer
						.valueOf(piBox));
	}

	public String getPpBox() {
		return (getSolutionAction().getCurrentSolution() == null || getSolutionAction()
				.getCurrentSolution().getPp() == null) ? null
				: getSolutionAction().getCurrentSolution().getPp().toString();
	}

	public void setPpBox(String ppBox) {
		getSolutionAction().getCurrentSolution().setPp(
				(ppBox == null || ppBox.isEmpty()) ? null : Integer
						.valueOf(ppBox));
	}
}
