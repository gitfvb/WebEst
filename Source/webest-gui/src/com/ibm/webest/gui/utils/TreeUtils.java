package com.ibm.webest.gui.utils;

import java.util.Iterator;

import org.apache.myfaces.trinidad.component.core.data.CoreTreeTable;
import org.apache.myfaces.trinidad.event.SelectionEvent;

public class TreeUtils {
	
	/**
	 * With this method, the children of a selected/unselected parent item in a tree will be selected/unselected.
	 * If a child is unselected, the parent container will be unselected true.
	 * @param event the fired SelectionEvent of a table or treetable
	 */
	public static void selectAndUnselectChildren(SelectionEvent event) {
		CoreTreeTable ctt = (CoreTreeTable) event.getComponent();
		Iterator<Object> it;
    	Object rowKey;
		
		// Select children of container
    	it = event.getAddedSet().iterator();
    	while (it.hasNext()) {
    		rowKey = it.next();
    		ctt.setRowKey(rowKey);
    		if (ctt.isContainer()) {
    			ctt.enterContainer();    			
    			for (int i = 0; i < ctt.getRowCount(); i++) {
    				ctt.setRowIndex(i);
    				ctt.getSelectedRowKeys().add();
    			}    			
    			ctt.exitContainer();
    		}	
    	}
    	
    	// Unselect children of container
    	it = event.getRemovedSet().iterator();
    	while (it.hasNext()) {
    		rowKey = it.next();
    		ctt.setRowKey(rowKey);
    		if (ctt.isContainer()) {
    			ctt.enterContainer();    			
    			for (int i = 0; i < ctt.getRowCount(); i++) {
    				ctt.setRowIndex(i);
    				ctt.getSelectedRowKeys().remove();
    			}    			
    			ctt.exitContainer();
    		} else {
    			// unselect container, if one child is unselected
    			ctt.setRowKey(ctt.getContainerRowKey());
    			ctt.getSelectedRowKeys().remove();
    		}
    	}
	}
	
}
