package com.ibm.webest.gui.action;

import java.util.ArrayList;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import org.apache.myfaces.trinidad.model.ChildPropertyTreeModel;
import org.apache.myfaces.trinidad.model.TreeModel;

import com.ibm.webest.gui.model.PiHistoryItem;
import com.ibm.webest.gui.utils.ManagedBeanUtils;
import com.ibm.webest.gui.utils.SelectBoxUtils;
import com.ibm.webest.persistence.model.ApplicationType;
import com.ibm.webest.persistence.model.ApplicationTypeGroup;
import com.ibm.webest.persistence.model.Certainty;
import com.ibm.webest.persistence.model.Country;
import com.ibm.webest.persistence.model.Division;
import com.ibm.webest.persistence.model.EffortUnit;
import com.ibm.webest.persistence.model.Estimate;
import com.ibm.webest.persistence.model.GranularityLevel;
import com.ibm.webest.persistence.model.IndustrySector;
import com.ibm.webest.persistence.model.IndustrySectorGroup;
import com.ibm.webest.persistence.model.MTTDTimeUnit;
import com.ibm.webest.persistence.model.MonetaryUnit;
import com.ibm.webest.persistence.model.Organization;
import com.ibm.webest.persistence.model.Phase;
import com.ibm.webest.persistence.model.StaffingShape;
import com.ibm.webest.persistence.model.Template;
import com.ibm.webest.persistence.model.UseCaseComplexity;
import com.ibm.webest.persistence.model.User;
import com.ibm.webest.processing.administration.EstimationManagerException;
import com.ibm.webest.processing.administration.GuiServiceException;
import com.ibm.webest.processing.administration.GuiServiceLocal;

/**
 * Managed bean, responsible for providing data for the project description form.
 * @author Gregor Schumm
 *
 */
public class ProjectDescriptionAction {
	@EJB
	private GuiServiceLocal guiService;
	
	private Organization selectedOrganization;
	private ApplicationTypeGroup selectedApplicationTypeGroup;
	private IndustrySectorGroup selectedIndustrySectorGroup;
	
	/**
	 * @return all available organizations as label->object map for selectItems element
	 * @throws GuiServiceException
	 */
	public Map<String, Organization> getOrganizations() throws GuiServiceException {
		Map<String, Organization> orga = SelectBoxUtils.createSelectItemsMap(guiService.getAllOrganizations(), Organization.class, null, "name");
		if (selectedOrganization == null) {
			Estimate est  = ManagedBeanUtils.getBean(EstimateAction.class, "estimateAction").getCurrentEstimate();
			if (est.getDivision() == null) {
				if (orga.values().iterator().hasNext())
					selectedOrganization = orga.values().iterator().next();
			} else {					
				selectedOrganization = est.getDivision().getOrganization();
			}
		}		
		return orga;
	}
	/**
	 * @return all available divisions of a selected organization
	 * @throws GuiServiceException
	 */
	public Map<String, Division> getDivisions() throws GuiServiceException {
		return SelectBoxUtils.createSelectItemsMap(getSelectedOrganization() == null ? new ArrayList<Division>(0) : getSelectedOrganization().getDivisions(), Division.class, null, "name");
	}
	/**
	 * @return all available estimators as label->object map for selectItems element
	 * @throws GuiServiceException
	 */
	public Map<String, User> getEstimators() throws GuiServiceException {
		return SelectBoxUtils.createSelectItemsMap(guiService.getAllEstimators(), User.class, "%s, %s", "lastName", "firstName");
	}
	/**
	 * @return all available countries as label->object map for selectItems element
	 * @throws GuiServiceException
	 */
	public Map<String, Country> getCountries() throws GuiServiceException {
		return SelectBoxUtils.createSelectItemsMap(guiService.getAllCountries(), Country.class, null, "name");
	}
	/**
	 * @return all available industry sector groups
	 * @throws GuiServiceException
	 */
	public Map<String, IndustrySectorGroup> getIndustrySectorGroups() throws GuiServiceException {
		Map<String, IndustrySectorGroup> industry = SelectBoxUtils.createSelectItemsMap(guiService.getAllIndustrySectorGroups(), IndustrySectorGroup.class, null, "name");		
		if (selectedIndustrySectorGroup == null) {
			Estimate est  = ManagedBeanUtils.getBean(EstimateAction.class, "estimateAction").getCurrentEstimate();
			if (est.getProjectEnvironment().getIndustrySector() == null) {
				if (industry.values().iterator().hasNext())
					selectedIndustrySectorGroup = industry.values().iterator().next();
			} else {
				selectedIndustrySectorGroup = est.getProjectEnvironment().getIndustrySector().getGroup();
			}
		}			
		return industry;
	}
	/**
	 * @return all available industry sectors of a selected industry sector group
	 * @throws GuiServiceException
	 */
	public Map<String, IndustrySector> getIndustrySectors() throws GuiServiceException {
		return SelectBoxUtils.createSelectItemsMap(getSelectedIndustrySectorGroup() == null ? new ArrayList<IndustrySector>(0) :getSelectedIndustrySectorGroup().getIndustrySectors(), IndustrySector.class, null, "name");
	}
	/**
	 * @return all available application type groups
	 * @throws GuiServiceException
	 */
	public Map<String, ApplicationTypeGroup> getApplicationTypeGroups() throws GuiServiceException {
		Map<String, ApplicationTypeGroup> apptype = SelectBoxUtils.createSelectItemsMap(guiService.getAllApplicationTypeGroups(), ApplicationTypeGroup.class, null, "name");		
		if (selectedApplicationTypeGroup == null) {
			Estimate est  = ManagedBeanUtils.getBean(EstimateAction.class, "estimateAction").getCurrentEstimate();
			if (est.getProjectEnvironment().getApplicationType() == null) {
				if (apptype.values().iterator().hasNext())
					selectedApplicationTypeGroup = apptype.values().iterator().next();
			} else {
				selectedApplicationTypeGroup = est.getProjectEnvironment().getApplicationType().getGroup();		
			}
		}			
		return apptype;
	}
	/**
	 * @return the available application types of application type group
	 * @throws GuiServiceException
	 */
	public Map<String, ApplicationType> getApplicationTypes() throws GuiServiceException {
		return SelectBoxUtils.createSelectItemsMap(getSelectedApplicationTypeGroup() == null ? new ArrayList<ApplicationType>(0) :getSelectedApplicationTypeGroup().getApplicationTypes(), ApplicationType.class, null, "name");
	}
	/**
	 * @return all available monetary units as label->object map for selectItems element
	 * @throws GuiServiceException
	 */
	public Map<String, MonetaryUnit> getMonetaryUnits() throws GuiServiceException {
		return SelectBoxUtils.createSelectItemsMap(guiService.getAllMonetaryUnits(), MonetaryUnit.class, "%s (%s)", "id", "name");
	}
	/**
	 * @return all available mttd time units as label->object map for selectItems element
	 * @throws GuiServiceException
	 */
	public Map<String, MTTDTimeUnit> getMttdTimeUnits() throws GuiServiceException {
		return SelectBoxUtils.createSelectItemsMap(guiService.getAllMttdTimeUnits(), MTTDTimeUnit.class, "%s (%s)", "name", "id");
	}
	/**
	 * @return all available effort units as label->object map for selectItems element
	 * @throws GuiServiceException
	 */
	public Map<String, EffortUnit> getEffortUnits() throws GuiServiceException {
		return SelectBoxUtils.createSelectItemsMap(guiService.getAllEffortUnits(), EffortUnit.class, "%s (%s)", "name", "id");
	}
	
	/**
	 * Action listener for changes of organization field.<br>
	 * @param event
	 * @throws GuiServiceException 
	 */
	public void organizationChanged(ValueChangeEvent event) {
		setSelectedOrganization((Organization)event.getNewValue());
		FacesContext.getCurrentInstance().renderResponse();
	}

	/**
	 * @param selectedOrganization currently selected organization
	 * @throws GuiServiceException 
	 */
	public void setSelectedOrganization(Organization selectedOrganization) {
		this.selectedOrganization = selectedOrganization;
	}

	/**
	 * @return currently selected organization
	 */
	public Organization getSelectedOrganization() {
		return selectedOrganization;
	}
	/**
	 * Action listener for changes of application type group field.<br>
	 * @param event
	 */
	public void applicationTypeGroupChanged(ValueChangeEvent event) {		
		setSelectedApplicationTypeGroup((ApplicationTypeGroup)event.getNewValue());
		FacesContext.getCurrentInstance().renderResponse();
	}
	/**
	 * @param selectedApplicationTypeGroup currently selected application type group
	 */
	public void setSelectedApplicationTypeGroup(
			ApplicationTypeGroup selectedApplicationTypeGroup) {
		this.selectedApplicationTypeGroup = selectedApplicationTypeGroup;
	}
	/**
	 * @return currently selected application type group
	 */
	public ApplicationTypeGroup getSelectedApplicationTypeGroup() {
		return selectedApplicationTypeGroup;
	}
	/**
	 * Action listener for changes of industry sector group field.<br>
	 * @param event
	 */
	public void industrySectorGroupChanged(ValueChangeEvent event) {
		setSelectedIndustrySectorGroup((IndustrySectorGroup)event.getNewValue());
		FacesContext.getCurrentInstance().renderResponse();
	}
	/**
	 * @param selectedIndustrySectorGroup currently selected industry sector group
	 */
	public void setSelectedIndustrySectorGroup(
			IndustrySectorGroup selectedIndustrySectorGroup) {
		this.selectedIndustrySectorGroup = selectedIndustrySectorGroup;
	}
	/**
	 * @return currently selected industry sector group
	 */
	public IndustrySectorGroup getSelectedIndustrySectorGroup() {
		return selectedIndustrySectorGroup;
	}
	
	/**
	 * get all templates for estimates
	 * @return map of all templates with showed text in dropdown-box like "name (description)" 
	 * @throws GuiServiceException
	 */
	public Map<String, Template> getTemplates() throws GuiServiceException {
		return SelectBoxUtils.createSelectItemsMap(guiService.getAllTemplates(), Template.class, null, "name");
	}
	
	/**
	 * get all phases for selected estimate
	 * @return map of all phases of a estimate with name shown
	 * @throws GuiServiceException
	 */
	public Map<String, Phase> getPhases() throws GuiServiceException {
		return SelectBoxUtils.createSelectItemsMap(ManagedBeanUtils.getBean(EstimateAction.class, "estimateAction").getCurrentEstimate().getProjectEnvironment().getPhases()
				, Phase.class, null, "name");
	}
	
	/**
	 * get all usecase complexities for solutions
	 * @return map of all usecase complexities of a estimate with name shown
	 * @throws GuiServiceException
	 */
	public Map<String, UseCaseComplexity> getUseCaseComplexities() throws GuiServiceException {
		return SelectBoxUtils.createSelectItemsMap(guiService.getAllUseCaseComplexities(), UseCaseComplexity.class, null, "name");
	}
	
	/**
	 * get all usecase certainties for solutions
	 * @return map of all certainties of a estimate with name shown
	 * @throws GuiServiceException
	 */
	public Map<String, Certainty> getCertainties() throws GuiServiceException {
		return SelectBoxUtils.createSelectItemsMap(guiService.getAllCertainties(), Certainty.class, null, "name");
	}
	
	/**
	 * get all usecase granularity levels for solutions
	 * @return map of all granularity levels of a estimate with name shown
	 * @throws GuiServiceException
	 */
	public Map<String, GranularityLevel> getGranularityOverrides() throws GuiServiceException {
		return SelectBoxUtils.createSelectItemsMap(guiService.getAllGranularityLevels(), GranularityLevel.class, null, "name");
	}
	

	/**
	 * get a treetable for pi history for gui. Saves the categories in an extended object of pihistoryentry. Then the
	 * transformed categories will be saved in a root node with value null.
	 * @return TreeModel for the PiHistoryTree, it can be used with trinidad-component <tr:treeTable>
	 * @throws EstimationManagerException
	 * @throws GuiServiceException
	 */
	public TreeModel getPiHistoryTree() throws EstimationManagerException, GuiServiceException {
		return new ChildPropertyTreeModel(new PiHistoryItem(guiService.getAllPiHistoryCategories()), "children");
	}
	
	/**
	 * get all possible staffing shapes for selection in solution
	 * @return map of all staffing shapes with name as key and staffing shape as value
	 * @throws GuiServiceException
	 */
	public Map<String, StaffingShape> getStaffingShapes() throws GuiServiceException {
		return SelectBoxUtils.createSelectItemsMap(guiService.getAllStaffingShapes(), StaffingShape.class, null, "name");
	}
	
}
