package com.ibm.webest.gui.model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.apache.myfaces.trinidad.model.ChartModel;

import com.ibm.webest.gui.action.ReportAction;
import com.ibm.webest.gui.utils.ManagedBeanUtils;

/**
 * Model that manages the pnr curve data to represent it in the report 
 * @author Dirk Kohlweyer
 */
public class PnrCurveModel extends ChartModel {
	
	/**
	 * getGroupLabels: get current group labels 
	 */
	public List<String> getGroupLabels() {
		try {
			return ManagedBeanUtils.getBean(ReportAction.class, "reportAction").getCurrentReport().getRayleighResults().getxAxis();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * getMaxXValue: a trinidat specific preference to sync the x values with the drawings
	 */
	public Double getMaxXValue() {
		try {	
			return new Double(ManagedBeanUtils.getBean(ReportAction.class, "reportAction").getCurrentReport().getRayleighResults().getxValues().size());
		} catch (Exception e) {
			e.printStackTrace();
			return 0.0;
		}
	}
	/**
	 * getMinXValue: a trinidat specific preference to sync the x values with the drawings 
	 */
	@Override
	public Double getMinXValue() {
		return new Double(1);
	}

	/**
	 * getSeriesLabels: set up the labels for the curves 
	 */
	public List<String> getSeriesLabels() {
		List<String> seriesLabels = new ArrayList<String>();
		seriesLabels.add("PNR curve");
		seriesLabels.add("Project Duration");
		return seriesLabels;
	}
	
	/**
	 * getSeriesClolor: set up the two different colors for td and 
	 * the staffing curve in an array for trinidat
	 */
	public List<Color> getSeriesColors() {
		List<Color> seriesColors = new ArrayList<Color>();
		seriesColors.add(Color.BLUE);
		seriesColors.add(Color.RED);
		return seriesColors;
	}
	
	public String getTitle() { 
		return "Staffing Curve";
	}
	
	/**
	 * getYValues: get calculated y values from calculated report 
	 */
	public List<List<Double>> getYValues() {
		try {
			return ManagedBeanUtils.getBean(ReportAction.class, "reportAction").getCurrentReport().getRayleighResults().getyValues();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	/** 
	 * getXValues: gets calculated x values from calculated report 
	 */
	public List<List<Double>> getXValues() {
		try {
			return ManagedBeanUtils.getBean(ReportAction.class, "reportAction").getCurrentReport().getRayleighResults().getxValues();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
	