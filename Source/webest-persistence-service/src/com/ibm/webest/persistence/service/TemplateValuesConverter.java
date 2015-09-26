package com.ibm.webest.persistence.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.ibm.webest.persistence.model.DefectCategory;
import com.ibm.webest.persistence.model.Milestone;
import com.ibm.webest.persistence.model.Phase;
import com.ibm.webest.persistence.model.TemplateValues;

/**
 * Utility class to convert an instance of TemplateValues to another, e.g. to
 * convert a Template to a ProjectEnvironment.
 * 
 * @author Gregor Schumm
 * 
 */
public final class TemplateValuesConverter {

	/**
	 * Converts instance <code>in</code> to a new instance of type
	 * <code>outClass</code>.
	 * 
	 * @param in
	 *            the original object
	 * @param outClass
	 *            the type of the new object
	 * @return the converted object
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public static <T extends TemplateValues> T convert(TemplateValues in,
			Class<T> outClass) throws IllegalAccessException,
			InstantiationException, IllegalArgumentException,
			InvocationTargetException {
		T out = outClass.newInstance();
		convert(in, out);
		return out;
	}

	/**
	 * Copies all values from <code>in</code> to the instance of
	 * <code>out</code>.
	 * 
	 * @param in
	 *            the original object
	 * @param out
	 *            the type of the new object
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public static <T extends TemplateValues> void convert(TemplateValues in,
			T out) {
		out.setDaysPerWeek(in.getDaysPerWeek());
		out.setHoursPerDay(in.getHoursPerDay());
		Map<Phase, Phase> phaseMapping = new Hashtable<Phase, Phase>();
		List<Phase> phases;
		if (out.getPhases() != null) {
			phases = out.getPhases();
			phases.clear();
		} else {
			phases = new ArrayList<Phase>();
		}
		phases.addAll(clonePhases(in.getPhases(), out, phaseMapping));
		out.setPhases(phases);

		List<Milestone> milestones;
		if (out.getMilestones() != null) {
			milestones = out.getMilestones();
			milestones.clear();
		} else {
			milestones = new ArrayList<Milestone>();
		}
		milestones
				.addAll(cloneMilestones(in.getMilestones(), phaseMapping, out));
		out.setMilestones(milestones);
		List<DefectCategory> defectCategories;
		if (out.getDefectCategories() != null) {
			defectCategories = out.getDefectCategories();
			defectCategories.clear();
		} else {
			defectCategories = new ArrayList<DefectCategory>();
		}
		defectCategories.addAll(cloneDefectCategories(in.getDefectCategories(),
				out));
	}

	private static List<DefectCategory> cloneDefectCategories(
			List<DefectCategory> defectCategories, TemplateValues owner) {
		if (defectCategories == null)
			return Collections.emptyList();
		List<DefectCategory> lst = new ArrayList<DefectCategory>(
				defectCategories.size());
		for (DefectCategory dc : defectCategories) {
			DefectCategory dcNew = new DefectCategory();
			dcNew.setIncluded(dc.isIncluded());
			dcNew.setName(dc.getName());
			dcNew.setPercentage(dc.getPercentage());
			dcNew.setOwner(owner);
			lst.add(dcNew);
		}
		return lst;
	}

	private static List<Milestone> cloneMilestones(List<Milestone> milestones,
			Map<Phase, Phase> phases, TemplateValues owner) {
		if (milestones == null)
			return Collections.emptyList();
		List<Milestone> lst = new ArrayList<Milestone>(milestones.size());
		for (Milestone ms : milestones) {
			Milestone msNew = new Milestone();
			msNew.setAcronym(ms.getAcronym());
			msNew.setDescription(ms.getDescription());
			msNew.setIncluded(ms.isIncluded());
			msNew.setName(ms.getName());
			msNew.setOwner(owner);
			msNew.setPercentage(ms.getPercentage());
			msNew.setPhase(phases.get(ms.getPhase()));
			msNew.setMilestoneId(ms.getMilestoneId());
			lst.add(msNew);
		}
		return lst;
	}

	private static List<Phase> clonePhases(List<Phase> phases,
			TemplateValues owner, Map<Phase, Phase> mapping) {
		if (phases == null)
			return Collections.emptyList();
		List<Phase> lst = new ArrayList<Phase>(phases.size());
		for (Phase ph : phases) {
			Phase phNew = new Phase();
			phNew.setAcronym(ph.getAcronym());
			phNew.setDescription(ph.getDescription());
			phNew.setName(ph.getName());
			phNew.setNumber(ph.getNumber());
			phNew.setOwner(owner);
			phNew.setPercentage(ph.getPercentage());
			lst.add(phNew);
			mapping.put(ph, phNew);
		}
		return lst;
	}

}
