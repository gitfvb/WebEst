package com.ibm.webest.gui.utils;

import java.util.Map;

import com.ibm.webest.persistence.model.GranularityQuestion;

/**
 * Helper class to save a map of granularity questions with on granularity
 * question to store the selected question in it.
 * 
 * @author Florian Friedrichs
 * 
 */
public class GranularityUtils {

	private GranularityQuestion question;
	private Map<String, GranularityQuestion> map;

	/**
	 * 
	 * @param question
	 *            the granularity question that will be changed, if someone
	 *            selects one question
	 * @param map
	 *            this map holds all granularity questions for to show them in
	 *            gui
	 */
	public GranularityUtils(GranularityQuestion question,
			Map<String, GranularityQuestion> map) {
		this.question = question;
		this.map = map;
	}

	/**
	 * getter for the question
	 * 
	 * @return Granularity Question
	 */
	public GranularityQuestion getQuestion() {
		return question;
	}

	/**
	 * setter for the question
	 * 
	 * @param question
	 */
	public void setQuestion(GranularityQuestion question) {
		this.question = question;
	}

	/**
	 * getter for the map of granularity questions. The key of this Map is the
	 * question as string. The value is the question-object itself
	 * 
	 * @return a map of granularity questions
	 */
	public Map<String, GranularityQuestion> getMap() {
		return map;
	}

	/**
	 * setter for the map of granularity questions. The key of this Map is the
	 * question as string. The value is the question-object itself
	 * 
	 * @param map
	 */
	public void setMap(Map<String, GranularityQuestion> map) {
		this.map = map;
	}

}
