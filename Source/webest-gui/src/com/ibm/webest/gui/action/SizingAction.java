package com.ibm.webest.gui.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.component.UIParameter;
import javax.faces.event.ActionEvent;

import org.apache.myfaces.trinidad.component.core.data.CoreTreeTable;
import org.apache.myfaces.trinidad.context.RequestContext;
import org.apache.myfaces.trinidad.event.SelectionEvent;
import org.apache.myfaces.trinidad.model.ChildPropertyTreeModel;
import org.apache.myfaces.trinidad.model.TreeModel;

import com.ibm.webest.gui.model.UseCaseItem;
import com.ibm.webest.gui.utils.ManagedBeanUtils;
import com.ibm.webest.gui.utils.MessageUtils;
import com.ibm.webest.gui.utils.TreeUtils;
import com.ibm.webest.persistence.model.UseCase;
import com.ibm.webest.persistence.model.UseCasePack;
import com.ibm.webest.processing.administration.GuiServiceException;
import com.ibm.webest.processing.calculation.SizingCalculatorException;
import com.ibm.webest.processing.calculation.SizingCalculatorServiceLocal;
import com.ibm.webest.processing.helpers.DefaultValues;

/**
 * Managed bean to store objects for sizing treetable
 * and handle fired events like adding/deleting
 * use cases or use case packs.
 * 
 * @author Florian Friedrichs
 * @author Gregor Schumm
 */
public class SizingAction {
	
	// constants for saving to and loading from pageflowscope
	private final String SIZINGTREE = "SIZING_TREE";
	private final String SIZINGTABLE = "SIZING_TABLE";
	private final String SIZINGNEWUSECASES = "SIZING_NEW_USE_CASES";
	
	@EJB
	private SizingCalculatorServiceLocal sizingCalculator;
	
	/**
	 * Adds an use case with default values to an use case pack
	 * @param event given event of site
	 */
	public void addUseCase(ActionEvent event) {		

		// get the use case pack where use case packs will be added
		UIParameter param = (UIParameter) event.getComponent().findComponent("node");
		UseCasePack useCasePack = (UseCasePack) ((UseCaseItem)param.getValue()).getObject();
		
		// create new uses cases - the amount of new use cases is setted with setNewUseCases in gui 
		List<UseCaseItem> useCaseItemList = new ArrayList<UseCaseItem>();
		for (int i = 0; i < this.getNewUseCases(); i++) {
			UseCase u = new UseCase();
			u.setName("");
			u.setAssumptions("");
			u.setMultiplier(DefaultValues.getDouble("use_case_multiplier"));
			u.setInScope(DefaultValues.getBoolean("use_case_in_scope"));
			u.setCertainty(null);
			u.setUseCaseComplexity(null);
			u.setGranularityOverride(null);
			u.setUseCasePack(useCasePack);
			useCaseItemList.add(new UseCaseItem(u));	
		}
		
		// now add the new uses cases to use case pack
		((UseCaseItem)param.getValue()).getChildren().addAll(useCaseItemList);
		
		// remove the whole selection and refresh it afterwards
		getTable().getSelectedRowKeys().clear();
		RequestContext.getCurrentInstance().addPartialTarget(getTable());
	}
	
	/**
	 * Adds an use case pack
	 */
	public void addUseCasePack() {
		UseCaseItem uci = getSizing();
		
		UseCasePack u = new UseCasePack();
		u.setName("");
		u.setSolution(getSolutionAction().getCurrentSolution());
		u.setUseCases(new ArrayList<UseCase>());
		((List<UseCaseItem>)uci.getChildren()).add(new UseCaseItem(u));
		
		getTable().getSelectedRowKeys().clear();
		RequestContext.getCurrentInstance().addPartialTarget(getTable());
	}
	
	/**
	 * Deletes selected items (use cases and use case packs).
	 * If a use case pack is selected, the children uses cases
	 * will be deleted, too.
	 */
	public void delete() {		
		
		// store all objects to delete in a list
		Iterator<Object> selection = getTable().getSelectedRowKeys().iterator();
		List<UseCaseItem> uList = new ArrayList<UseCaseItem>();		
		while (selection.hasNext()) {
			Object rowKey = selection.next();			
			UseCaseItem u = (UseCaseItem)getTable().getRowData(rowKey);	
			uList.add(u); // delete usecaseitems later to avoid problems
		}
		
		// delete the usecasepacks and usecases with iterators
		Iterator<UseCaseItem> useCasePacks = getSizing().getChildren().iterator();
		while (useCasePacks.hasNext()) {
			UseCaseItem useCasePack = useCasePacks.next();
			if (uList.contains(useCasePack)) {
				useCasePacks.remove();
			} else {
				Iterator<UseCaseItem> useCases = useCasePack.getChildren().iterator();
				while (useCases.hasNext()) {
					UseCaseItem useCase = useCases.next();
					if (uList.contains(useCase))
						useCases.remove();
				}
			}						
		}
		
		// remove all selections in table
		getTable().getSelectedRowKeys().clear();
		RequestContext.getCurrentInstance().addPartialTarget(getTable());		
	}

	/**
	 * Fills the use case list with all usecases of usecasepacks
	 * @throws GuiServiceException
	 */
	public void fillUseCaseList() throws GuiServiceException {
		this.setSizing(new UseCaseItem(getSolutionAction().getCurrentSolution().getUseCasePacks()));
		setTable(new CoreTreeTable());
		calculateUucpAndTotal();
		setNewUseCases(5);
	}
	
	/**
	 * Get list of use case packs (with use cases as children). 
	 * @return list of use case packs (with use cases as children)
	 */
	public List<UseCasePack> getUseCasePacks() {
		List<UseCasePack> ucpList = new ArrayList<UseCasePack>();
		// iterate over usecasepacks (second level of tree)
		for (UseCaseItem uciPack : getSizing().getChildren()) {
			UseCasePack ucp = (UseCasePack)uciPack.getObject();
			ucp.setUseCases(new ArrayList<UseCase>());
			// iterate over usecases (third level)
			for (UseCaseItem uciCase : uciPack.getChildren()) {
				UseCase uc = (UseCase)uciCase.getObject();
				uc.setUseCasePack(ucp);
				ucp.getUseCases().add(uc);
			}
			ucpList.add(ucp);
		}
		return ucpList;
	}
	
	/**
	 * Selection Listener for the tree table. If a event occurs, it will select/unselect children and parents
	 * @param event
	 */
	public void selectRows(SelectionEvent event) {
		TreeUtils.selectAndUnselectChildren(event);
	}
	
	/**
	 * Recalculate uucp and total in table
	 */
	public void calculateUucpAndTotal() {
		getSizing().reCalc();
		RequestContext.getCurrentInstance().addPartialTarget(getTable());
	}
	
	/**
	 * Getter for the solution session.
	 * @return the current solution session.
	 */
	public SolutionAction getSolutionAction() {
		return ManagedBeanUtils.getBean(SolutionAction.class, "solutionAction");
	}
	
	/**
	 * Setter for quantity of new uses cases, if the user decides to add new use cases.
	 * The value will be saved in pageflowscope.
	 * @param newUseCases quantity of new uses cases to add
	 */
	public void setNewUseCases(int newUseCases) {
		RequestContext.getCurrentInstance().getPageFlowScope().put(SIZINGNEWUSECASES, newUseCases);
	}

	/**
	 * Getter for quantity of new uses cases, if the user decides to add new use cases.
	 * The value will be loaded from pageflowscope.
	 * @return quantity of new uses cases to add
	 */
	public int getNewUseCases() {
		return (Integer)RequestContext.getCurrentInstance().getPageFlowScope().get(SIZINGNEWUSECASES);
	}

	/**
	 * get the use case table
	 * @return the trinidad CoreTreeTable-Object
	 */
	public CoreTreeTable getTable() {
		return (CoreTreeTable)RequestContext.getCurrentInstance().getPageFlowScope().get(SIZINGTABLE);
	}
	
	/**
	 * set the use case table
	 * @param table the trinidad CoreTreeTable-Object
	 */
	public void setTable(CoreTreeTable table) {
		RequestContext.getCurrentInstance().getPageFlowScope().put(SIZINGTABLE, table);
	}
	
	/**
	 * setter for the custom tree-table
	 * @param useCaseItem the root UseCaseItem with children
	 */
	public void setSizing(UseCaseItem useCaseItem) {
		RequestContext.getCurrentInstance().getPageFlowScope().put(SIZINGTREE, useCaseItem);
	}
	
	/**
	 * getter for the custom tree-table
	 * @return the root UseCaseItem with children
	 */
	public UseCaseItem getSizing() {
		return (UseCaseItem)RequestContext.getCurrentInstance().getPageFlowScope().get(SIZINGTREE);
	}
	
	/**
	 * getter for the specified tree-table-model
	 * @return the tree-table-model, based on root UseCaseItem
	 */
	public TreeModel getModel() {
		getSizing().reCalc();
		return new ChildPropertyTreeModel(this.getSizing(), "children");
	}


	/**
	 * Invokes size calculator service to calculate uucp for the given use case.
	 * @param useCase
	 * @return the uucp of the given use case
	 */
	public int calcUucp(UseCase useCase) {
		try {
			return sizingCalculator.calcUCP(useCase, getSolutionAction().getCurrentSolution().getGranularity());
		} catch (SizingCalculatorException e) {
			MessageUtils.addErrorMessage("UUCP could not be calculated: "+e.getMessage());
			return 0;
		}
	}
	
}
