package com.ibm.webest.persistence.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.ibm.webest.persistence.model.Phase;
import com.ibm.webest.persistence.model.TemplateValues;

/**
 * Validator class for {@link ValidPhasePercentages} constraint.<br>
 * Validates that the sum of all phase percentages of the {@link TemplateValues}' associated phases equal 100%.
 * @author Gregor Schumm
 *
 */
public class ValidPhasePercentagesValidator implements ConstraintValidator<ValidPhasePercentages, TemplateValues> {

	@Override
	public void initialize(ValidPhasePercentages arg0) {
	}

	/**
	 * @param arg0 the object to validate
	 * @param arg1 the validation context
	 * @return true if the sum of the phases' percentages equal 100%  
	 */
	@Override
	public boolean isValid(TemplateValues arg0, ConstraintValidatorContext arg1) {
		if (arg0.getPhases() == null || arg0.getPhases().size() < 1) {
			return false;
		}
		int sum = 0;

		for (Phase p : arg0.getPhases()) {
			if (p.getPercentage() == null)
				return false;
			sum += p.getPercentage();
		}

		return sum == 100;
	}

}
