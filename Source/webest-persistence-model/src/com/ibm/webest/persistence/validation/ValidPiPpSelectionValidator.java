package com.ibm.webest.persistence.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.ibm.webest.persistence.model.Solution;

/**
 * Validator class for {@link ValidPiPpSelection} constraint.<br>
 * Validates that at least one of either PP or PI is specified in a solution.
 * @author Gregor Schumm
 *
 */
public class ValidPiPpSelectionValidator
		implements
		ConstraintValidator<com.ibm.webest.persistence.validation.ValidPiPpSelection, Solution> {

	@Override
	public void initialize(ValidPiPpSelection arg0) {
	}

	/**
	 * @param arg0 the solution to validate
	 * @param arg1 the validation context
	 * @return true if in the given solution is a PP or PI specified 
	 */
	@Override
	public boolean isValid(Solution arg0, ConstraintValidatorContext arg1) {
		return arg0.getPp() != null || arg0.getPi() != null;
	}

}
