package com.ibm.webest.persistence.validation;

import java.util.HashSet;
import java.util.Set;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.ibm.webest.persistence.model.Phase;
import com.ibm.webest.persistence.model.TemplateValues;

/**
 * Validator class for {@link UniquePhaseNumbers} constraint.<br>
 * Validates that all phases of a {@link TemplateValues} object have unique numbers.
 * @author Gregor Schumm
 *
 */
public class UniquePhaseNumbersValidator implements
		ConstraintValidator<UniquePhaseNumbers, TemplateValues> {

	@Override
	public void initialize(UniquePhaseNumbers arg0) {
	}

	/**
	 * @param arg0 the object to validate
	 * @param arg1 the validation context
	 * @return true if the given object has no phases with duplicate numbers
	 */
	@Override
	public boolean isValid(TemplateValues arg0, ConstraintValidatorContext arg1) {
		if (arg0.getPhases() != null && arg0.getPhases().size() > 0) {
			Set<Byte> numbers = new HashSet<Byte>(arg0.getPhases().size());
			for (Phase p : arg0.getPhases()) {
				if (numbers.contains(p.getNumber()))
					return false;
				numbers.add(p.getNumber());
			}
		}
		return true;
	}

}
