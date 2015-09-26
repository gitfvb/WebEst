package com.ibm.webest.persistence.validation;

import java.util.HashSet;
import java.util.Set;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.ibm.webest.persistence.model.Milestone;
import com.ibm.webest.persistence.model.TemplateValues;

/**
 * Validator class for {@link UniqueMilestoneIds} constraint.<br>
 * Validates that all milestones of a {@link TemplateValues} object have unique milestone IDs.
 * @author Gregor Schumm
 *
 */
public class UniqueMilestoneIdsValidator implements
		ConstraintValidator<UniqueMilestoneIds, TemplateValues> {

	@Override
	public void initialize(UniqueMilestoneIds arg0) {
	}

	/**
	 * @param arg0 the object to validate
	 * @param arg1 the validation context
	 * @return true if the given object has no milestones with duplicate ids
	 */
	@Override
	public boolean isValid(TemplateValues arg0, ConstraintValidatorContext arg1) {
		if (arg0.getMilestones() != null && arg0.getMilestones().size() > 0) {
			Set<Byte> ids = new HashSet<Byte>(arg0.getMilestones().size());
			for (Milestone m : arg0.getMilestones()) {
				if (ids.contains(m.getMilestoneId()))
					return false;
				ids.add(m.getMilestoneId());
			}
		}
		return true;
	}
}
