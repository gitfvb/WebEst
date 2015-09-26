package com.ibm.webest.gui.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJB;

import com.ibm.webest.gui.model.SelectableObject;
import com.ibm.webest.gui.utils.ManagedBeanUtils;
import com.ibm.webest.gui.utils.MessageUtils;
import com.ibm.webest.gui.utils.SelectBoxUtils;
import com.ibm.webest.persistence.model.GranularityLevel;
import com.ibm.webest.persistence.model.GranularityQuestion;
import com.ibm.webest.processing.administration.GuiServiceException;
import com.ibm.webest.processing.administration.GuiServiceLocal;
import com.ibm.webest.processing.calculation.SizingCalculatorException;
import com.ibm.webest.processing.calculation.SizingCalculatorServiceLocal;
import com.ibm.webest.processing.helpers.DefaultValues;

/**
 * Managed Bean for the Solution/Use Case Granularity tab.<br>
 * Stores state information for the granularity questions.
 * @author Gregor Schumm
 */
public class GranularityAction {
	@EJB
	private GuiServiceLocal guiService;
	@EJB
	private SizingCalculatorServiceLocal sizingCalc;
	
	private List<Map<GranularityLevel, SelectableObject<GranularityQuestion>>> granularityQuestions;
	private List<GranularityLevel> granularityLevels;
	private GranularityLevel suggestedGranularityLevel;
		
	/**
	 * Sets the granularity level, calculated with the questions to the Solution.granularity field.
	 */
	public void takeSuggestedGranularity() {
		if (processGranularityQuestions()) {
			try {
				calculateGranularitySuggestion();
				if (suggestedGranularityLevel != null)
					getSolutionAction().getCurrentSolution().setGranularity(
							suggestedGranularityLevel);
			} catch (Exception e1) {
				MessageUtils
						.addErrorMessage("Use Case Granularity: Suggestions could not be calculated. "
								+ e1.getMessage());
			}
		} else {
			MessageUtils
					.addWarnMessage("Use Case Granularity: Suggestions could not be calculated. You have to select one text in each row.");
		}

	}

	/**
	 * Generates the helper map for the selection of granularity questions.
	 * @throws GuiServiceException
	 */
	public void generateGranularityQuestionsMap() throws SizingCalculatorException, GuiServiceException {
		setGranularityQuestions(getGranularityQuestionsMapList());
	}

	/**
	 * Retrieves the calculated granularity level from the calculation service.
	 * @throws GuiServiceException
	 * @throws SizingCalculatorException 
	 */
	private void calculateGranularitySuggestion() throws SizingCalculatorException {
		suggestedGranularityLevel = sizingCalc.calcUseCaseGranularity(getSolutionAction().getCurrentSolution().getGranularityQuestions());
	}

	/**
	 * Reads the questions helper map and add all selected questions to the current solution.
	 * @return true if user has selected one question in each row
	 */
	public boolean processGranularityQuestions() {
		getSolutionAction().getCurrentSolution().getGranularityQuestions().clear();
		int count = 0;
		for (Map<GranularityLevel, SelectableObject<GranularityQuestion>> map : granularityQuestions) {
			for (SelectableObject<GranularityQuestion> so : map.values()) {
				if (so.isSelected()) {
					getSolutionAction().getCurrentSolution()
							.getGranularityQuestions().add(so.getObject());
					count++;
					break;
				}
			}
		}
		return granularityQuestions.size() == count;
	}

	/**
	 * Retrieves granularity questions from database and creates a helper map for to make questions selectable.
	 * @return the questions helper map
	 * @throws GuiServiceException
	 */
	public List<Map<GranularityLevel, SelectableObject<GranularityQuestion>>> getGranularityQuestionsMapList()
			throws GuiServiceException {
		// default values are externalized to processing/helpers/default-values.properties
		String[] fns = DefaultValues.getStringArray("gra_quest_row_order");
		Map<String, Map<GranularityLevel, GranularityQuestion>> map = guiService
				.getAllGranularityQuestions();
		List<GranularityLevel> granularityLevels = getGranularityLevels();
		List<Map<GranularityLevel, SelectableObject<GranularityQuestion>>> lst = new ArrayList<Map<GranularityLevel, SelectableObject<GranularityQuestion>>>(
				map.size());
		Set<GranularityQuestion> selected = new HashSet<GranularityQuestion>(
				getSolutionAction().getCurrentSolution().getGranularityQuestions());
		for (String fn : fns) {
			Map<GranularityLevel, GranularityQuestion> subMap = map.get(fn);
			Map<GranularityLevel, SelectableObject<GranularityQuestion>> values = new HashMap<GranularityLevel, SelectableObject<GranularityQuestion>>(
					granularityLevels.size());
			for (GranularityLevel level : granularityLevels) {
				GranularityQuestion question = subMap.get(level);
				values.put(level, new SelectableObject<GranularityQuestion>(
						question, selected.contains(question)));
			}
			lst.add(values);
		}
		return lst;
	}

	/**
	 * @return all granularity levels for dropdown boxes
	 * @throws GuiServiceException
	 */
	public Map<String, GranularityLevel> getGranularityLevelsMap()
			throws GuiServiceException {
		return SelectBoxUtils.createSelectItemsMap(getGranularityLevels(),
				GranularityLevel.class, null, "name");
	}

	/**
	 * @return all granularity levels
	 * @throws GuiServiceException
	 */
	public List<GranularityLevel> getGranularityLevels()
			throws GuiServiceException {
		if (granularityLevels == null)
			granularityLevels = guiService.getAllGranularityLevels();

		return granularityLevels;
	}

	/**
	 * @param granularityQuestions granularity questions for the ui
	 */
	public void setGranularityQuestions(
			List<Map<GranularityLevel, SelectableObject<GranularityQuestion>>> granularityQuestions) {
		this.granularityQuestions = granularityQuestions;
	}

	/**
	 * @return granularity questions for the ui
	 */
	public List<Map<GranularityLevel, SelectableObject<GranularityQuestion>>> getGranularityQuestions() {
		return granularityQuestions;
	}

	/**
	 * @return the current solutionAction managed bean
	 */
	private SolutionAction getSolutionAction() {
		return ManagedBeanUtils.getBean(
				SolutionAction.class, "solutionAction");
	}
	
}
