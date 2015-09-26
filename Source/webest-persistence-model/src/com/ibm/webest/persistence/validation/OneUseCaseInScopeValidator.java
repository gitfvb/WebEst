package com.ibm.webest.persistence.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.ibm.webest.persistence.model.Solution;
import com.ibm.webest.persistence.model.UseCase;
import com.ibm.webest.persistence.model.UseCasePack;

/**
 * Validator class for {@link OneUseCaseInScope} constraint.<br>
 * Validates that at least on use case of a solution is in scope.
 * @author Gregor Schumm
 *
 */
public class OneUseCaseInScopeValidator implements ConstraintValidator<OneUseCaseInScope, Solution> {

	@Override
	public void initialize(OneUseCaseInScope arg0) {
	}

	/**
	 * @param arg0 the solution to validate
	 * @param arg1 the validation context
	 * @return true if the given solution has at least on use case in scope
	 */
	@Override
	public boolean isValid(Solution arg0, ConstraintValidatorContext arg1) {
		if (arg0.getUseCasePacks() != null) {
			for (UseCasePack ucp : arg0.getUseCasePacks()) {
				for (UseCase uc : ucp.getUseCases()) {
					if (uc.isInScope())
						return true;
				}
			}
		}
		return false;
	}

}
