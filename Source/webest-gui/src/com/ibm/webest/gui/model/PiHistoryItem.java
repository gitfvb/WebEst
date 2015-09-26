package com.ibm.webest.gui.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.ibm.webest.persistence.model.PiHistoryCategory;
import com.ibm.webest.persistence.model.PiHistoryEntry;

/**
 * This class helps to create a tree for the PI History in GUI.
 * You should initialize it with a list of piHistoryCategories. Then the whole tree
 * will build by itself, because a PiHistoryCategory contains all child-elements
 * (PiHistoryEntry) as a list.
 * The class also stores the pp and effort, if the stored object is an instance
 * of PiHistoryEntry. Otherwise it will calculate the average pp and effort of child-elements.
 * The PI will be calculated for all kinds of objects.
 * 
 * @author Florian Friedrichs
 */
public class PiHistoryItem implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private Object object;
	private List<PiHistoryItem> children;
	private int pp;
	private double effort;
	
	/**
	 * Constructor for the root node (first level in tree)
	 * 
	 * @param piHistoryCategories
	 *            saves the list of piHistoryCategories as an PiHistoryItem. This
	 *            list builds the tree recursive.
	 */
	public PiHistoryItem(List<PiHistoryCategory> piHistoryCategories) {
		this.object = null;
		List<PiHistoryItem> piHistoryList = new ArrayList<PiHistoryItem>();
		for (PiHistoryCategory p : piHistoryCategories)
			piHistoryList.add(new PiHistoryItem(p));
		this.setChildren(piHistoryList);
	}
	
	/**
	 * Constructor for a PiHistoryCategory (second level in tree)
	 * 
	 * @param piHistoryCategory
	 *            saves the PiHistoryCategory as a PiHistoryItem
	 */
	public PiHistoryItem(PiHistoryCategory piHistoryCategory) {
		this.object = piHistoryCategory;
		List<PiHistoryItem> piHistoryList = new ArrayList<PiHistoryItem>();
		for (PiHistoryEntry p : piHistoryCategory.getPiHistoryEntries())
			piHistoryList.add(new PiHistoryItem(p));
		this.pp = calcAveragePp(piHistoryCategory.getPiHistoryEntries());
		this.effort = calcAverageEffort(piHistoryCategory.getPiHistoryEntries());
		this.setChildren(piHistoryList);
	}
	
	/**
	 * Constructor for a PiHistoryEntry (third level in tree)
	 * 
	 * @param piHistoryEntry
	 *            the PiHistoryEntry to save it as a PiHistoryItem
	 */
	public PiHistoryItem(PiHistoryEntry piHistoryEntry) {
		this.object = piHistoryEntry;
		this.pp = piHistoryEntry.getPp();
		this.effort = piHistoryEntry.getEffort();
		this.setChildren(null);
	}
	
	/**
	 * calculates the average Pi for categories
	 * @param list
	 * @return rounded average pi value
	 */
	private int calcAveragePp(List<PiHistoryEntry> list) {
		double avg = 0;
		for (PiHistoryEntry e : list)
			avg += (double) e.getPp() / (double) list.size();
		return (int) Math.round(avg);
	}
	
	/**
	 * calculates the average effort for categories
	 * @param list
	 * @return rounded average effort value
	 */
	private double calcAverageEffort(List<PiHistoryEntry> list) {
		double avg = 0.0;
		for (PiHistoryEntry e : list)
			avg += e.getEffort() / (double) list.size();
		return avg;
	}
	
	/**
	 * setter for the object. This should be null (the root) or a instance of PiHistoryCategory or PiHistoryEntry.
	 * @param object should be null (the root) or a instance of PiHistoryCategory or PiHistoryEntry
	 */
	public void setObject(Object object) {
		this.object = object;
	}

	/**
	 * getter for the for the object. This could be a null (the root), a PiHistoryCategory or a PiHistoryEntry.
	 * @return the object
	 */
	public Object getObject() {
		return object;
	}
	
	/**
	 * setter for a value
	 * @param pp integer value for pp
	 */
	public void setPp(int pp) {
		this.pp = pp;
	}
	
	/**
	 * getter for the pp value
	 * @return pp value
	 */
	public int getPp() {
		return pp;
	}
	
	/**
	 * setter for the children of this PiHistoryItem. If Object is a PiHistoryEntry, you should set this null
	 * @param children list of PiHistoryItem. You should set this null, if object is a instance of PiHistoryEntry
	 */
	public void setChildren(List<PiHistoryItem> children) {
		this.children = children;
	}
	
	/**
	 * getter for the children of this PiHistoryItem. If Object is a PiHistoryEntry, children is null
	 * @return children of PiHistoryItem. null, if object is a instance of PiHistoryEntry
	 */
	public List<PiHistoryItem> getChildren() {
		return children;
	}
	
	/**
	 * setter for an effort value
	 * @param effort double value for effort
	 */
	public void setEffort(double effort) {
		this.effort = effort;
	}
	
	/**
	 * getter for the effort value
	 * @return effort value
	 */
	public double getEffort() {
		return effort;
	}
	
	
}
