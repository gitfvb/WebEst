package com.ibm.webest.persistence.model;

import java.util.Collection;

/**
 * This interface provides the functionality for entities to be represented in a
 * hierarchical form.<br>
 * Entities which has a parent->children relationship should implement this
 * interface.<br>
 * Note: both, parent and child has to implement it
 * 
 * @author Gregor Schumm
 * 
 */
public interface TreeItem {
	/**
	 * @return the collection of child items or null if entity is a leaf
	 */
	public Collection<? extends TreeItem> getChildren();

	/**
	 * @return the representative name of the current item
	 */
	public String getName();
}
