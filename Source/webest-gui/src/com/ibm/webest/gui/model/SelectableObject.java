package com.ibm.webest.gui.model;

/**
 * Container class to add a boolean selection value to an entity object.
 * @author Gregor Schumm
 */
public class SelectableObject<T> {
	private T object;
	private boolean selected;
	
	public SelectableObject() {
	}
	
	/**
	 * Initialize container with a data object.
	 * @param obj the data object
	 */
	public SelectableObject(T obj) {
		setObject(obj);
	}
	
	/**
	 * Initialize container with a data object an a selection value.
	 * @param obj the data object
	 * @param sel indicates if the object should be marked as selected
	 */
	public SelectableObject(T obj, boolean sel) {
		this(obj);
		setSelected(sel);
	}
	
	/**
	 * @return the data object hold by this container
	 */
	public T getObject() {
		return object;
	}
	
	/**
	 * @param object the data object hold by this container
	 */
	public void setObject(T object) {
		this.object = object;
	}
	
	/**
	 * Indicates whether the the object is marked as selected.
	 * @return true if selected
	 */
	public boolean isSelected() {
		return selected;
	}
	
	/**
	 * Indicates whether the the object is marked as selected.
	 * @param selected true if selected
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

}
